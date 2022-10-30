package com.approdevelopers.ranchites.Services;

import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;
import static com.approdevelopers.ranchites.Services.AddLocationForegroundService.CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.approdevelopers.ranchites.Models.UserUpdateModel;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Repository.FirebaseStorageRepository;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.approdevelopers.ranchites.UserProfile;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class UpdateMyProfileService extends Service {

    private static final int UPLOAD_USER_PROFILE_NOTIFICATION_ID = 7777;
    private String userId=null;
    private byte[] bitmapArray=null;
    private String userName=null;
    private String userAbout=null;
    private String userProfileImageUrl=null;
    private boolean searchByProfessionEnabled=false;
    private String userProfession=null;
    private NotificationManager manager;

    private ArrayList<String> keywords;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent!=null){
            userId = intent.getStringExtra("userId");
            userName = intent.getStringExtra("userName");
            userAbout = intent.getStringExtra("userAbout");
            userProfileImageUrl = intent.getStringExtra("userProfileImageUrl");
            userProfession = intent.getStringExtra("userProfession");
            searchByProfessionEnabled = intent.getBooleanExtra("searchByProfessionEnabled",false);

            if (!intent.getExtras().getBoolean("bitmapNull")) {
                bitmapArray = intent.getExtras().getByteArray("bitmapArray");
            }

            keywords = intent.getStringArrayListExtra("keywords");
        }

        if (userId!=null){
            createNotificationChannel();


            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Updating profile")
                    .setContentText("Your profile is being updated")
                    .setSmallIcon(R.drawable.ranchites_app_logo_notif)
                    .setProgress(100, 0, true)
                    .setPriority(PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .build();
            startForeground(UPLOAD_USER_PROFILE_NOTIFICATION_ID, notification);


            if (bitmapArray!=null ){
                uploadBitmapToFireStorage();
            }else {
                updateUserProfileToFireStore(userProfileImageUrl);
            }

        }




        return super.onStartCommand(intent, flags, startId);
    }



    private void uploadBitmapToFireStorage(){
        if (bitmapArray!=null){
            FirebaseStorageRepository storageRepository = FirebaseStorageRepository.getStorageRepositoryInstance();
            UploadTask task = storageRepository
                    .uploadUserProfDisplayWdByteArray(userId,bitmapArray);
            task.addOnCompleteListener(task12 -> {
                if (task12.isSuccessful()){
                    Task<Uri> urlTask = task12.getResult().getStorage().getDownloadUrl();
                    urlTask.addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()){
                            String profImageUrl = task1.getResult().toString();
                            //continue to update further data
                            updateUserProfileToFireStore(profImageUrl);
                        }else {
                            //image url getting failed
                            stopForeground(true);
                            stopSelf();
                            updateImageNotification("Failed to update profile picture data","Try again later");
                        }
                    });
                }else {
                    //image upload failed
                    stopForeground(true);
                    stopSelf();
                    updateImageNotification("Failed to upload profile picture","Try again later");
                }
            });
        }

    }
    private void updateUserProfileToFireStore(String profImageUrl){
        UserUpdateModel updateModel = new UserUpdateModel(userId,userName,searchByProfessionEnabled,userProfession,userAbout,profImageUrl,2);
        Task<Void> updateUserTask = FirestoreRepository.getInstance().updateUserProfileData(updateModel);
        if (updateUserTask!=null){
            updateUserTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    FirestoreRepository.getInstance().updateUserProfileKeywords(userId,keywords);
                    stopForeground(true);
                    stopSelf();
                    updateImageNotification("Profile Successfully Updated","Tap to view your profile");
                }
                else {
                    stopForeground(true);
                    stopSelf();
                    updateImageNotification("Failed to update profile","Try again later");
                }
            });
        }

    }



    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Update Profile Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }


    private void updateImageNotification(String titleText, String contentText) {


        Intent notificationIntent = new Intent(this, UserProfile.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(titleText)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ranchites_app_logo_notif)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(PRIORITY_HIGH)
                .build();
        manager.notify(UPLOAD_USER_PROFILE_NOTIFICATION_ID,notification);
    }

}
