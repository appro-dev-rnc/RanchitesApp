package com.approdevelopers.ranchites.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.approdevelopers.ranchites.Models.ImagesModel;
import com.approdevelopers.ranchites.Models.LocationModel;
import com.approdevelopers.ranchites.Models.UserCompleteDetModel;
import com.approdevelopers.ranchites.Models.UserReviewModel;
import com.approdevelopers.ranchites.Repository.FirebaseUserRepository;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class UserProfViewModel extends ViewModel {

    private FirebaseUserRepository firebaseUserRepository;
    private FirebaseUser currentUser;
    private FirestoreRepository firestoreRepository;
    private LiveData<List<LocationModel>> currentUserLocationLiveData;
    private LiveData<List<ImagesModel>> currentProfImages;
    private LiveData<UserCompleteDetModel> currentUserCompleteDataLive;
    private String userId = null;

    private LiveData<List<UserReviewModel>> myProfileReviewLive;


    public UserProfViewModel(){
        firebaseUserRepository = FirebaseUserRepository.getInstance();
        firestoreRepository = FirestoreRepository.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!=null){
            userId =currentUser.getUid();
        }
        currentUserLocationLiveData=null;
        currentProfImages = null;
        currentUserCompleteDataLive = null;
        myProfileReviewLive = null;

    }


    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public LiveData<List<LocationModel>> getCurrentUserLocationLiveData() {
        currentUserLocationLiveData = firestoreRepository.getCurrentUserLocationsLiveData(userId);
        return currentUserLocationLiveData;
    }

    public LiveData<List<ImagesModel>> getCurrentProfImages() {
        currentProfImages = firestoreRepository.getCurrentProfImages(userId);
        return currentProfImages;
    }

    public String getUserId() {
        return userId;
    }

    public LiveData<UserCompleteDetModel> getCurrentUserCompleteDataLive() {
        currentUserCompleteDataLive = firestoreRepository.getCurrentUserCompleteDetLive(userId);
        return currentUserCompleteDataLive;
    }

    public void signOutUser(){
        FirebaseUserRepository.getInstance().getFirebaseAuth().signOut();
    }

    public boolean isEmailVerified(){
        if (currentUser!=null){
            currentUser.reload();
        }
        if (currentUser != null) {
            return currentUser.isEmailVerified();
        }
        return false;
    }

    public Task<Void> sendEmailVerificationLink(){
        if (currentUser==null){
            return null;
        }
        return currentUser.sendEmailVerification();
    }

    public LiveData<List<UserReviewModel>> getMyProfileReviewLive() {
        myProfileReviewLive = FirestoreRepository.getInstance().getSelectedUserReviewLiveData(userId);
        return myProfileReviewLive;
    }
}
