package com.approdevelopers.ranchites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.approdevelopers.ranchites.Activities.AddNewLocationActivity;
import com.approdevelopers.ranchites.Activities.EditUserProfileActivity;
import com.approdevelopers.ranchites.Activities.ImageFullScreen;
import com.approdevelopers.ranchites.Adapters.UserMyProfileTabAdapter;
import com.approdevelopers.ranchites.ApplicationFiles.GlideApp;
import com.approdevelopers.ranchites.Broadcasts.NetworkConnection;
import com.approdevelopers.ranchites.Services.AddImageService;
import com.approdevelopers.ranchites.Utils.ConnectionUtils;
import com.approdevelopers.ranchites.ViewModels.UserProfViewModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity implements NetworkConnection.ReceiverListener, View.OnClickListener {


    //Constants
    private static final int CAMERA_REQUEST_CODE = 3010;
    private static final int STORAGE_REQUEST_CODE = 3015;
    private static final int IMAGE_PICKER_REQUEST_CODE = 6464;
    //permissions array
    private String[] cameraPermissions;
    private String[] storagePermissions;



    TextView txtCurrentUserEmail,txtCurrentUserPhone,txtUserMyProfileProfession;
    AppCompatRatingBar ratingUserMyProfileRating;


    CircleImageView imgUserMyProfilePic;

    String[] tabNames ={"About","Profile Images","My Places/Businesses","Profile Reviews"};
    TabLayout userMyProfileTabLayout;
    ViewPager2 userMyProfileViewPager;
    CollapsingToolbarLayout userMyProfileCollapsingToolbar;

    NetworkConnection networkConnection;
    ConnectionUtils connectionUtils;

    CoordinatorLayout userMyProfParentView;
    String userProfileImageUrl;
    TextView txtEmailVerified;
    TextView txtEmailNotVerified;

    private RewardedInterstitialAd mRewardedAd;
    private Bitmap finalAccountMediaImage;
    private int imageCount;




    UserProfViewModel userProfViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_my_profile_page_modified);

        networkConnection = new NetworkConnection();

        Toolbar toolbar = findViewById(R.id.user_my_profile_toolbar);
        setSupportActionBar(toolbar);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

        //hooks
        imgUserMyProfilePic = findViewById(R.id.img_user_profile_det_profile_pic);
        txtCurrentUserEmail = findViewById(R.id.txt_user_profile_det_email);
        txtCurrentUserPhone = findViewById(R.id.txt_user_profile_det_phone);
        txtUserMyProfileProfession = findViewById(R.id.txt_user_profile_det_profession);
        ratingUserMyProfileRating = findViewById(R.id.rating_user_profile_rating);
        userMyProfParentView = findViewById(R.id.user_my_profile_parent_view);
        txtEmailNotVerified = findViewById(R.id.txt_email_not_verified);
        txtEmailVerified = findViewById(R.id.txt_email_verified);
        userMyProfileCollapsingToolbar = findViewById(R.id.user_profile_collapsing_toolbar_layout);
        userMyProfileCollapsingToolbar.setTitle("");


        userMyProfileTabLayout = findViewById(R.id.user_profile_det_tab_layout);
        userMyProfileViewPager  = findViewById(R.id.user_profile_det_view_pager);




        userMyProfileTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        UserMyProfileTabAdapter userMyProfileTabAdapter = new UserMyProfileTabAdapter(getSupportFragmentManager(),getLifecycle());
        userMyProfileViewPager.setAdapter(userMyProfileTabAdapter);
        userMyProfileViewPager.setOffscreenPageLimit(2);

        //init view model
        userProfViewModel = new ViewModelProvider(this).get(UserProfViewModel.class);
        if (userProfViewModel.isEmailVerified()){
            txtEmailNotVerified.setVisibility(View.GONE);
            txtEmailVerified.setVisibility(View.VISIBLE);
        }else {
            txtEmailNotVerified.setVisibility(View.VISIBLE);
            txtEmailVerified.setVisibility(View.GONE);
        }

        userProfViewModel.getCurrentUserCompleteDataLive().observe(this, userCompleteDetModel -> {
            if (userCompleteDetModel!=null){
                userProfileImageUrl = userCompleteDetModel.getUserProfileImageUrl();

                if (userProfileImageUrl!=null && !userProfileImageUrl.equals("")){
                    GlideApp.with(this).load(userCompleteDetModel.getUserProfileImageUrl())
                            .timeout(20000)
                            .thumbnail(GlideApp.with(this).load(userCompleteDetModel.getUserProfileImageUrl()).override(30))
                            .placeholder(R.drawable.loading_pic_anim)
                            .error( GlideApp.with(this).load(userCompleteDetModel.getUserProfileImageUrl())
                                    .timeout(20000)
                                    .thumbnail(GlideApp.with(this).load(userCompleteDetModel.getUserProfileImageUrl()).override(30))
                                    .placeholder(R.drawable.loading_pic_anim)
                                    .error(R.drawable.ic_image_not_found).into(imgUserMyProfilePic))
                            .into(imgUserMyProfilePic);

                }else {
                    GlideApp.with(this).load(R.drawable.ic_person).into(imgUserMyProfilePic);
                }


                txtCurrentUserEmail.setText(userCompleteDetModel.getUserEmail());
                txtCurrentUserPhone.setText(userCompleteDetModel.getUserPhone());
                if (userCompleteDetModel.getUserProfession()==null){
                    txtUserMyProfileProfession.setText(getString(R.string.no_profession_added));
                }else {
                    txtUserMyProfileProfession.setText(userCompleteDetModel.getUserProfession());

                }
                ratingUserMyProfileRating.setRating((float) userCompleteDetModel.getUserRating());
                userMyProfileCollapsingToolbar.setTitle(userCompleteDetModel.getUserName());
            }else {
                userProfileImageUrl=null;
            }
        });

        userProfViewModel.getCurrentProfImages().observe(this, imagesModels -> {
            if (imagesModels!=null && !imagesModels.isEmpty()){
                imageCount = imagesModels.size();
            }
            else {
                imageCount = 0;
            }
        });

        new TabLayoutMediator(userMyProfileTabLayout,userMyProfileViewPager,(tab, position) -> tab.setText(tabNames[position])).attach();
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        imgUserMyProfilePic.setOnClickListener(this);
        txtEmailNotVerified.setOnClickListener(this);



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
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user_my_profile,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (item.getItemId()==R.id.menu_my_profile_edit){
            Intent intent = new Intent(UserProfile.this,EditUserProfileActivity.class);
            startActivity(intent);
        }

        if (item.getItemId()==R.id.menu_my_profile_add_image){
            if (imageCount%4==0){

                Toast.makeText(UserProfile.this, "To add more images,\nWatch upcoming ad fully.", Toast.LENGTH_SHORT).show();

                String rewarded_ad_unit = getString(R.string.ranchites_rewarded_profile_image_ad_unit);

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
        if (item.getItemId()==R.id.menu_my_profile_add_place){

            Intent intent = new Intent(UserProfile.this, AddNewLocationActivity.class);
            intent.putExtra("editLocation",false);
            intent.putExtra("addLocation",true);
            intent.putExtra("locationDocId", (String) null);
            startActivity(intent);
        }
        if (item.getItemId()==R.id.menu_my_profile_sign_out){
            showSignOutAlertDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSignOutAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Sign out")
                .setMessage("Are you sure you want to sign out ?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // Continue with delete operation
                    Toast.makeText(UserProfile.this, "Signing out", Toast.LENGTH_SHORT).show();
                    userProfViewModel.signOutUser();
                    Intent intent = new Intent(UserProfile.this,LoginActivity.class);
                    finishAffinity();
                    startActivity(intent);

                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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



    private void initAddImageService(Bitmap accountMediaImage) {

        if (isNetworkAvailable()){

            Intent imageIntent = new Intent(this, AddImageService.class);

            imageIntent.putExtra("documentId",userProfViewModel.getUserId());

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
                ContextCompat.startForegroundService(UserProfile.this, imageIntent);
                Toast.makeText(this, "Uploading \n Check notifications for more.", Toast.LENGTH_SHORT).show();

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
            if (AddImageService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectionUtils = new ConnectionUtils(UserProfile.this,networkConnection,userMyProfParentView);
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
    public void onNetworkChange(boolean isConnected) {
        connectionUtils.showSnackBar(isConnected);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.img_user_profile_det_profile_pic){

            ArrayList<String> imageUrlList = new ArrayList<>();
            imageUrlList.add(userProfileImageUrl);
            Intent intent = new Intent(UserProfile.this, ImageFullScreen.class);
            intent.putStringArrayListExtra("imageUrls", imageUrlList);
            startActivity(intent);
        }
        
        if (view.getId()==R.id.txt_email_not_verified){
            Task<Void> verifyEmailTask =userProfViewModel.sendEmailVerificationLink();
            verifyEmailTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(UserProfile.this, "Verification link sent to your Email Id", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(UserProfile.this, "Failed to send verification link\nTry again later", Toast.LENGTH_SHORT).show();
                }
            });
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

                    Bitmap accountMediaImage = null;
                    try {
                        accountMediaImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (accountMediaImage != null) {
                        if (mRewardedAd != null) {

                             finalAccountMediaImage= accountMediaImage;
                            mRewardedAd.show(this, rewardItem -> {
                                // Handle the reward.
                                initAddImageService(finalAccountMediaImage);

                            });
                        } else {
                            initAddImageService(accountMediaImage);
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
            mRewardedAd = null;
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