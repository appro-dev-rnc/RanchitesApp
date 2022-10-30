package com.approdevelopers.ranchites.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;

import com.approdevelopers.ranchites.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class FirebaseNotifCloudService extends FirebaseMessagingService {


    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    NotificationChannel channel;
    Notification.Builder notification;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        String title = Objects.requireNonNull(message.getNotification()).getTitle();
        String text = message.getNotification().getBody();

        String CHANNEL_ID = "FIREBASE_CLOUD_NOTIF";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channel = new NotificationChannel(CHANNEL_ID,"Server Notifications"
                , NotificationManager.IMPORTANCE_HIGH);
            }
        }

        if (channel!=null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getSystemService(NotificationManager.class).createNotificationChannel(channel);
            }

        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
             notification= new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setSmallIcon(R.drawable.ranchites_app_logo_notif)
                    .setAutoCancel(true);
        }else {
            notification= new Notification.Builder(this)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setSmallIcon(R.drawable.ranchites_app_logo_notif)
                    .setAutoCancel(true);
        }

        int NOTIF_ID = 1022;
        NotificationManagerCompat.from(this).notify(NOTIF_ID,notification.build());

    }
}
