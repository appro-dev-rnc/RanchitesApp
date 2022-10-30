package com.approdevelopers.ranchites.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.approdevelopers.ranchites.Adapters.DiffUtilsReviewsClass;
import com.approdevelopers.ranchites.Adapters.ReviewsAdapter;
import com.approdevelopers.ranchites.Interfaces.PlaceReviewReportInterface;
import com.approdevelopers.ranchites.Models.ReviewModel;
import com.approdevelopers.ranchites.R;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.approdevelopers.ranchites.ViewModels.MyLocFullDetailViewModel;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyLocationReviewAndRatings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyLocationReviewAndRatings extends Fragment implements PlaceReviewReportInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView txtTotalReviewCount,txtRatingSecTitle;
    AppCompatRatingBar ratingSecComDetRatingBar;
    LinearProgressIndicator seekBarRating5,seekBarRating4,seekBarRating3,seekBarRating2,seekBarRating1;

    RecyclerView recyclerCompDetReviews;
    ReviewsAdapter reviewsAdapter;

    private String locationDocId;
    private MyLocFullDetailViewModel viewModel;
    private TextView txtMyLocNoReviewFoundError;

    public MyLocationReviewAndRatings() {
        // Required empty public constructor
    }public MyLocationReviewAndRatings(String locationDocId) {
        // Required empty public constructor
        this.locationDocId = locationDocId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyLocationReviewAndRatings.
     */
    // TODO: Rename and change types and number of parameters
    public static MyLocationReviewAndRatings newInstance(String param1, String param2) {
        MyLocationReviewAndRatings fragment = new MyLocationReviewAndRatings();
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

        if (locationDocId!=null)
            viewModel = new ViewModelProvider(requireActivity())
                    .get(MyLocFullDetailViewModel.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_location_review_and_ratings, container, false);
        txtTotalReviewCount = view.findViewById(R.id.txt_my_loc_reviews_count_rating_sec);
        txtRatingSecTitle = view.findViewById(R.id.txt_my_loc_rating_sec_title);
        ratingSecComDetRatingBar = view.findViewById(R.id.rating_my_loc_rating_sec);
        txtMyLocNoReviewFoundError = view.findViewById(R.id.txt_my_loc_no_review_found_error);

        seekBarRating1 = view.findViewById(R.id.my_loc_seekBar_rating_1);
        seekBarRating2 = view.findViewById(R.id.my_loc_seekBar_rating_2);
        seekBarRating3 = view.findViewById(R.id.my_loc_seekBar_rating_3);
        seekBarRating4 = view.findViewById(R.id.my_loc_seekBar_rating_4);
        seekBarRating5 = view.findViewById(R.id.my_loc_seekBar_rating_5);
        recyclerCompDetReviews  = view.findViewById(R.id.recycler_my_location_reviews);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reviewsAdapter = new ReviewsAdapter(new DiffUtilsReviewsClass(),getViewLifecycleOwner(),this);

        // recyclerCompDetReviews.setHasFixedSize(true);
        recyclerCompDetReviews.setLayoutManager(new LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false));


        viewModel.getCurrentLocationDetailLiveData(locationDocId).observe(requireActivity(), locationModel -> {
            if (locationModel!=null){
                txtRatingSecTitle.setText(String.valueOf(locationModel.getOverallRating()));
                ratingSecComDetRatingBar.setRating((float) locationModel.getOverallRating());

            }
        });


        viewModel.getSelectedLocReviewLive(locationDocId).observe(requireActivity(), reviewModels -> {
            if (reviewModels!=null && !reviewModels.isEmpty()){
                recyclerCompDetReviews.setVisibility(View.VISIBLE);
                reviewsAdapter.submitList(reviewModels);
                recyclerCompDetReviews.setAdapter(reviewsAdapter);
                txtMyLocNoReviewFoundError.setVisibility(View.GONE);
                recyclerCompDetReviews.setVisibility(View.VISIBLE);

            }else {
                txtMyLocNoReviewFoundError.setVisibility(View.VISIBLE);
                recyclerCompDetReviews.setVisibility(View.GONE);
            }
           calculateAndUpdateRatingsSection(reviewModels);
        });


    }

    private void calculateAndUpdateRatingsSection(List<ReviewModel> reviewModels) {
        double totalReviewsCount = reviewModels.size();

        int rating_5 = 0,rating_4 = 0,rating_3 = 0,rating_2 = 0,rating_1 = 0;
        double ratingPercentCount_5=0,ratingPercentCount_4=0,ratingPercentCount_3=0,ratingPercentCount_2=0,ratingPercentCount_1=0;
        for (ReviewModel model: reviewModels){
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

        txtTotalReviewCount.setText(new StringBuilder().append((int) totalReviewsCount).append(" reviews").toString());
        seekBarRating1.setProgress((int) ratingPercentCount_1);
        seekBarRating2.setProgress((int) ratingPercentCount_2);
        seekBarRating3.setProgress((int) ratingPercentCount_3);
        seekBarRating4.setProgress((int) ratingPercentCount_4);
        seekBarRating5.setProgress((int) ratingPercentCount_5);



    }

    @Override
    public void getPlaceReviewDetails(ReviewModel model) {
        FirestoreRepository.getInstance().reportPlaceReview(locationDocId,model.getUserId());
        Toast.makeText(requireActivity(), "Reported", Toast.LENGTH_SHORT).show();
    }
}