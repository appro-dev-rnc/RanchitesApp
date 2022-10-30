package com.approdevelopers.ranchites.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.ViewModels.LocationDetailPageViewModel;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationYourReview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationYourReview extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LocationDetailPageViewModel viewModel;
    AppCompatRatingBar ratingAddYourRating;
    TextInputLayout textLayoutAddReview;
    ImageButton imgBtnEditReviewComDet,imgBtnDeleteReviewComDet;
    Button btnAddReview,btnUpdateReview;
    String locationId;
    TextView txtLocDetYourReviewTag;

    private InterstitialAd mInterstitialAd;



    public LocationYourReview() {
        // Required empty public constructor
    }public LocationYourReview(String locationId) {
        // Required empty public constructor
        this.locationId = locationId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationYourReview.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationYourReview newInstance(String param1, String param2) {
        LocationYourReview fragment = new LocationYourReview();
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
        viewModel= new ViewModelProvider(requireActivity()).get(LocationDetailPageViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_your_review, container, false);


        imgBtnEditReviewComDet = view.findViewById(R.id.img_edit_my_review_com_det);
        imgBtnDeleteReviewComDet = view.findViewById(R.id.img_delete_my_review_com_det);
        btnUpdateReview = view.findViewById(R.id.btn_update_review);

        ratingAddYourRating = view.findViewById(R.id.rating_add_your_rating);
        textLayoutAddReview = view.findViewById(R.id.textLayoutAddReview);
        btnAddReview = view.findViewById(R.id.btn_add_review);
        txtLocDetYourReviewTag = view.findViewById(R.id.txt_loc_det_your_review_tag);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        viewModel.getSelectedLocCurrentUserLive(locationId).observe(requireActivity(), reviewModel -> {

            if (reviewModel!=null){
                Objects.requireNonNull(textLayoutAddReview.getEditText()).setEnabled(false);
                ratingAddYourRating.setIsIndicator(true);
                ratingAddYourRating.setRating((float) reviewModel.getRating());
                textLayoutAddReview.getEditText().setText(reviewModel.getReview());
                btnAddReview.setVisibility(View.GONE);
                imgBtnDeleteReviewComDet.setVisibility(View.VISIBLE);
                imgBtnEditReviewComDet.setVisibility(View.VISIBLE);
                btnUpdateReview.setVisibility(View.GONE);
                String updateReview = "Update your review";
                txtLocDetYourReviewTag.setText(updateReview);

            }
            if (reviewModel==null){

                Objects.requireNonNull(textLayoutAddReview.getEditText()).setEnabled(true);
                ratingAddYourRating.setIsIndicator(false);
                btnAddReview.setVisibility(View.VISIBLE);
                imgBtnDeleteReviewComDet.setVisibility(View.GONE);
                imgBtnEditReviewComDet.setVisibility(View.GONE);
                btnUpdateReview.setVisibility(View.GONE);
                ratingAddYourRating.setRating(0);
                textLayoutAddReview.getEditText().setText(null);
                String addReview = "Add your review";
                txtLocDetYourReviewTag.setText(addReview);
            }
        });

        btnAddReview.setOnClickListener(this);
        imgBtnEditReviewComDet.setOnClickListener(this);
        imgBtnDeleteReviewComDet.setOnClickListener(this);
        btnUpdateReview.setOnClickListener(this);
        Objects.requireNonNull(textLayoutAddReview.getEditText()).addTextChangedListener(reviewTextWatcher);

        AdRequest adRequest = new AdRequest.Builder().build();
        String interstitial_ad_unit = getString(R.string.ranchites_interstitial_add_update_reviews_ad_unit);
        InterstitialAd.load(requireActivity(),interstitial_ad_unit, adRequest,
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
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.btn_add_review){
            validateReviewAndUpload();
        }
        if (view.getId() == R.id.img_edit_my_review_com_det){
            Objects.requireNonNull(textLayoutAddReview.getEditText()).setEnabled(true);
            ratingAddYourRating.setIsIndicator(false);
            btnUpdateReview.setVisibility(View.VISIBLE);
        }
        if (view.getId() == R.id.img_delete_my_review_com_det){

            new AlertDialog.Builder(requireActivity())
                    .setTitle("Delete review")
                    .setMessage("Are you sure you want to delete your review?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        // Continue with delete operation
                        viewModel.deleteReviewFromFirestore(locationId);
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        if (view.getId()==R.id.btn_update_review){
            validateReviewAndUpload();
        }

    }

    private void validateReviewAndUpload() {
        String review = null;
        if (textLayoutAddReview.getEditText()!=null){
            review  = textLayoutAddReview.getEditText().getText().toString().trim();
        }

        double rating = ratingAddYourRating.getRating();
        if (review==null || review.isEmpty()){
            textLayoutAddReview.setError("Review cannot be empty");
            return;
        }if (rating==0){
            Toast.makeText(requireActivity(), "Rating cannot be 0", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mInterstitialAd != null) {
            mInterstitialAd.show(requireActivity());
            mInterstitialAd.setFullScreenContentCallback(contentCallback);
        } else {
            viewModel.uploadReviewToFirestore(locationId,review,rating);
        }


    }


    TextWatcher reviewTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            textLayoutAddReview.setError(null);
            textLayoutAddReview.setErrorEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    FullScreenContentCallback contentCallback = new FullScreenContentCallback() {
        @Override
        public void onAdClicked() {
            super.onAdClicked();
        }

        @Override
        public void onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent();
            mInterstitialAd = null;
            String review = null;
            if (textLayoutAddReview.getEditText()!=null){
                review  = textLayoutAddReview.getEditText().getText().toString().trim();
            }

            double rating = ratingAddYourRating.getRating();
            viewModel.uploadReviewToFirestore(locationId,review,rating);


        }

        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
            super.onAdFailedToShowFullScreenContent(adError);
            mInterstitialAd = null;
            String review = null;
            if (textLayoutAddReview.getEditText()!=null){
                review  = textLayoutAddReview.getEditText().getText().toString().trim();
            }

            double rating = ratingAddYourRating.getRating();
            viewModel.uploadReviewToFirestore(locationId,review,rating);

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