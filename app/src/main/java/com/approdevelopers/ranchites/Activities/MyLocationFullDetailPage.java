package com.approdevelopers.ranchites.Activities;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.approdevelopers.ranchites.Adapters.MyLocationsPageTabAdapter;
import com.approdevelopers.ranchites.ApplicationFiles.GlideApp;
import com.approdevelopers.ranchites.Broadcasts.NetworkConnection;
import com.approdevelopers.ranchites.MainActivity;
import com.approdevelopers.ranchites.Models.LocationModel;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.approdevelopers.ranchites.Services.AddMediaImageService;
import com.approdevelopers.ranchites.UserProfile;
import com.approdevelopers.ranchites.Utils.ConnectionUtils;
import com.approdevelopers.ranchites.ViewModels.MyLocFullDetailViewModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MyLocationFullDetailPage extends AppCompatActivity implements NetworkConnection.ReceiverListener, View.OnClickListener {


    //Constants
    private static final int CAMERA_REQUEST_CODE = 4010;
    private static final int STORAGE_REQUEST_CODE = 4015;
    private static final int IMAGE_PICKER_REQUEST_CODE = 7484;


    //permissions array
    private String[] cameraPermissions;
    private String[] storagePermissions;

    MyLocFullDetailViewModel myLocFullDetailViewModel;
    private String locationDocId;




    //Ui elements
    TextView  txtMyLocDetailAddress,txtMyLocDetailCategory,txtMyLocDetailSearchCount;
    AppCompatRatingBar myLocDetailRating;
    ImageView imgMyLocDetailBanner;

    TabLayout myLocationTabLayout;
    ViewPager2 myLocationViewPager;
    CollapsingToolbarLayout myLocationCollapsingToolbar;
    AppBarLayout myLocationAppBarLayout;


    NetworkConnection networkConnection;
    ConnectionUtils connectionUtils;
    CoordinatorLayout myLocParentView;

    String locDetBannerImageUrl;

    ProgressDialog progressDialog;
    FloatingActionButton btnEditMyLoc;
    private Bitmap finalAccountMediaImage;



    String[] tabNames = {"About","Media","Map","Reviews and Ratings"};

    private RewardedInterstitialAd mRewardedAd;
    private int imageCount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_location_detail_page_modified);

        networkConnection = new NetworkConnection();

        Intent intent = getIntent();
        locationDocId = intent.getStringExtra("locationDocId");


        if (locationDocId!=null)
        myLocFullDetailViewModel = new ViewModelProvider(this)
                .get(MyLocFullDetailViewModel.class);

        else finish();

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};


        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);



        //hooks

        txtMyLocDetailCategory = findViewById(R.id.txtMyLocDetailCategory);
        txtMyLocDetailAddress = findViewById(R.id.txtMyLocDetailAddress);
        txtMyLocDetailSearchCount = findViewById(R.id.txtMyLocDetailSearchCount);
        myLocDetailRating = findViewById(R.id.ratingMyLocDetail);
        imgMyLocDetailBanner = findViewById(R.id.imgMyLocDetailBanner);
        myLocParentView = findViewById(R.id.my_loc_parent_view);

        myLocationTabLayout = findViewById(R.id.my_location_tab_layout);
        myLocationViewPager = findViewById(R.id.my_location_view_pager);
        myLocationCollapsingToolbar = findViewById(R.id.my_location_collapsing_toolbar_layout);
        myLocationAppBarLayout = findViewById(R.id.my_location_app_barlayout);
        btnEditMyLoc = findViewById(R.id.btn_edit_my_loc_float);



        Toolbar toolbar = findViewById(R.id.my_location_toolbar);
        setSupportActionBar(toolbar);

        myLocationTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        MyLocationsPageTabAdapter myLocationsPageTabAdapter = new MyLocationsPageTabAdapter(getSupportFragmentManager(),getLifecycle(),locationDocId);
        myLocationViewPager.setAdapter(myLocationsPageTabAdapter);
        myLocationViewPager.setOffscreenPageLimit(3);




        myLocFullDetailViewModel.getCurrentLocationDetailLiveData(locationDocId).observe(this, locationModel -> {
            if (locationModel!=null&& !locationModel.toString().isEmpty()){
                locDetBannerImageUrl = locationModel.getImageUrl();
                loadDataInUI(locationModel);
            }else {
                locDetBannerImageUrl = null;
                loadRawData();
            }
        });

        myLocFullDetailViewModel.loadLocationMediaImage(locationDocId).observe(this, imagesModels -> {
            if (imagesModels!=null && !imagesModels.isEmpty()){
                imageCount = imagesModels.size();
            }else {
                imageCount = 0;
            }
        });

        new TabLayoutMediator(myLocationTabLayout,myLocationViewPager,(tab, position) -> tab.setText(tabNames[position])).attach();


        myLocationAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {

            if (Math.abs(verticalOffset)  >= myLocationAppBarLayout.getTotalScrollRange()) { // collapse

                Objects.requireNonNull(toolbar.getOverflowIcon()).setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(Color.BLACK,PorterDuff.Mode.SRC_ATOP);



            } else if (verticalOffset == 0) { // fully expand
                Objects.requireNonNull(toolbar.getOverflowIcon()).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(Color.WHITE,PorterDuff.Mode.SRC_ATOP);

            }
        });

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        imgMyLocDetailBanner.setOnClickListener(this);
        btnEditMyLoc.setOnClickListener(this);




    }


    @Override
    public void onBackPressed() {
        if (!isTaskRoot()){
            super.onBackPressed();
        }else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        connectionUtils = new ConnectionUtils(MyLocationFullDetailPage.this,networkConnection,myLocParentView);
        NetworkConnection.Listener =this;
        connectionUtils.checkConnection();



    }

    @Override
    protected void onPause() {
        super.onPause();
        connectionUtils.destroySnackBar();

        this.unregisterReceiver(networkConnection);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_my_loc,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.menu_item_my_loc_add_image){

            if (imageCount%4==0){

                Toast.makeText(MyLocationFullDetailPage.this, "To add more images,\nWatch upcoming ad fully.", Toast.LENGTH_SHORT).show();


                String rewarded_ad_unit = getString(R.string.ranchites_rewarded_loc_media_image_ad_unit);

                RewardedInterstitialAd.load(this,rewarded_ad_unit,
                        new AdRequest.Builder().build(),  new RewardedInterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull RewardedInterstitialAd ad) {

                                mRewardedAd = ad;
                                mRewardedAd.setFullScreenContentCallback(rewardedCallback);
                            }
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                mRewardedAd = null;
                            }
                        });
            }


            showImagePickerDialog();
        }

        if (item.getItemId()==R.id.menu_item_my_loc_delete){
            if (isNetworkAvailable()){
                showDeleteAlertDialog();

            }else {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onPrepareOptionsMenu(@NonNull Menu menu) {
        myLocationAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {

            if (Math.abs(verticalOffset)  >= myLocationAppBarLayout.getTotalScrollRange()) { // collapse

                 menu.findItem(R.id.menu_item_my_loc_add_image).setIcon(R.drawable.ic_add_image_black);
                 menu.findItem(R.id.menu_item_my_loc_delete).setIcon(R.drawable.ic_delete_black);

            } else if (verticalOffset == 0) { // fully expand
                Objects.requireNonNull(menu).findItem(R.id.menu_item_my_loc_add_image).setIcon(R.drawable.ic_add_image_white);
                Objects.requireNonNull(menu).findItem(R.id.menu_item_my_loc_delete).setIcon(R.drawable.ic_delete_white);
            }

        });
        return super.onPrepareOptionsMenu(menu);
    }

    private void loadDataInUI(LocationModel locationModel) {
        GlideApp.with(this).load(locationModel.getImageUrl())
                .timeout(20000)
                .thumbnail(GlideApp.with(this).load(locationModel.getImageUrl()).override(30))
                .placeholder(R.drawable.ic_loading)
                .error( GlideApp.with(this).load(locationModel.getImageUrl())
                        .timeout(20000)
                        .thumbnail(GlideApp.with(this).load(locationModel.getImageUrl()).override(30))
                        .placeholder(R.drawable.ic_loading)
                        .into(imgMyLocDetailBanner))
                .into(imgMyLocDetailBanner);
        myLocationCollapsingToolbar.setTitle(locationModel.getTitle());
       txtMyLocDetailCategory.setText(locationModel.getCategory());
       txtMyLocDetailAddress.setText(locationModel.getAddress());
       txtMyLocDetailSearchCount.setText(String.valueOf(locationModel.getSearchCount()));
       myLocDetailRating.setRating((float) locationModel.getOverallRating());
    }
    private void loadRawData() {
        imgMyLocDetailBanner.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_person,getTheme()));
        myLocationCollapsingToolbar.setTitle("");
        txtMyLocDetailCategory.setText("");
        txtMyLocDetailAddress.setText("");
        txtMyLocDetailSearchCount.setText("");
        myLocDetailRating.setRating(0.0F);
    }

    private void showDeleteAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete ")
                .setMessage("Are you sure you want to delete this place/business?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // Continue with delete operation
                    initDeletePlaceService();
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void initDeletePlaceService() {
        showDeleteProgressDialog();
        Task<Void> task = FirestoreRepository.getInstance().deletePlaceRecord(locationDocId);
        task.addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()){
                FirestoreRepository.getInstance().addToDeletedPlaceRecord(locationDocId);
                showDeletedDialog();
                new Handler().postDelayed(() -> {
                    if (progressDialog!=null && progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    finish();
                },1000);

            }else {
                showDeletePlaceFailedDialog();
                new Handler().postDelayed(() -> {
                    if (progressDialog!=null && progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                },1000);
                 }
        });
        
    }

    private void showDeleteProgressDialog(){
        progressDialog.setTitle("Deleting");
        progressDialog.setMessage("Deleting Place/Business from collection");
        progressDialog.show();
    }

    private void showDeletedDialog(){
        progressDialog.setTitle("Deleted");
        progressDialog.setMessage("Place/business successfully deleted");
        progressDialog.show();
    }private void showDeletePlaceFailedDialog(){
        progressDialog.setTitle("Failed");
        progressDialog.setMessage("Failed to delete Place/business");
        progressDialog.show();
    }



    @Override
    public void onNetworkChange(boolean isConnected) {
        connectionUtils.showSnackBar(isConnected);
    }


    //method to show alert dialog to choose between camera or gallery
    private void showImagePickerDialog() {
        //options (camera,gallery) to show in dialog
        String[] options = {"Camera", "Gallery"};

        //Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image From");
        //set options
        builder.setItems(options, (dialogInterface, i) -> {
            //item click handler
            if (i == 0) {
                //camera clicked
                checkCameraPermissions();

            }
            if (i == 1) {
                //gallery clicked
                checkStoragePermission();

            }

        });
        builder.create().show();
    }


    //method to check/ request camera permissions
    private void checkCameraPermissions() {
        boolean result_camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result_write_external = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        if (result_camera && result_write_external) {
            pickCropImageFromCamera();
        } else {
            //request permission at run time
            ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
        }
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            pickCropImageFromGallery();
        } else {
            ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
        }
    }


    private void pickCropImageFromCamera(){
        ImagePicker.with(this)
                .cameraOnly()
                .crop()//Crop image(Optional), Check Customization for more option
                .compress(512)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start(IMAGE_PICKER_REQUEST_CODE);
    }

    private void pickCropImageFromGallery(){
        ImagePicker.with(this)
                .galleryOnly()
                .crop()//Crop image(Optional), Check Customization for more option
                .compress(512)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start(IMAGE_PICKER_REQUEST_CODE);
    }



    private void initAddImageService(Bitmap accountMediaImage) {

        if (isNetworkAvailable()){

            Intent imageIntent = new Intent(this, AddMediaImageService.class);

            imageIntent.putExtra("documentId",locationDocId);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if (accountMediaImage != null) {
                accountMediaImage.compress(Bitmap.CompressFormat.JPEG, 25, stream);
            }
            if (accountMediaImage!=null){
                imageIntent.putExtra("bitmapNull",false);
                imageIntent.putExtra("bitmapArray",stream.toByteArray());
            }else {
                imageIntent.putExtra("bitmapNull",true);
            }

            if (isMyServiceRunning()){
                Toast.makeText(this, "Please wait, till the previous task gets finished", Toast.LENGTH_SHORT).show();
            }else {
                ContextCompat.startForegroundService(MyLocationFullDetailPage.this, imageIntent);
                Toast.makeText(this, "Uploading \n Check notifications for more.", Toast.LENGTH_SHORT).show();
                //finish();
            }

        }else {
            Toast.makeText(this, "Turn on Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (AddMediaImageService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.imgMyLocDetailBanner){
            ArrayList<String> imageUrlList = new ArrayList<>();

            imageUrlList.add(locDetBannerImageUrl);

            Intent intent = new Intent(MyLocationFullDetailPage.this, ImageFullScreen.class);
            intent.putStringArrayListExtra("imageUrls", imageUrlList);
            startActivity(intent);
        }

        if (view.getId()==R.id.btn_edit_my_loc_float){
            Intent editIntent = new Intent(MyLocationFullDetailPage.this,AddNewLocationActivity.class);
            editIntent.putExtra("editLocation",true);
            editIntent.putExtra("addLocation",false);
            editIntent.putExtra("locationDocId",locationDocId);
            startActivity(editIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==CAMERA_REQUEST_CODE){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                pickCropImageFromCamera();
            }else {
                Toast.makeText(this, "Camera Permissions Denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode==STORAGE_REQUEST_CODE){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                pickCropImageFromGallery();
            }
            else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER_REQUEST_CODE){
            if (resultCode==RESULT_OK){
                Uri uri;
                if (data != null) {
                    uri = data.getData();

                    Bitmap locationMediaImage = null;
                    try {
                        locationMediaImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (locationMediaImage != null) {

                        if (mRewardedAd != null) {
                             finalAccountMediaImage= locationMediaImage;
                            mRewardedAd.show(this, rewardItem -> {
                                // Handle the reward.
                                initAddImageService(finalAccountMediaImage);
                            });
                        } else {
                            initAddImageService(locationMediaImage);
                        }

                    }
                }
            }

        }
    }


    FullScreenContentCallback rewardedCallback = new FullScreenContentCallback() {
        @Override
        public void onAdClicked() {
            super.onAdClicked();
        }

        @Override
        public void onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent();

            mRewardedAd = null;
        }

        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
            super.onAdFailedToShowFullScreenContent(adError);
            mRewardedAd =  null;
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