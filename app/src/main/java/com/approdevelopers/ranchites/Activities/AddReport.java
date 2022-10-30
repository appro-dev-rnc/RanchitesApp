package com.approdevelopers.ranchites.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.approdevelopers.ranchites.Broadcasts.NetworkConnection;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Utils.ConnectionUtils;
import com.approdevelopers.ranchites.ViewModels.ReportClassViewModel;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class AddReport extends AppCompatActivity implements View.OnClickListener, NetworkConnection.ReceiverListener {

    private static final int REPORT_RESULT_SUCCESS = 1000;
    private static final int REPORT_RESULT_FAILED = 1050;
    String documentId=null;
    int documentType;
    //UI elements
    TextInputLayout textLayoutReportReason, textLayoutReportDesc;
    AutoCompleteTextView autoCompleteReportReason;
    Button btnAddReport;

    ReportClassViewModel reportViewModel;

    NetworkConnection networkConnection;
    ConnectionUtils connectionUtils;
    CoordinatorLayout addReportParentView;
    ArrayAdapter<String> reportsCategoriesAdapter;

    private InterstitialAd mInterstitialAd;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);

        Toolbar toolbar =findViewById(R.id.add_report_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        networkConnection = new NetworkConnection();
        Intent intent = getIntent();
        if (intent!=null){
            documentId = intent.getStringExtra("documentId");
            documentType = intent.getIntExtra("documentType",0);
        }

        if (documentId==null&& documentType==0){
            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            finish();
        }
        //hooks
        textLayoutReportReason = findViewById(R.id.textLayoutReportReason);
        textLayoutReportDesc  = findViewById(R.id.textLayoutReportDescription);
        autoCompleteReportReason = findViewById(R.id.autoTextEditReportReason);
        btnAddReport = findViewById(R.id.btn_add_report);
        addReportParentView = findViewById(R.id.add_report_parent_view);

        reportViewModel = new ViewModelProvider(this).get(ReportClassViewModel.class);

        reportViewModel.getReportsCategory(documentType).observe(this, strings -> {
            if (strings!=null && !strings.isEmpty()){
                reportsCategoriesAdapter = new ArrayAdapter<>(AddReport.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, strings);
                autoCompleteReportReason.setAdapter(reportsCategoriesAdapter);
            }
        });

        btnAddReport.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        autoCompleteReportReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textLayoutReportReason.setErrorEnabled(false);
                textLayoutReportReason.setError("");
                reportViewModel.setReportReason(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        autoCompleteReportReason.setOnItemClickListener((adapterView, view, i, l) -> reportViewModel.setReportReason(adapterView.getItemAtPosition(i).toString()));

        Objects.requireNonNull(textLayoutReportDesc.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textLayoutReportDesc.setError("");
                textLayoutReportDesc.setErrorEnabled(false);
                reportViewModel.setReportDesc(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        AdRequest adRequest = new AdRequest.Builder().build();
        String interstitial_ad_unit = getString(R.string.ranchites_interstitial_report_place_profile_ad_unit);
        InterstitialAd.load(this,interstitial_ad_unit, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error

                        mInterstitialAd = null;
                    }
                });

    }



    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btn_add_report){
            if (isNetworkAvailable()){
                validateReportAndUpdate();

            }else {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void validateReportAndUpdate() {
        String reportDesc = reportViewModel.getReportDesc();
        String reportReason = reportViewModel.getReportReason();

        if (reportReason==null || reportReason.equals("")){
            textLayoutReportReason.setError("Reason cannot be empty");
            return;
        }
        if (reportDesc==null || reportDesc.equals("")){
            textLayoutReportDesc.setError("Description is empty");
            return;
        }

        if (mInterstitialAd != null) {
            mInterstitialAd.show(AddReport.this);
            mInterstitialAd.setFullScreenContentCallback(contentCallback);
        } else {
            reportAndObserve();
        }




    }

    private void reportAndObserve(){
        String reportDesc = reportViewModel.getReportDesc();
        String reportReason = reportViewModel.getReportReason();
        reportViewModel.addToReports(documentType,documentId,reportReason,reportDesc).observe(this, integer -> {
            if (integer == REPORT_RESULT_SUCCESS){
                Toast.makeText(AddReport.this, "Reported", Toast.LENGTH_SHORT).show();
                finish();
            }
            if (integer == REPORT_RESULT_FAILED) {
                Toast.makeText(AddReport.this, "Failed to report \n Try again later ...", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectionUtils = new ConnectionUtils(AddReport.this,networkConnection,addReportParentView);
        NetworkConnection.Listener = this;
        connectionUtils.checkConnection();



    }

    @Override
    protected void onPause() {
        super.onPause();
        connectionUtils.destroySnackBar();

        this.unregisterReceiver(networkConnection);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof TextInputEditText || v instanceof AppCompatAutoCompleteTextView) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    @Override
    public void onNetworkChange(boolean isConnected) {
        connectionUtils.showSnackBar(isConnected);
    }

    FullScreenContentCallback contentCallback = new FullScreenContentCallback() {
        @Override
        public void onAdClicked() {
            super.onAdClicked();
        }

        @Override
        public void onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent();
            mInterstitialAd = null;
            reportAndObserve();

        }

        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
            super.onAdFailedToShowFullScreenContent(adError);
            mInterstitialAd = null;
            reportAndObserve();
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