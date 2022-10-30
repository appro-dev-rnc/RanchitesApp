package com.approdevelopers.ranchites.ApplicationFiles;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.maps.MapsInitializer;
import com.google.firebase.FirebaseApp;

public class ApplicationCustomFile extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        MapsInitializer.initialize(this);
        FirebaseApp.initializeApp(this);
    }
}
