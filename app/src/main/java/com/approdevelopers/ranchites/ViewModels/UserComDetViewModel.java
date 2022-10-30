package com.approdevelopers.ranchites.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.approdevelopers.ranchites.Models.ImagesModel;
import com.approdevelopers.ranchites.Models.RecentlyViewedLocation;
import com.approdevelopers.ranchites.Models.UserCompleteDetModel;
import com.approdevelopers.ranchites.Models.UserReviewModel;
import com.approdevelopers.ranchites.Repository.FirebaseUserRepository;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class UserComDetViewModel extends ViewModel {

    private LiveData<UserCompleteDetModel> userCompleteDetLive;
    private LiveData<List<ImagesModel>> userMediaImagesLive;
    private String currentUserId;
    private LiveData<List<UserReviewModel>> selectedUserReviewLive;
    private LiveData<UserReviewModel> selectedUserCurrentReview;
    private FirebaseUser currentUser;

    public UserComDetViewModel(){
        userCompleteDetLive = null;
        userMediaImagesLive = null;
        selectedUserReviewLive = null;
        selectedUserCurrentReview = null;
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!=null){
            currentUserId = currentUser.getUid();

        }
    }

    public LiveData<UserCompleteDetModel> getUserCompleteDetLive(String userId) {
        userCompleteDetLive = FirestoreRepository.getInstance().getUserComDetLiveData(userId);
        return userCompleteDetLive;
    }

    public LiveData<List<ImagesModel>> getUserMediaImagesLive(String userId) {
        userMediaImagesLive = FirestoreRepository.getInstance().getUserMediaImagesLiveData(userId);
        return userMediaImagesLive;
    }

    public void addUserReviewToFirestore(String uId,String review,double rating){
        UserReviewModel model = new UserReviewModel(uId,currentUserId,review,rating, Timestamp.now());
        FirestoreRepository.getInstance().addUserReview(model);

    }

    public void deleteUserReviewFromFirestore(String uId){
        FirestoreRepository.getInstance().deleteUserReview(uId,currentUserId);
    }

    public LiveData<List<UserReviewModel>> getSelectedUserReviewLive(String uId) {
        selectedUserReviewLive = FirestoreRepository.getInstance().getSelectedUserReviewLiveData(uId);
        return selectedUserReviewLive;
    }

    public LiveData<UserReviewModel> getSelectedUserCurrentReview(String uId) {
        selectedUserCurrentReview = FirestoreRepository.getInstance().getSelectedUserCurrentUserReview(uId,currentUserId);
        return selectedUserCurrentReview;
    }


    public void addToRecentlyViewed(String professionId){
        RecentlyViewedLocation location = new RecentlyViewedLocation();
        location.setDocumentId(professionId);
        location.setDocumentType(2);
        location.setViewTimestamp(Timestamp.now());

        FirestoreRepository.getInstance().addToRecentlyViewed(location,currentUserId);
    }
    public String getUserPhoneNumber(){
        return FirebaseUserRepository.getInstance().getCurrentUser().getPhoneNumber();
    }
    public String getUserEmailId(){
        return FirebaseUserRepository.getInstance().getCurrentUser().getEmail();
    }
}
