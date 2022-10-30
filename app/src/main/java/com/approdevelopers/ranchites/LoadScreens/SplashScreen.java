package com.approdevelopers.ranchites.LoadScreens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.approdevelopers.ranchites.LoginActivity;
import com.approdevelopers.ranchites.MainActivity;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Repository.FirebaseStorageRepository;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    private static final String ONB_SCREEN_VIEWED = "onBoardingScreenViewed";


    TextView txtTitle,txtSubTitle;
    Animation titleAnim,subTitleAnim,appLogoAnim;
    private static final int SPLASH_SCREEN_TIMEOUT=3750;
    ImageView imgSplashScrAppLogo;

    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        //hooks
        txtTitle = findViewById(R.id.txtTitle);
        txtSubTitle = findViewById(R.id.txtSubTitle);
        subTitleAnim = AnimationUtils.loadAnimation(this,R.anim.sub_title_anim_splash);
        titleAnim= AnimationUtils.loadAnimation(this,R.anim.title_anim_splash);
        appLogoAnim= AnimationUtils.loadAnimation(this,R.anim.splash_logo_anim);
        imgSplashScrAppLogo = findViewById(R.id.img_splash_scr_app_logo);

        imgSplashScrAppLogo.setAnimation(appLogoAnim);
        txtTitle.setAnimation(titleAnim);

        txtSubTitle.setAnimation(subTitleAnim);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();




        FirestoreRepository.getInstance();
        FirebaseStorageRepository.getStorageRepositoryInstance();

        Intent intent ;

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        // Check if we need to display our OnboardingSupportFragment
        if (!sharedPreferences.getBoolean(
                ONB_SCREEN_VIEWED, false)) {
            // The user hasn't seen the OnboardingSupportFragment yet, so show it
            intent = new Intent(this, OnBoardingScreen.class);
        }else {
            if (currentUser!=null && currentUser.getMetadata()!=null){

                    intent = new Intent(SplashScreen.this, MainActivity.class);

            }
            else {
                intent = new Intent(SplashScreen.this,LoginActivity.class);
            }
        }


        new Handler().postDelayed(() -> {
         startActivity(intent);
            finish();
        },SPLASH_SCREEN_TIMEOUT);
    }
}