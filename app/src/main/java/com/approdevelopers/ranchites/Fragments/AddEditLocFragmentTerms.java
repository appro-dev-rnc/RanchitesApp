package com.approdevelopers.ranchites.Fragments;

import static android.content.Context.ACTIVITY_SERVICE;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Services.AddLocationForegroundService;
import com.approdevelopers.ranchites.ViewModels.AddNewLocViewModel;
import com.approdevelopers.ranchites.ViewModels.AddNewLocViewModelFactory;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.GeoPoint;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEditLocFragmentTerms#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEditLocFragmentTerms extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FloatingActionButton btnPreviousFragTermsDet;
    private ExtendedFloatingActionButton btnAddFragTerms,btnUpdateFragterms;
    ViewPager2 addEditLocViewPager;
    private String locationDocId;
    private AddNewLocViewModel viewModel;
    private CheckBox checkBoxTerms;

    private boolean addLocationPhase,editLocationPhase;

    private RewardedInterstitialAd mRewardedAd;


    public AddEditLocFragmentTerms() {
        // Required empty public constructor
    }public AddEditLocFragmentTerms(String locationDocId) {
        // Required empty public constructor
        this.locationDocId = locationDocId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddEditLocFragmentTerms.
     */
    // TODO: Rename and change types and number of parameters
    public static AddEditLocFragmentTerms newInstance(String param1, String param2) {
        AddEditLocFragmentTerms fragment = new AddEditLocFragmentTerms();
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
        addEditLocViewPager = requireActivity().findViewById(R.id.add_edit_view_pager);
        viewModel = new ViewModelProvider(requireActivity(), new AddNewLocViewModelFactory(locationDocId)).get(AddNewLocViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_edit_loc_terms, container, false);
        btnPreviousFragTermsDet = view.findViewById(R.id.btn_previous_frag_terms_det);
        btnUpdateFragterms  = view.findViewById(R.id.btn_update_frag_terms_det);
        btnAddFragTerms = view.findViewById(R.id.btn_add_frag_terms_det);
        checkBoxTerms = view.findViewById(R.id.checkbox_terms);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (locationDocId==null){
            btnAddFragTerms.setVisibility(View.VISIBLE);
            btnUpdateFragterms.setVisibility(View.GONE);
            addLocationPhase=true;
            editLocationPhase=false;

        }
        if (locationDocId!=null){
            btnUpdateFragterms.setVisibility(View.VISIBLE);
            btnAddFragTerms.setVisibility(View.GONE);
            editLocationPhase = true;
            addLocationPhase= false;

        }
        btnPreviousFragTermsDet.setOnClickListener(this);
        btnAddFragTerms.setOnClickListener(this);
        btnUpdateFragterms.setOnClickListener(this);
        checkBoxTerms.setOnClickListener(this);



        String rewarded_ad_unit = getString(R.string.ranchites_rewarded__add_edit_place_ad_unit);

        RewardedInterstitialAd.load(requireActivity(),rewarded_ad_unit,
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

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btn_previous_frag_terms_det){
            addEditLocViewPager.setCurrentItem(2);
        }

        if (view.getId()==R.id.btn_update_frag_terms_det){
            if (checkBoxTerms.isChecked()){
                if (isNetworkAvailable()){
                    if (mRewardedAd != null) {

                        mRewardedAd.show(requireActivity(), rewardItem -> {
                        });
                    } else {
                        sendUpdateLocData();
                    }

                }else {
                    Toast.makeText(requireActivity(), "Turn on Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }else {
                checkBoxTerms.setError("Agree to the terms and conditions.");
                checkBoxTerms.requestFocus();
            }

            
        }
        if (view.getId()==R.id.btn_add_frag_terms_det){
           if (checkBoxTerms.isChecked()){
               if (isNetworkAvailable()){
                   if (mRewardedAd != null) {

                       mRewardedAd.show(requireActivity(), rewardItem -> {

                       });
                   } else {
                       sendAddNewLocData();
                   }
               }else {
                   Toast.makeText(requireActivity(), "Turn on Internet Connection", Toast.LENGTH_SHORT).show();

               }

           }else {
               checkBoxTerms.setError("Agree to the terms and conditions.");
               checkBoxTerms.requestFocus();
           }
        }
        if (view.getId()==R.id.checkbox_terms){
            checkBoxTerms.setError(null);
        }
    }

    private void sendAddNewLocData() {
        if (locationDocId==null){
            String title = viewModel.getTitle();
            String desc = viewModel.getDesc();
            String address = viewModel.getAddress();
            String category = viewModel.getCategory();
            boolean locationShareEnabled = viewModel.getLocationShareEnabled();
            boolean editLocation = false;
            boolean addLocation = true;
            String ownerId = viewModel.getOwnerId();
            Bitmap bitmap = viewModel.getLocBannerBitmap().getValue();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
            }

            GeoPoint geoPoint = viewModel.getMarkedLocation();
            double latitude;
            double longitude;
            if (geoPoint==null){
                latitude = 0.0;
                longitude=0.0;
            }else {
                latitude = geoPoint.getLatitude();
                longitude = geoPoint.getLongitude();
            }

            List<String> keywords = viewModel.generateKeywords();

            Intent serviceIntent = new Intent(requireActivity(), AddLocationForegroundService.class);
            serviceIntent.putExtra("title",title);
            serviceIntent.putExtra("desc",desc);
            serviceIntent.putExtra("address",address);
            serviceIntent.putExtra("category",category);
            serviceIntent.putExtra("locationShareEnabled",locationShareEnabled);
            serviceIntent.putExtra("editLocation",editLocation);
            serviceIntent.putExtra("addLocation",addLocation);
            serviceIntent.putExtra("locationLatitude",latitude);
            serviceIntent.putExtra("locationLongitude",longitude);
            serviceIntent.putExtra("ownerId",ownerId);
            serviceIntent.putStringArrayListExtra("keywords", (ArrayList<String>) keywords);

            if (bitmap!=null){
                serviceIntent.putExtra("bitmapNull",false);
                serviceIntent.putExtra("bitmapArray",stream.toByteArray());
            }else {
                serviceIntent.putExtra("bitmapNull",true);
            }

            if (isMyServiceRunning()){
                Toast.makeText(requireActivity(), "Please wait, till the previous task gets finished", Toast.LENGTH_SHORT).show();
            }else {
                ContextCompat.startForegroundService(requireContext(), serviceIntent);
                Toast.makeText(requireActivity(), "Uploading \n Check notifications for more.", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
        }
    }



    private void sendUpdateLocData(){
        if (locationDocId!=null){
            String documentId = viewModel.getDocumentId();
            String title = viewModel.getTitle();
            String desc = viewModel.getDesc();
            String address = viewModel.getAddress();
            String category = viewModel.getCategory();
            String imageUrl = viewModel.getImageUrl();
            boolean locationShareEnabled = viewModel.getLocationShareEnabled();
            boolean editLocation = true;
            boolean addLocation = false;

            Bitmap bitmap = viewModel.getLocBannerBitmap().getValue();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
            }

            GeoPoint geoPoint = viewModel.getMarkedLocation();
            double latitude;
            double longitude;
            if (geoPoint==null){
                latitude = 0.0;
                longitude=0.0;
            }else {
                latitude = geoPoint.getLatitude();
                longitude = geoPoint.getLongitude();
            }

            List<String> keywords = viewModel.generateKeywords();


            Intent serviceIntent = new Intent(requireActivity(), AddLocationForegroundService.class);
            serviceIntent.putExtra("documentId",documentId);
            serviceIntent.putExtra("title",title);
            serviceIntent.putExtra("desc",desc);
            serviceIntent.putExtra("address",address);
            serviceIntent.putExtra("category",category);
            serviceIntent.putExtra("imageUrl",imageUrl);
            serviceIntent.putExtra("locationShareEnabled",locationShareEnabled);
            serviceIntent.putExtra("editLocation",editLocation);
            serviceIntent.putExtra("addLocation",addLocation);
            serviceIntent.putExtra("locationLatitude",latitude);
            serviceIntent.putExtra("locationLongitude",longitude);
            serviceIntent.putStringArrayListExtra("keywords", (ArrayList<String>) keywords);


            if (bitmap!=null){
                serviceIntent.putExtra("bitmapNull",false);
                serviceIntent.putExtra("bitmapArray",stream.toByteArray());
            }else {
                serviceIntent.putExtra("bitmapNull",true);
            }
            if (isMyServiceRunning()){
                Toast.makeText(requireActivity(), "Please wait , till the previous task gets finished", Toast.LENGTH_SHORT).show();
            }else {
                ContextCompat.startForegroundService(requireContext(), serviceIntent);
                Toast.makeText(requireActivity(), "Updating \n Check notifications for more.", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
           
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) requireActivity().getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (AddLocationForegroundService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    FullScreenContentCallback rewardedCallback = new FullScreenContentCallback() {
        @Override
        public void onAdClicked() {
            super.onAdClicked();

        }

        @Override
        public void onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent();
            if (addLocationPhase && !editLocationPhase){
                sendAddNewLocData();
            }
            if (editLocationPhase && !addLocationPhase){
                sendUpdateLocData();
            }
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