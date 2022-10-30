package com.approdevelopers.ranchites.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.approdevelopers.ranchites.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class ContactUsPage extends AppCompatActivity {

    private InterstitialAd mInterstitialAdContactUs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_us_page);

        AdRequest adRequest = new AdRequest.Builder().build();
        String interstitial_ad_unit = getString(R.string.ranchites_interstitial_contact_us_ad_unit);

        InterstitialAd.load(this,interstitial_ad_unit, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.

                        mInterstitialAdContactUs = interstitialAd;
                        mInterstitialAdContactUs.setFullScreenContentCallback(contentCallbackContactUs);
                        mInterstitialAdContactUs.show(ContactUsPage.this);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error

                        mInterstitialAdContactUs = null;

                    }
                });


        Toolbar toolbar = findViewById(R.id.toolbar_contact_us);
        toolbar.setTitle("Contact Us");
        setSupportActionBar(toolbar);

        TextView txtPrivacyLink = findViewById(R.id.txt_privacy_link);
        TextView txtTermsLink = findViewById(R.id.txt_terms_link);

        txtPrivacyLink.setClickable(true);
        txtPrivacyLink.setMovementMethod(LinkMovementMethod.getInstance());
        txtTermsLink.setClickable(true);
        txtTermsLink.setMovementMethod(LinkMovementMethod.getInstance());

        toolbar.setNavigationOnClickListener(view -> onBackPressed());



    }


    FullScreenContentCallback contentCallbackContactUs = new FullScreenContentCallback() {
        @Override
        public void onAdClicked() {
            super.onAdClicked();
        }

        @Override
        public void onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent();
            mInterstitialAdContactUs= null;

        }

        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
            super.onAdFailedToShowFullScreenContent(adError);
            mInterstitialAdContactUs = null;

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