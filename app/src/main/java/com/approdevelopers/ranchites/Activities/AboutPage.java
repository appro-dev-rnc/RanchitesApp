package com.approdevelopers.ranchites.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.approdevelopers.ranchites.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class AboutPage extends AppCompatActivity {

    private InterstitialAd mInterstitialAdAboutPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about_page);
        AdRequest adRequest = new AdRequest.Builder().build();
        String interstitial_ad_unit = getString(R.string.ranchites_interstitial_about_page_ad_unit);

        InterstitialAd.load(this,interstitial_ad_unit, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.

                        mInterstitialAdAboutPage = interstitialAd;
                        mInterstitialAdAboutPage.setFullScreenContentCallback(contentCallbackAboutPage);
                        mInterstitialAdAboutPage.show(AboutPage.this);

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error

                        mInterstitialAdAboutPage = null;

                    }
                });

        Toolbar toolbar = findViewById(R.id.toolbar_about);
        toolbar.setTitle("About and How To ?");
        setSupportActionBar(toolbar);


        toolbar.setNavigationOnClickListener(view -> onBackPressed());








       
    }
    FullScreenContentCallback contentCallbackAboutPage = new FullScreenContentCallback() {
        @Override
        public void onAdClicked() {
            super.onAdClicked();
        }

        @Override
        public void onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent();
            mInterstitialAdAboutPage= null;

        }

        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
            super.onAdFailedToShowFullScreenContent(adError);
            mInterstitialAdAboutPage= null;

        }

        @Override
        public void onAdImpression() {
            super.onAdImpression();
        }

        @Override
        public void onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent();
        }
    };

}