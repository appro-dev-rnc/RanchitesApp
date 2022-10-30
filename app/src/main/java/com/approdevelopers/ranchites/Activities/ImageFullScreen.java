package com.approdevelopers.ranchites.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.approdevelopers.ranchites.Adapters.FullScrViewPagerAdapter;
import com.approdevelopers.ranchites.Broadcasts.NetworkConnection;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Repository.FirebaseStorageRepository;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.approdevelopers.ranchites.Utils.ConnectionUtils;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class ImageFullScreen extends AppCompatActivity implements View.OnClickListener, NetworkConnection.ReceiverListener {

    //constants
    private static final int DOCUMENT_TYPE_PLACE = 1;
    private static final int DOCUMENT_TYPE_PROFESSION = 2;

    FullScrViewPagerAdapter fullScrViewPagerAdapter;
    ViewPager2 fullScreenViewPager;
    ArrayList<String> imageUrls;
    int imagePosition;
    boolean isAdmin;
    ArrayList<String> imageStoragePaths;
    ArrayList<String> imageDocumentIds;
    Button btnDelFullScr;
    int documentType;
    String user_place_id;
    NetworkConnection networkConnection;
    ConnectionUtils connectionUtils;
    CoordinatorLayout imageFullScrParentView;

    ProgressDialog progressDialog;

    private InterstitialAd mInterstitialAd;
    private int deleteClickCount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);

        networkConnection = new NetworkConnection();
        deleteClickCount =0;


        Intent intent = getIntent();
        imageUrls = intent.getStringArrayListExtra("imageUrls");
        imagePosition = intent.getIntExtra("imagePosition", 0);
        isAdmin = intent.getBooleanExtra("isAdmin", false);
        imageStoragePaths = intent.getStringArrayListExtra("imageStoragePaths");
        imageDocumentIds = intent.getStringArrayListExtra("imageDocumentIds");
        documentType = intent.getIntExtra("documentType", 0);
        user_place_id = intent.getStringExtra("user_place_id");

        Toolbar toolbar = findViewById(R.id.full_scr_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //hooks
        fullScreenViewPager = findViewById(R.id.full_scr_act_view_pager);
        btnDelFullScr = findViewById(R.id.btn_delete_full_scr);

        imageFullScrParentView = findViewById(R.id.image_full_scr_parent_view);


        populateDataInViewPager();


        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        btnDelFullScr.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectionUtils = new ConnectionUtils(ImageFullScreen.this,networkConnection,imageFullScrParentView);
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
    public void onClick(View view) {
        if (view.getId() == R.id.btn_delete_full_scr) {
            if (isNetworkAvailable()){
                if (documentType == DOCUMENT_TYPE_PLACE || documentType == DOCUMENT_TYPE_PROFESSION) {

                    if (deleteClickCount==3){

                        AdRequest adRequest = new AdRequest.Builder().build();
                        String interstitial_ad_unit = getString(R.string.ranchites_interstitial_delete_image_ad_unit);
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
                        Toast.makeText(this,"Some ads may appear",Toast.LENGTH_SHORT).show();
                        deleteClickCount = 0;
                    }

                    new AlertDialog.Builder(ImageFullScreen.this)
                            .setTitle("Delete image?")
                            .setMessage("Are you sure you want to delete this image from collection?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                // Continue with delete operation

                                if (mInterstitialAd != null) {
                                    mInterstitialAd.show(this);
                                    mInterstitialAd.setFullScreenContentCallback(contentCallback);
                                } else {
                                    deleteUserImage();

                                }


                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                    deleteClickCount++;
                }

            }else {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void deleteUserImage() {
        showDeletingProgressDialog();
        int currentPosition = fullScreenViewPager.getCurrentItem();
        String storagePath = imageStoragePaths.get(currentPosition);
        String imageDocumentId = imageDocumentIds.get(currentPosition);
        FirebaseStorageRepository.getStorageRepositoryInstance().getmStorageReference().child(storagePath).getDownloadUrl().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                deleteFileFromStorage(storagePath, user_place_id, imageDocumentId, currentPosition);
            } else {
                deleteFireStoreRefData(user_place_id, imageDocumentId, currentPosition);
            }
        });

    }

    private void showDeletingProgressDialog() {
        progressDialog.setTitle("Deleting");
        progressDialog.setMessage("Deleting image from your profile");
        progressDialog.show();
    }

    private void deleteFireStoreRefData(String user_place_id, String imageDocumentId, int currentPosition) {
        if (documentType == DOCUMENT_TYPE_PROFESSION) {
            Task<Void> firestoreRefTask = FirestoreRepository.getInstance().deleteProfileImageRecord(user_place_id, imageDocumentId);
            firestoreRefTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    showDeletedProgressDialog();
                    updateData(currentPosition);
                } else {
                    showFailedToDeleteImage();
                    new Handler().postDelayed(() -> {
                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    },1000);
                }
            });

        }
        if (documentType == DOCUMENT_TYPE_PLACE) {
            Task<Void> firestoreRefTask = FirestoreRepository.getInstance().deletePlaceImageRecord(user_place_id, imageDocumentId);
            firestoreRefTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    showDeletedProgressDialog();
                    updateData(currentPosition);
                } else {
                    showFailedToDeleteImage();
                    new Handler().postDelayed(() -> {
                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    },1000);
                }
            });
        }
    }



    private void updateData(int currentPosition) {
        imageStoragePaths.remove(currentPosition);
        imageDocumentIds.remove(currentPosition);
        imageUrls.remove(currentPosition);
        if (imagePosition >= imageUrls.size()) {
            imagePosition--;
        }
        showUpdatingData();
        new Handler().postDelayed(() -> {
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        },1000);
        populateDataInViewPager();

    }

    private void showUpdatingData() {
        progressDialog.setTitle("Updating");
        progressDialog.setMessage("Updating data");
        progressDialog.show();
    }

    private void showDeletedProgressDialog() {
        progressDialog.setTitle("Task Completed");
        progressDialog.setMessage("Image successfully deleted");
        progressDialog.show();
    }

    private void deleteFileFromStorage(String storagePath, String user_place_id, String imageDocumentId, int currentPosition) {
        Task<Void> delImageTask = FirebaseStorageRepository.getStorageRepositoryInstance().deleteUserMyProfileImage(storagePath);
        delImageTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                deleteFireStoreRefData(user_place_id, imageDocumentId, currentPosition);

            } else {
                showFailedToDeleteImage();
                new Handler().postDelayed(() -> {
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                },1000);
            }
        });
    }

    private void showFailedToDeleteImage() {
        progressDialog.setTitle("Task Failed");
        progressDialog.setMessage("Failed to delete image from your profile");
    }

    private void populateDataInViewPager() {
        if (imageUrls.size() > 0) {
            fullScrViewPagerAdapter = new FullScrViewPagerAdapter(getApplicationContext(), imageUrls);
            fullScreenViewPager.setAdapter(fullScrViewPagerAdapter);

            fullScreenViewPager.setCurrentItem(imagePosition);
            fullScrViewPagerAdapter.notifyDataSetChanged();

            if (isAdmin && imageStoragePaths.size() == imageUrls.size()) {
                btnDelFullScr.setVisibility(View.VISIBLE);
            } else {
                btnDelFullScr.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(this, "No image data found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
            deleteUserImage();

        }

        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
            super.onAdFailedToShowFullScreenContent(adError);
            mInterstitialAd = null;
            deleteUserImage();

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