package com.approdevelopers.ranchites.LoadScreens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.approdevelopers.ranchites.Adapters.OnboardingPagesAdapter;
import com.approdevelopers.ranchites.LoginActivity;
import com.approdevelopers.ranchites.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class OnBoardingScreen extends AppCompatActivity implements View.OnClickListener {

    private static final String ONB_SCREEN_VIEWED = "onBoardingScreenViewed";
    //viewpager adapter
    OnboardingPagesAdapter onboardingPagesAdapter;

    //ui element variables
    ViewPager2 viewPagerOnBoarding;
    DotsIndicator dotsIndicatorOnBoarding;
    Button btnSkipAllOnBoarding,btnNextOnBoarding;
    ExtendedFloatingActionButton btnOnbGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_screen);

        getWindow().setStatusBarColor(getColor(R.color.on_boarding_first_page_bg));

        //hooks
        viewPagerOnBoarding = findViewById(R.id.view_pager_on_boarding);
        dotsIndicatorOnBoarding = findViewById(R.id.dots_indicate_on_boarding);
        btnSkipAllOnBoarding = findViewById(R.id.btn_skip_all_on_boarding);
        btnNextOnBoarding = findViewById(R.id.btn_next_on_boarding_scr);
        btnOnbGetStarted = findViewById(R.id.btn_onb_get_started);

        //init adapter
        onboardingPagesAdapter = new OnboardingPagesAdapter(getSupportFragmentManager(),getLifecycle(),3);
        viewPagerOnBoarding.setAdapter(onboardingPagesAdapter);
        viewPagerOnBoarding.setOffscreenPageLimit(3);
        dotsIndicatorOnBoarding.setViewPager2(viewPagerOnBoarding);

        viewPagerOnBoarding.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        getWindow().setStatusBarColor(getColor(R.color.on_boarding_first_page_bg));
                        btnSkipAllOnBoarding.setVisibility(View.VISIBLE);
                        btnNextOnBoarding.setVisibility(View.VISIBLE);
                        btnOnbGetStarted.setVisibility(View.GONE);
                        break;
                    case 1:
                        getWindow().setStatusBarColor(getColor(R.color.on_boarding_second_page_bg));
                        btnSkipAllOnBoarding.setVisibility(View.VISIBLE);
                        btnNextOnBoarding.setVisibility(View.VISIBLE);
                        btnOnbGetStarted.setVisibility(View.GONE);
                        break;
                    case 2:
                        getWindow().setStatusBarColor(getColor(R.color.on_boarding_third_page_bg));
                        btnSkipAllOnBoarding.setVisibility(View.GONE);
                        btnNextOnBoarding.setVisibility(View.GONE);
                        btnOnbGetStarted.setVisibility(View.VISIBLE);
                        break;

                }
            }
        });

        //on click listeners
        btnSkipAllOnBoarding.setOnClickListener(this);
        btnNextOnBoarding.setOnClickListener(this);
        btnOnbGetStarted.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btn_skip_all_on_boarding){
            Intent intent;
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseAuth.getInstance().signOut();
            }
            intent = new Intent(OnBoardingScreen.this, LoginActivity.class);

            startActivity(intent);
            onBoardingScreenViewed();
            finish();
        }

        if (view.getId()==R.id.btn_next_on_boarding_scr){
            int posi = viewPagerOnBoarding.getCurrentItem();
            viewPagerOnBoarding.setCurrentItem(posi+1);
        }
        if (view.getId()==R.id.btn_onb_get_started) {
            Intent intent;
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
               FirebaseAuth.getInstance().signOut();
            }
                intent = new Intent(OnBoardingScreen.this, LoginActivity.class);

            startActivity(intent);
            onBoardingScreenViewed();
            finish();
        }
    }


    private void onBoardingScreenViewed(){
        SharedPreferences.Editor sharedPreferencesEditor =
                PreferenceManager.getDefaultSharedPreferences(this).edit();
        sharedPreferencesEditor.putBoolean(
                ONB_SCREEN_VIEWED, true);
        sharedPreferencesEditor.apply();
    }
}