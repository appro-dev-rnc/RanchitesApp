package com.approdevelopers.ranchites.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.approdevelopers.ranchites.ApplicationFiles.GlideApp;
import com.approdevelopers.ranchites.Broadcasts.NetworkConnection;
import com.approdevelopers.ranchites.Models.User;
import com.approdevelopers.ranchites.Models.UserUpdateModel;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Services.UpdateMyProfileService;
import com.approdevelopers.ranchites.Utils.ConnectionUtils;
import com.approdevelopers.ranchites.ViewModels.EditUserProfViewModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditUserProfileActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener, NetworkConnection.ReceiverListener {

    //Constants
    private static final int CAMERA_REQUEST_CODE = 2010;
    private static final int STORAGE_REQUEST_CODE = 2015;
    private static final int IMAGE_PICKER_REQUEST_CODE = 652;

    EditUserProfViewModel editUserProfViewModel;

    //permissions array
    private String[] cameraPermissions;
    private String[] storagePermissions;

    // UI elements
    ImageView imgEditUserProfPic;
    TextInputLayout textLayoutEditUserName, textLayoutEditUserProfession, textLayoutEditUserAbout;
    SwitchCompat searchByProfessionSwitch;
    AppCompatAutoCompleteTextView autoTextEditUserProfession;

    AppBarLayout editMyProfileAppBarLayout;

    NetworkConnection networkConnection;
    ConnectionUtils connectionUtils;
    CoordinatorLayout editUserParentView;

    ArrayAdapter<String> categoriesAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_my_profile_page_design_modified);
        networkConnection = new NetworkConnection();

        Toolbar toolbar = findViewById(R.id.edit_my_profile_toolbar);
        setSupportActionBar(toolbar);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

        //hooks
        imgEditUserProfPic = findViewById(R.id.imgEditUserProfPic);
        textLayoutEditUserName = findViewById(R.id.textLayoutEditUserName);
        textLayoutEditUserProfession = findViewById(R.id.textLayoutEditUserProfession);
        textLayoutEditUserAbout = findViewById(R.id.textLayoutEditUserAbout);
        searchByProfessionSwitch = findViewById(R.id.searchByProfessionSwitch);
        autoTextEditUserProfession = findViewById(R.id.autoTextEditUserProfession);
        editMyProfileAppBarLayout = findViewById(R.id.edit_my_profile_app_barlayout);
        FloatingActionButton imgEditProfileFloatBtn = findViewById(R.id.img_float_edit_user_add_profile_pic);

        editUserParentView = findViewById(R.id.edit_user_parent_view);


        editUserProfViewModel = new ViewModelProvider(this).get(EditUserProfViewModel.class);

        editUserProfViewModel.getCurrentUserProfLiveData().observe(this, user -> {
            if (user != null) {
                populateUserDataIntoUI(user);
            }
        });

        editUserProfViewModel.getUserProfImgBitmap().observe(this, bitmap -> {
            if (bitmap != null) {
                imgEditUserProfPic.invalidate();
                imgEditUserProfPic.setImageBitmap(bitmap);
            }
        });



        editUserProfViewModel.getProfessionNameList().observe(this, strings -> {
            if (strings!=null){
                categoriesAdapter = new ArrayAdapter<>(EditUserProfileActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, strings);
                autoTextEditUserProfession.setAdapter(categoriesAdapter);
            }
        });


        //setting onTextChange Listener on UI elements
        Objects.requireNonNull(textLayoutEditUserName.getEditText()).addTextChangedListener(myWatcher);
        Objects.requireNonNull(textLayoutEditUserAbout.getEditText()).addTextChangedListener(myWatcher);
        searchByProfessionSwitch.setOnCheckedChangeListener(this);
        autoTextEditUserProfession.addTextChangedListener(myWatcher);

        imgEditProfileFloatBtn.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        autoTextEditUserProfession.setOnItemClickListener(this);


    }

    private void populateUserDataIntoUI(User user) {
        if (editUserProfViewModel.getUserProfImgBitmap().getValue() == null) {


            GlideApp.with(EditUserProfileActivity.this).load(user.getUserProfileImageUrl())
                    .timeout(20000)
                    .thumbnail(GlideApp.with(this).load(user.getUserProfileImageUrl()).override(30))
                    .placeholder(R.drawable.loading_pic_anim)
                    .error(
                            GlideApp.with(EditUserProfileActivity.this).load(user.getUserProfileImageUrl())
                                    .timeout(20000)
                                    .thumbnail(GlideApp.with(this).load(user.getUserProfileImageUrl()).override(30))
                                    .placeholder(R.drawable.loading_pic_anim)
                                    .into(imgEditUserProfPic))
                    .into(imgEditUserProfPic);

        }
        Objects.requireNonNull(textLayoutEditUserName.getEditText()).setText(user.getUserName());
        searchByProfessionSwitch.setChecked(user.getSearchByProfessionEnabled());
        autoTextEditUserProfession.setText(user.getUserProfession());
        Objects.requireNonNull(textLayoutEditUserAbout.getEditText()).setText(user.getUserAbout());

        //setting view model values
        editUserProfViewModel.setUserName(user.getUserName());
        editUserProfViewModel.setUserProfession(user.getUserProfession());
        editUserProfViewModel.setUserAbout(user.getUserAbout());
        editUserProfViewModel.setSearchByProfessionEnabled(user.getSearchByProfessionEnabled());
        editUserProfViewModel.getUserProfImgUrl().postValue(user.getUserProfileImageUrl());


    }

    @Override
    protected void onResume() {
        super.onResume();
        connectionUtils = new ConnectionUtils(EditUserProfileActivity.this, networkConnection, editUserParentView);
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
            if (v instanceof TextInputEditText || v instanceof AppCompatAutoCompleteTextView) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    //on click listener implementation
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.img_float_edit_user_add_profile_pic) {
            showImagePickerDialog();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_my_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_edit_my_profile_save) {
            if (validateUserInputs()) {
                uploadUserProfiledata();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == R.id.searchByProfessionSwitch) {
            textLayoutEditUserProfession.setErrorEnabled(false);
            editUserProfViewModel.setSearchByProfessionEnabled(b);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String profession = (String) adapterView.getItemAtPosition(i);
        editUserProfViewModel.setUserProfession(profession);

    }

    TextWatcher myWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (Objects.requireNonNull(textLayoutEditUserName.getEditText()).getText() == charSequence) {
                textLayoutEditUserName.setErrorEnabled(false);
                editUserProfViewModel.setUserName(charSequence.toString());
            } else if (Objects.requireNonNull(textLayoutEditUserAbout.getEditText()).getText() == charSequence) {
                textLayoutEditUserAbout.setErrorEnabled(false);
                editUserProfViewModel.setUserAbout(charSequence.toString());
            } else if (autoTextEditUserProfession.getText() == charSequence) {
                textLayoutEditUserProfession.setErrorEnabled(false);
                editUserProfViewModel.setUserProfession(null);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode== CAMERA_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                pickCropImageFromCamera();
            }else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode== STORAGE_REQUEST_CODE){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                pickCropImageFromGallery();
            }else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void pickCropImageFromCamera(){
        ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .cropSquare()//Crop image(Optional), Check Customization for more option
                .compress(512)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start(IMAGE_PICKER_REQUEST_CODE);
    }

    private void pickCropImageFromGallery(){
        ImagePicker.with(this)
                .galleryOnly()
                .crop(1f,1f)//Crop image(Optional), Check Customization for more option
                .compress(512)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start(IMAGE_PICKER_REQUEST_CODE);
    }




    private boolean validateUserInputs() {
        if (!editUserProfViewModel.validateUserName()) {
            textLayoutEditUserName.setError("Name cannot be empty");
            return false;
        }
        if (!editUserProfViewModel.validateProfession()) {
            textLayoutEditUserProfession.setError("Profession cannot be empty if sharing is enabled");
            return false;
        }
        if (!editUserProfViewModel.validateUserAbout()) {
            textLayoutEditUserAbout.setError("About section cannot be empty");
            return false;
        }
        return true;
    }

    private void uploadUserProfiledata() {
        if (isNetworkAvailable()) {
            Intent intent = new Intent(this, UpdateMyProfileService.class);
            Bitmap profBitmap = editUserProfViewModel.getUserProfImgBitmap().getValue();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if (profBitmap != null) {
                profBitmap.compress(Bitmap.CompressFormat.JPEG, 25, stream);
            }
            if (profBitmap != null) {
                intent.putExtra("bitmapNull", false);
                intent.putExtra("bitmapArray", stream.toByteArray());
            } else {
                intent.putExtra("bitmapNull", true);
            }

            UserUpdateModel model = editUserProfViewModel.getUserCreated();
            intent.putExtra("userId", model.getuId());
            intent.putExtra("userName", model.getUserName());
            intent.putExtra("searchByProfessionEnabled", model.getSearchByProfessionEnabled());
            intent.putExtra("userProfession", model.getUserProfession());
            intent.putExtra("userAbout", model.getUserAbout());
            intent.putExtra("userProfileImageUrl", model.getUserProfileImageUrl());

            List<String> keywords = editUserProfViewModel.generateKeywords();

            intent.putStringArrayListExtra("keywords", (ArrayList<String>) keywords);





            if (isMyServiceRunning()) {
                Toast.makeText(this, "Please wait, till the previous task gets finished", Toast.LENGTH_SHORT).show();
            } else {
                ContextCompat.startForegroundService(EditUserProfileActivity.this, intent);
                Toast.makeText(this, "Updating \n Check notifications for more.", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Turn on Internet Connection", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onNetworkChange(boolean isConnected) {
        connectionUtils.showSnackBar(isConnected);
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
            if (UpdateMyProfileService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER_REQUEST_CODE){
            if (resultCode==RESULT_OK){
                Uri uri;
                if (data != null) {
                    uri = data.getData();

                    Bitmap profImgBitmap = null;
                    try {
                        profImgBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (profImgBitmap != null) {
                        editUserProfViewModel.getUserProfImgBitmap().postValue(profImgBitmap);
                    }
                }
            }

        }
    }
}