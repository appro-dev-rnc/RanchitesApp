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

import com.approdevelopers.ranchites.Models.ImagesModel;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Repository.FirebaseStorageRepository;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.approdevelopers.ranchites.UserProfile;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.UploadTask;

public class AddImageService extends Service {

    private static final int UPLOAD_IMAGE_NOTIFICATION_ID = 9999;
    private byte[] imageBitmapArray;
    private String documentId;
    private NotificationManager manager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


            documentId = intent.getStringExtra("documentId");
            if (!intent.getExtras().getBoolean("bitmapNull")) {
                imageBitmapArray = intent.getExtras().getByteArray("bitmapArray");
            }



        if (documentId != null) {
            createNotificationChannel();


            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Posting image")
                    .setContentText("Adding profile media image.")
                    .setSmallIcon(R.drawable.ranchites_app_logo_notif)
                    .setProgress(100, 0, true)
                    .setPriority(PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .build();
            startForeground(UPLOAD_IMAGE_NOTIFICATION_ID, notification);
            uploadImageToFirebase();



        }

        return START_NOT_STICKY;
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Update Image Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void uploadImageToFirebase() {
        if (imageBitmapArray!=null){
            Task<UploadTask.TaskSnapshot> task = FirebaseStorageRepository.getStorageRepositoryInstance().
                    uploadUserAccountMediaWdByteArray(documentId,imageBitmapArray);
            task.addOnCompleteListener(task12 -> {
                if (task12.isSuccessful()){
                    String storagePath = task12.getResult().getStorage().getPath();
                    Task<Uri> urlTask = task12.getResult().getStorage().getDownloadUrl();
                    urlTask.addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()){
                            String url = task1.getResult().toString();
                            ImagesModel model = new ImagesModel("",url,storagePath);
                            Task<DocumentReference> updateTask= FirestoreRepository.getInstance().updateUserAccountMediaImages(documentId,model);
                            updateTask.addOnCompleteListener(task11 -> {
                                if (task11.isSuccessful()){
                                    String documentId =  task11.getResult().getId();
                                    task11.getResult().update("documentId",documentId);
                                }

                                stopForeground(true);
                                stopSelf();
                                updateImageNotification("Image successfully posted","Tap to view your profile");


                            });
                        }else {
                            //url task failed
                            stopForeground(true);
                            stopSelf();
                            updateImageNotification("Upload task failed","Tap to view your profile");

                        }
                    });


                }else {
                    //upload task failed
                    stopForeground(true);
                    stopSelf();
                    updateImageNotification("Upload task failed","Tap to view your profile");

                }
            });

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
        manager.notify(UPLOAD_IMAGE_NOTIFICATION_ID,notification);
    }
}
