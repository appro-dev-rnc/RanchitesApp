package com.approdevelopers.ranchites.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.approdevelopers.ranchites.Adapters.DiffUtilsUserReviewClass;
import com.approdevelopers.ranchites.Adapters.UserReviewAdapter;
import com.approdevelopers.ranchites.Interfaces.UserReviewReportInterface;
import com.approdevelopers.ranchites.Models.UserCompleteDetModel;
import com.approdevelopers.ranchites.Models.UserReviewModel;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.approdevelopers.ranchites.ViewModels.UserComDetViewModel;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewsAndRatings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewsAndRatings extends Fragment implements UserReviewReportInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String userId;
    UserComDetViewModel viewModel;
    LinearProgressIndicator seekBarUserRating5,seekBarUserRating4,seekBarUserRating3,seekUserBarRating2,seekBarUserRating1;

    UserReviewAdapter  userReviewAdapter;
    TextView txtUserComDetRatingSecTitle;
    TextView txtUserTotalReviewCount;
    AppCompatRatingBar ratingUserComDetRatingSection;
    private TextView txtUserDetNoReviewsFoundError;

    RecyclerView recyclerUserComDetReviews;



    public ReviewsAndRatings() {
        // Required empty public constructor
    }
    public ReviewsAndRatings(String userId) {
        // Required empty public constructor
        this.userId = userId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReviewsAndRatings.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewsAndRatings newInstance(String param1, String param2) {
        ReviewsAndRatings fragment = new ReviewsAndRatings();
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
        View view = inflater.inflate(R.layout.fragment_reviews_and_ratings, container, false);
        seekBarUserRating1 = view.findViewById(R.id.user_seekBar_rating_1);
        seekUserBarRating2 = view.findViewById(R.id.user_seekBar_rating_2);
        seekBarUserRating3 = view.findViewById(R.id.user_seekBar_rating_3);
        seekBarUserRating4 = view.findViewById(R.id.user_seekBar_rating_4);
        seekBarUserRating5 = view.findViewById(R.id.user_seekBar_rating_5);
        txtUserComDetRatingSecTitle = view.findViewById(R.id.txt_user_com_det_rating_sec_title);
        txtUserTotalReviewCount = view.findViewById(R.id.txt_user_total_reviews_count_rating_sec);
        ratingUserComDetRatingSection = view.findViewById(R.id.rating_user_com_det_rating_sec);
        recyclerUserComDetReviews  = view.findViewById(R.id.recycler_user_comp_det_reviews);
        txtUserDetNoReviewsFoundError = view.findViewById(R.id.txt_user_det_no_review_found_error);



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //init adapter
        userReviewAdapter =new UserReviewAdapter(new DiffUtilsUserReviewClass(),getViewLifecycleOwner(),this);

        //setting recycler view properties
        recyclerUserComDetReviews.setLayoutManager(new LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false));




        viewModel.getUserCompleteDetLive(userId).observe(requireActivity(), userCompleteDetModel -> {
            if (userCompleteDetModel!=null ){
                txtUserComDetRatingSecTitle.setText(String.valueOf(userCompleteDetModel.getUserRating()));
                ratingUserComDetRatingSection.setRating((float) userCompleteDetModel.getUserRating());

            }
        });

        viewModel.getSelectedUserReviewLive(userId).observe(requireActivity(), userReviewModels -> {
            if (userReviewModels!=null && !userReviewModels.isEmpty()){
                userReviewAdapter.submitList(userReviewModels);
                recyclerUserComDetReviews.setAdapter(userReviewAdapter);
                recyclerUserComDetReviews.setVisibility(View.VISIBLE);
                txtUserDetNoReviewsFoundError.setVisibility(View.GONE);

            }else {
                recyclerUserComDetReviews.setVisibility(View.GONE);
                txtUserDetNoReviewsFoundError.setVisibility(View.VISIBLE);
            }calculateAndUpdateRatingsSection(userReviewModels);
        });
    }

    private void calculateAndUpdateRatingsSection(List<UserReviewModel> reviewModels) {
        double totalReviewsCount = reviewModels.size();
        int rating_5 = 0,rating_4 = 0,rating_3 = 0,rating_2 = 0,rating_1 = 0;
        double ratingPercentCount_5=0,ratingPercentCount_4=0,ratingPercentCount_3=0,ratingPercentCount_2=0,ratingPercentCount_1=0;
        for (UserReviewModel model: reviewModels){
            double modelRating = model.getRating();
            if (modelRating>=5){
                rating_5 +=1;
            }
            if (modelRating>=4 && modelRating<5){
                rating_4+=1;
            }if (modelRating>=3 && modelRating <4){
                rating_3 +=1;
            }
            if (modelRating>=2 && modelRating<3){
                rating_2 +=1;
            }
            if (modelRating >=1 && modelRating<2){
                rating_1+=1;
            }
        }

        ratingPercentCount_5 = ((rating_5/totalReviewsCount)*100) ;
        ratingPercentCount_4 = (rating_4/totalReviewsCount)*100 ;
        ratingPercentCount_3 = (rating_3/totalReviewsCount)*100 ;
        ratingPercentCount_2 = (rating_2/totalReviewsCount)*100 ;
        ratingPercentCount_1 = (rating_1/totalReviewsCount)*100 ;

        txtUserTotalReviewCount.setText(new StringBuilder().append((int) totalReviewsCount).append(" reviews").toString());
        seekBarUserRating1.setProgress((int) ratingPercentCount_1);
        seekUserBarRating2.setProgress((int) ratingPercentCount_2);
        seekBarUserRating3.setProgress((int) ratingPercentCount_3);
        seekBarUserRating4.setProgress((int) ratingPercentCount_4);
        seekBarUserRating5.setProgress((int) ratingPercentCount_5);



    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void getReviewData(UserReviewModel userReviewModel) {
        FirestoreRepository.getInstance().reportUserReview(userId,userReviewModel.getUserId());
        Toast.makeText(requireActivity(), "Reported", Toast.LENGTH_SHORT).show();
    }
}