package com.approdevelopers.ranchites.Services;

import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.approdevelopers.ranchites.Activities.MyLocationFullDetailPage;
import com.approdevelopers.ranchites.Models.EditLocationModel;
import com.approdevelopers.ranchites.Models.LocationModel;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Repository.FirebaseStorageRepository;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;


public class AddLocationForegroundService extends Service {

    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    public static final String CANCEL_INTENT_ACTION = "cancel";
    private static final int UPDATE_NOTIFICATION_ID = 5555;
    private static final int ADD_NOTIFICATION_ID = 6666;

    private String documentId;
    private String title;
    private String desc;
    private String address;
    private String category;
    private String imageUrl;
    private String ownerId;
    private String documentAddedId;
    private boolean locationShareEnabled;
    private boolean editLocation;
    private boolean addLocation;
    private byte[] bitmapArray;
    private double locationLatitude, locationLongitude;
    private EditLocationModel editLocationModel;
    private LocationModel addLocationModel;

    private ArrayList<String> keywords;

    NotificationManager manager;

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


        editLocation = intent.getBooleanExtra("editLocation", false);
        addLocation = intent.getBooleanExtra("addLocation", false);
        if (editLocation && !addLocation) {
            documentId = intent.getExtras().getString("documentId");
            title = intent.getStringExtra("title");
            desc = intent.getStringExtra("desc");
            address = intent.getStringExtra("address");
            category = intent.getStringExtra("category");
            imageUrl = intent.getStringExtra("imageUrl");
            locationShareEnabled = intent.getBooleanExtra("locationShareEnabled", false);
            if (!intent.getExtras().getBoolean("bitmapNull")) {
                bitmapArray = intent.getExtras().getByteArray("bitmapArray");
            }
            locationLatitude = intent.getDoubleExtra("locationLatitude", 0.0);
            locationLongitude = intent.getDoubleExtra("locationLongitude", 0.0);
            GeoPoint geoPoint;
            if (locationLongitude == 0.0 && locationLatitude == 0.0) {
                geoPoint = null;
            } else {
                geoPoint = new GeoPoint(locationLatitude, locationLongitude);

            }

            keywords = intent.getStringArrayListExtra("keywords");


            editLocationModel = new EditLocationModel(documentId, title, desc, address, category, imageUrl, geoPoint, locationShareEnabled,Timestamp.now());

        }
        if (addLocation && !editLocation) {
            title = intent.getStringExtra("title");
            desc = intent.getStringExtra("desc");
            address = intent.getStringExtra("address");
            category = intent.getStringExtra("category");
            ownerId = intent.getStringExtra("ownerId");
            locationShareEnabled = intent.getBooleanExtra("locationShareEnabled", false);
            if (!intent.getExtras().getBoolean("bitmapNull")) {
                bitmapArray = intent.getExtras().getByteArray("bitmapArray");
            }
            locationLatitude = intent.getDoubleExtra("locationLatitude", 0.0);
            locationLongitude = intent.getDoubleExtra("locationLongitude", 0.0);
            GeoPoint geoPoint;
            if (locationLongitude == 0.0 && locationLatitude == 0.0) {
                geoPoint = null;
            } else {
                geoPoint = new GeoPoint(locationLatitude, locationLongitude);

            }

            keywords = intent.getStringArrayListExtra("keywords");

            addLocationModel = new LocationModel("",title,desc,address,category,"",ownerId,geoPoint,Timestamp.now(),0,0,1,locationShareEnabled);

        }


        String action = intent.getStringExtra("actionCancel");
        if (!TextUtils.isEmpty(action) && action.matches(CANCEL_INTENT_ACTION)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stopForeground(true);
                stopSelf();
            }

        } else {
            createNotificationChannel();

            Intent cancenIntent = new Intent(this, AddLocationForegroundService.class);
            cancenIntent.putExtra("actionCancel", CANCEL_INTENT_ACTION);
            PendingIntent cancelPendingIntent = PendingIntent.getService(this, 12, cancenIntent, 0);


            if (editLocation && !addLocation) {
                Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("Updating data")
                        .setContentText(title + " data is being updated")
                        .setSmallIcon(R.drawable.ranchites_app_logo_notif)
                        .setProgress(100, 0, true)
                        .setPriority(PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .build();
                startForeground(UPDATE_NOTIFICATION_ID, notification);
                updateLocData();
            }
            if (addLocation && !editLocation) {
                Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("Uploading data")
                        .setContentText("Adding new place/business")
                        .setSmallIcon(R.drawable.ranchites_app_logo_notif)
                        .setProgress(100, 0, true)
                        .setPriority(PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .build();
                startForeground(ADD_NOTIFICATION_ID, notification);
                addNewLocation();
            }


        }
        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Update Place/Business Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }


    public void updateLocData() {
        FirestoreRepository firestoreRepository = FirestoreRepository.getInstance();
        FirebaseStorageRepository storageRepository = FirebaseStorageRepository.getStorageRepositoryInstance();
        Task<Void> uploadTask = firestoreRepository.updateLocationData(editLocationModel);
        if (uploadTask!=null){
            uploadTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (editLocation && imageUrl == null) {

                        UploadTask taskBitmap = storageRepository.uploadLoBannerImageByteArray(documentId, bitmapArray);
                        taskBitmap.addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()){
                                String bannerImgPath = "Locations/" + documentId + "/BannerImage/";
                                String bannerImgName = "BannerImage_" + documentId;
                                StorageReference mBannerImageReference = storageRepository.getmStorageReference().child(bannerImgPath + bannerImgName);
                                mBannerImageReference.getDownloadUrl().addOnCompleteListener(task11 -> {
                                    if (task11.isSuccessful()){
                                        String imgUri = task11.getResult().toString();
                                        firestoreRepository.getmLocationReference().document(documentId).update("imageUrl", imgUri).addOnCompleteListener(task111 -> {

                                            stopForeground(true);
                                            stopSelf();

                                            updateNotification("Checkout your updated place/business", title + " was updated successfully");

                                        });

                                    }else {
                                        stopForeground(true);
                                        stopSelf();

                                        updateNotification("Failed to update banner image data", title + " was updated successfully");

                                    }
                                });


                            }else {
                                stopForeground(true);
                                stopSelf();
                                updateNotification("Failed to update banner image", "Try updating later");


                            }
                        });

                    } else {

                        stopForeground(true);
                        stopSelf();
                        updateNotification("Checkout your updated place/business", title + " was updated successfully");

                    }

                    firestoreRepository.addKeywordsToPlaceDoc(documentId,keywords);

                } else {
                    stopForeground(true);
                    stopSelf();
                    updateNotification("Failed to update", "Try updating later ");
                }
            });
        }

    }

    private void updateNotification(String notifTitle, String notifDesc) {

        Intent notificationIntent = new Intent(this, MyLocationFullDetailPage.class);
        notificationIntent.putExtra("locationDocId", documentId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(notifTitle)
                .setContentText(notifDesc)
                .setSmallIcon(R.drawable.ranchites_app_logo_notif)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(PRIORITY_HIGH)
                .build();
        manager.notify(UPDATE_NOTIFICATION_ID, notification);
    }

    public void addNewLocation() {
        FirestoreRepository firestoreRepository = FirestoreRepository.getInstance();
        FirebaseStorageRepository storageRepository = FirebaseStorageRepository.getStorageRepositoryInstance();
        if (addLocationModel != null) {
            Task<DocumentReference> uploadTask = firestoreRepository.addLocation(addLocationModel);
            if (uploadTask!=null){
                uploadTask.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentReference documentReference = task.getResult();
                        documentAddedId = documentReference.getId();
                        documentReference.update("documentId", documentAddedId);
                        firestoreRepository.addKeywordsToPlaceDoc(documentAddedId,keywords);


                        if (bitmapArray != null) {
                            UploadTask taskbitmap = storageRepository.uploadLoBannerImageByteArray(documentAddedId, bitmapArray);
                            taskbitmap.addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {

                                    String bannerImgPath = "Locations/" + documentAddedId + "/BannerImage/";
                                    String bannerImgName = "BannerImage_" + documentAddedId;
                                    StorageReference mBannerImageReference = storageRepository.getmStorageReference().child(bannerImgPath + bannerImgName);
                                    mBannerImageReference.getDownloadUrl().addOnCompleteListener(task11 -> {
                                        if (task11.isSuccessful()) {
                                            String imgUri = task11.getResult().toString();
                                            firestoreRepository.getmLocationReference().document(documentAddedId).update("imageUrl", imgUri);
                                        }
                                        stopForeground(true);
                                        stopSelf();
                                        updateAddNotification("Upload successful .", "Tap to view");
                                    });
                                } else {
                                    stopForeground(true);
                                    stopSelf();
                                    updateAddNotification("Upload successful . Failed to add image.", "Tap to view");

                                }
                            });


                        } else {
                            //bitmap array null
                            stopForeground(true);
                            stopSelf();
                            updateAddNotification("Upload successful . Failed to add image.", "Tap to view");

                        }

                    } else {
                        stopForeground(true);
                        stopSelf();
                        updateAddNotification("Upload Failed", "Try again later.");
                    }
                });
            }

        } else {
            stopForeground(true);
            stopSelf();
            updateAddNotification("Upload Failed", "Content data missing");

        }


    }

    private void updateAddNotification(String title, String content_message) {

        if (documentAddedId != null) {
            Intent notificationIntent = new Intent(this, MyLocationFullDetailPage.class);
            notificationIntent.putExtra("locationDocId", documentAddedId);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, 0);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(content_message)
                    .setSmallIcon(R.drawable.ranchites_app_logo_notif)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setPriority(PRIORITY_HIGH)
                    .build();

            manager.notify(ADD_NOTIFICATION_ID, notification);
        }
        if (documentAddedId == null) {
            Intent notificationIntent = new Intent(this, MyLocationFullDetailPage.class);
            notificationIntent.putExtra("locationDocId", documentId);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, 0);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(content_message)
                    .setSmallIcon(R.drawable.ranchites_app_logo_notif)
                    .setAutoCancel(true)
                    .setPriority(PRIORITY_HIGH)
                    .build();

            manager.notify(ADD_NOTIFICATION_ID, notification);
        }


    }

    public boolean validateImageUrl() {
        return imageUrl != null && !imageUrl.isEmpty();
    }

}
