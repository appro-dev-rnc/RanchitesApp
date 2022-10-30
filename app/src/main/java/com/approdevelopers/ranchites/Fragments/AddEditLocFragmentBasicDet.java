package com.approdevelopers.ranchites.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.approdevelopers.ranchites.ApplicationFiles.GlideApp;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.ViewModels.AddNewLocViewModel;
import com.approdevelopers.ranchites.ViewModels.AddNewLocViewModelFactory;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEditLocFragmentBasicDet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEditLocFragmentBasicDet extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //Constants
    private static final int CAMERA_REQUEST_CODE = 1010;
    private static final int STORAGE_REQUEST_CODE = 1015;
    private static final int IMAGE_PICKER_REQUEST_CODE = 7748;
    //permissions array
    private String[] cameraPermissions;
    private String[] storagePermissions;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FloatingActionButton btnNextFragBasicDet;
    private ViewPager2 addEditParentViewPager;
    TextInputLayout edtAddLocTitle,  edtAddLocAddress;
    AddNewLocViewModel viewModel;
    private String locationDocId;
    ImageView imgLocBanner;
    FloatingActionButton imgBtnAddEditLocBanner;



    public AddEditLocFragmentBasicDet() {
        // Required empty public constructor
    }public AddEditLocFragmentBasicDet(String locationDocId) {
        // Required empty public constructor
        this.locationDocId =locationDocId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddEditLocFragmentBasicDet.
     */
    // TODO: Rename and change types and number of parameters
    public static AddEditLocFragmentBasicDet newInstance(String param1, String param2) {
        AddEditLocFragmentBasicDet fragment = new AddEditLocFragmentBasicDet();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        addEditParentViewPager = requireActivity().findViewById(R.id.add_edit_view_pager);
        viewModel = new ViewModelProvider(requireActivity(),new AddNewLocViewModelFactory(locationDocId)).get(AddNewLocViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_edit_loc_basic_det, container, false);
        btnNextFragBasicDet = view.findViewById(R.id.btn_next_frag_basic_det);
        edtAddLocTitle = view.findViewById(R.id.textLayoutAddTitle);
        edtAddLocAddress = view.findViewById(R.id.textLayoutAddAddress);
        imgLocBanner = view.findViewById(R.id.img_add_loc_banner);
        imgBtnAddEditLocBanner = view.findViewById(R.id.img_float_edit_loc_add_image_btn);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        viewModel.getEditLocationModel().observe(requireActivity(), locationModel -> {
            if (locationModel!= null){

                GlideApp.with(requireActivity()).load(locationModel.getImageUrl())
                        .thumbnail(GlideApp.with(requireActivity()).load(locationModel.getImageUrl()).override(30))
                        .timeout(10000)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontTransform()
                        .error( GlideApp.with(requireActivity()).load(locationModel.getImageUrl())
                                .thumbnail(GlideApp.with(requireActivity()).load(locationModel.getImageUrl()).override(30))
                                .timeout(10000)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .dontTransform().into(imgLocBanner))
                        .into(imgLocBanner);
                Objects.requireNonNull(edtAddLocTitle.getEditText()).setText(locationModel.getTitle());

                Objects.requireNonNull(edtAddLocAddress.getEditText()).setText(locationModel.getAddress());

                viewModel.setImageUrl(locationModel.getImageUrl());

            }
            if (locationModel==null){

                imgLocBanner.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.business_banner_image,requireActivity().getTheme()));
                Objects.requireNonNull(edtAddLocTitle.getEditText()).setText("");
                Objects.requireNonNull(edtAddLocAddress.getEditText()).setText("");
                viewModel.setImageUrl(null);
                viewModel.getLocBannerBitmap().postValue(null);
                viewModel.setTitle(null);
                viewModel.setAddress(null);

            }
        });


        viewModel.getLocBannerBitmap().observe(requireActivity(), bitmap -> {
            if (bitmap != null)
                imgLocBanner.setImageBitmap(bitmap);
        });
        //onTextChanged Listener on edit texts
        Objects.requireNonNull(edtAddLocTitle.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //nothing to do here
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtAddLocTitle.setErrorEnabled(false);
                viewModel.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Objects.requireNonNull(edtAddLocAddress.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //nothing to do here
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtAddLocAddress.setErrorEnabled(false);
                viewModel.setAddress(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnNextFragBasicDet.setOnClickListener(this);
        imgBtnAddEditLocBanner.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btn_next_frag_basic_det){
            if (validateBasicData()){
                addEditParentViewPager.setCurrentItem(1);
            }

        }
        if (view.getId()==R.id.img_float_edit_loc_add_image_btn){
            showImagePickerDialog();
        }
    }

    private boolean validateBasicData() {
        if (!viewModel.validateImageBitmap()) {
            if (!viewModel.validateImageUrl()){
                Toast.makeText(requireActivity(), "No image selected \n Try Again ...", Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        if (!viewModel.validateTitle()) {
            edtAddLocTitle.setError("Title cannot be empty");
            return false;
        }
        if (!viewModel.validateAddress()) {
            edtAddLocAddress.setError("Address cannot be empty");
            return false;
        }
        return true;
    }

    private void showImagePickerDialog() {
        //options (camera,gallery) to show in dialog
        String[] options = {"Camera", "Gallery"};

        //Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
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
        boolean result_camera = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result_write_external = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        if (result_camera && result_write_external) {
           // pickImageFromCamera();
            pickCropImageFromCamera();
        } else {
            //request permission at run time
            ActivityCompat.requestPermissions(requireActivity(), cameraPermissions, CAMERA_REQUEST_CODE);
        }
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
           // pickImageFromGallery();
            pickCropImageFromGallery();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), storagePermissions, STORAGE_REQUEST_CODE);
        }
    }


    private void pickCropImageFromCamera(){
        ImagePicker.with(this)
                .cameraOnly()
                .crop(16f,10f)//Crop image(Optional), Check Customization for more option
                .compress(512)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start(IMAGE_PICKER_REQUEST_CODE);
    }

    private void pickCropImageFromGallery(){
               ImagePicker.with(this)
                .galleryOnly()
                .crop(16f,10f)//Crop image(Optional), Check Customization for more option
                .compress(512)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start(IMAGE_PICKER_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   // pickImageFromCamera();
                    pickCropImageFromCamera();
                } else {
                    Toast.makeText(requireActivity(), "Camera Permission Denied \n Failed To Continue...", Toast.LENGTH_SHORT).show();
                }
                break;
            case STORAGE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                  //  pickImageFromGallery();
                    pickCropImageFromGallery();
                } else {
                    Toast.makeText(requireActivity(), "Storage Permission Denied \n Failed To Continue...", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMAGE_PICKER_REQUEST_CODE){
            if (resultCode== Activity.RESULT_OK){
                Uri uri;
                if (data != null) {
                    uri = data.getData();

                    Bitmap locImgBitmap = null;
                    try {
                        locImgBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (locImgBitmap != null) {
                        viewModel.getLocBannerBitmap().postValue(locImgBitmap);
                        viewModel.setImageUrl(null);
                    }
                }
            }
        }
    }
}