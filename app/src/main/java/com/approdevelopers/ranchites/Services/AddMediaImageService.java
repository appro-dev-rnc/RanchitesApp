package com.approdevelopers.ranchites.Services;

import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;
import static com.approdevelopers.ranchites.Services.AddLocationForegroundService.CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.approdevelopers.ranchites.Activities.MyLocationFullDetailPage;
import com.approdevelopers.ranchites.Models.ImagesModel;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Repository.FirebaseStorageRepository;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.UploadTask;

public class AddMediaImageService extends Service {

    private static final int UPLOAD_LOC_MEDIA_IMAGE_NOTIFICATION_ID = 8888;
    private byte[] imageBitmapArray;
    private String documentId;
    private NotificationManager manager;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
                    .setContentText("Adding your place/business media image.")
                    .setSmallIcon(R.drawable.ranchites_app_logo_notif)
                    .setProgress(100, 0, true)
                    .setPriority(PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .build();
            startForeground(UPLOAD_LOC_MEDIA_IMAGE_NOTIFICATION_ID, notification);
            uploadImageToFirebase();



        }

        return super.onStartCommand(intent, flags, startId);
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Media Image Update Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }


    private void uploadImageToFirebase() {

        if (imageBitmapArray!=null){
            Task<UploadTask.TaskSnapshot> task = FirebaseStorageRepository.getStorageRepositoryInstance().
                    uploadLocationMediaWdByteArray(documentId,imageBitmapArray);
            task.addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()){
                    String storagePath = task1.getResult().getStorage().getPath();
                    Task<Uri> urlTask = task1.getResult().getStorage().getDownloadUrl();
                    urlTask.addOnCompleteListener(task11 -> {
                        if (task11.isSuccessful()){
                            String url = task11.getResult().toString();
                            ImagesModel model = new ImagesModel("",url,storagePath);
                            Task<DocumentReference> updateTask= FirestoreRepository.getInstance().updateLocationMediaImages(documentId,model);
                            updateTask.addOnCompleteListener(task111 -> {
                                if (task111.isSuccessful()){
                                    String documentIdRetrieved =  task111.getResult().getId();
                                    task111.getResult().update("documentId",documentIdRetrieved);
                                    stopForeground(true);
                                    stopSelf();
                                    updateImageNotification("Image successfully posted","Tap to view your place");

                                }else {
                                    //updating firestore doc failed
                                    stopForeground(true);
                                    stopSelf();
                                    updateImageNotification("Upload Failed","Try again later");

                                }
                            });
                        }else {
                            //get url task failed
                            stopForeground(true);
                            stopSelf();
                            updateImageNotification("Upload Failed","Try again later");
                        }
                    });


                }else {
                    //task failed completely
                    stopForeground(true);
                    stopSelf();
                    updateImageNotification("Upload Failed","Try again later");
                }
            });

        }


    }


    private void updateImageNotification(String titleText, String contentText) {


        Intent notificationIntent = new Intent(this, MyLocationFullDetailPage.class);
        notificationIntent.putExtra("locationDocId",documentId);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_add_business_black);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(titleText)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ranchites_app_logo_notif)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(PRIORITY_HIGH)
                .build();
        manager.notify(UPLOAD_LOC_MEDIA_IMAGE_NOTIFICATION_ID,notification);
    }

}
