package com.approdevelopers.ranchites.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.ViewModels.UserComDetViewModel;
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
 * Use the {@link UserYourReviews#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserYourReviews extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String userId;
    UserComDetViewModel viewModel;

    AppCompatRatingBar ratingAddUserRating;

    TextInputLayout textLayoutUserReview;
    Button btnAddUserReview;
    Button btnUpdateUserReview;
    ImageButton imgBtnEditUserReviewComDet,imgBtnDeleteUserReviewComDet;
    TextView txtUserDetYourReviewTag;

    private InterstitialAd mInterstitialAd;


    public UserYourReviews() {
        // Required empty public constructor
    }public UserYourReviews(String userId) {
        // Required empty public constructor
        this.userId = userId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserYourReviews.
     */
    // TODO: Rename and change types and number of parameters
    public static UserYourReviews newInstance(String param1, String param2) {
        UserYourReviews fragment = new UserYourReviews();
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

        viewModel = new ViewModelProvider(requireActivity()).get(UserComDetViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_your_reviews, container, false);
        ratingAddUserRating = view.findViewById(R.id.rating_add_your_user_rating);
        textLayoutUserReview = view.findViewById(R.id.textLayoutAdd_user_Review);
        btnAddUserReview = view.findViewById(R.id.btn_add_user_review);
        btnUpdateUserReview = view.findViewById(R.id.btn_update_user_review);
        imgBtnEditUserReviewComDet = view.findViewById(R.id.img_edit_my_review_user_com_det);
        imgBtnDeleteUserReviewComDet = view.findViewById(R.id.img_delete_my_review_user_com_det);
        txtUserDetYourReviewTag = view.findViewById(R.id.txt_user_det_add_review_tag);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getSelectedUserCurrentReview(userId).observe(requireActivity(), userReviewModel -> {
            if (userReviewModel!=null){
                Objects.requireNonNull(textLayoutUserReview.getEditText()).setEnabled(false);

                textLayoutUserReview.getEditText().setText(userReviewModel.getReview());
                ratingAddUserRating.setIsIndicator(true);
                ratingAddUserRating.setRating((float) userReviewModel.getRating());
                btnAddUserReview.setVisibility(View.GONE);
                btnUpdateUserReview.setVisibility(View.GONE);
                imgBtnDeleteUserReviewComDet.setVisibility(View.VISIBLE);
                imgBtnEditUserReviewComDet.setVisibility(View.VISIBLE);
                String updateReview = "Update your review";
                txtUserDetYourReviewTag.setText(updateReview);
            }if (userReviewModel==null){
                Objects.requireNonNull(textLayoutUserReview.getEditText()).setEnabled(true);
                ratingAddUserRating.setIsIndicator(false);
                ratingAddUserRating.setRating(0);
                textLayoutUserReview.getEditText().setText(null);
                btnAddUserReview.setVisibility(View.VISIBLE);
                btnUpdateUserReview.setVisibility(View.GONE);

                imgBtnDeleteUserReviewComDet.setVisibility(View.GONE);
                imgBtnEditUserReviewComDet.setVisibility(View.GONE);
                String addReview ="Add your review";
                txtUserDetYourReviewTag.setText(addReview);
            }
        });

        btnAddUserReview.setOnClickListener(this);
        btnUpdateUserReview.setOnClickListener(this);
        imgBtnEditUserReviewComDet.setOnClickListener(this);
        imgBtnDeleteUserReviewComDet.setOnClickListener(this);


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

    private void validateReviewAndUpload() {
        String review = null;
        if (textLayoutUserReview.getEditText()!=null){
            review  = textLayoutUserReview.getEditText().getText().toString().trim();
        }

        double rating = ratingAddUserRating.getRating();
        if (review==null || review.isEmpty()){
            textLayoutUserReview.setError("Review cannot be empty");
            return;
        }if (rating==0){
            Toast.makeText(requireActivity(), "Rating cannot be 0", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mInterstitialAd != null) {
            mInterstitialAd.show(requireActivity());
            mInterstitialAd.setFullScreenContentCallback(contentCallback);
        } else {
            viewModel.addUserReviewToFirestore(userId,review,rating);

        }

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.btn_add_user_review){
            validateReviewAndUpload();
        }
        if (viewId == R.id.btn_update_user_review){
            validateReviewAndUpload();
        }
        if (view.getId() == R.id.img_edit_my_review_user_com_det){
            Objects.requireNonNull(textLayoutUserReview.getEditText()).setEnabled(true);
            ratingAddUserRating.setIsIndicator(false);
            btnUpdateUserReview.setVisibility(View.VISIBLE);
        }
        if (view.getId() == R.id.img_delete_my_review_user_com_det){

            new AlertDialog.Builder(requireActivity())
                    .setTitle("Delete review")
                    .setMessage("Are you sure you want to delete your review?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        // Continue with delete operation
                        viewModel.deleteUserReviewFromFirestore(userId);
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
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
            String review = null;
            if (textLayoutUserReview.getEditText()!=null){
                review  = textLayoutUserReview.getEditText().getText().toString().trim();
            }

            double rating = ratingAddUserRating.getRating();
            viewModel.addUserReviewToFirestore(userId,review,rating);


        }

        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
            super.onAdFailedToShowFullScreenContent(adError);
            mInterstitialAd = null;
            String review = null;
            if (textLayoutUserReview.getEditText()!=null){
                review  = textLayoutUserReview.getEditText().getText().toString().trim();
            }

            double rating = ratingAddUserRating.getRating();
            viewModel.addUserReviewToFirestore(userId,review,rating);

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