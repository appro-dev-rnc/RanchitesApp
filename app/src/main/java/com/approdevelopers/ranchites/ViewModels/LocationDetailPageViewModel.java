package com.approdevelopers.ranchites.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.approdevelopers.ranchites.Models.ImagesModel;
import com.approdevelopers.ranchites.Models.LocationModel;
import com.approdevelopers.ranchites.Models.RecentlyViewedLocation;
import com.approdevelopers.ranchites.Models.ReviewModel;
import com.approdevelopers.ranchites.Models.UserBasicInfo;
import com.approdevelopers.ranchites.Repository.FirebaseUserRepository;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.google.firebase.Timestamp;

import java.util.List;


public class LocationDetailPageViewModel extends ViewModel {

    private LiveData<LocationModel> currentLocationDetailLive;
    private MutableLiveData<String> locationUserId;
    private LiveData<UserBasicInfo> ownerDetails;
    private LiveData<Boolean> locationSavedBool;
    private final String currentUserId;
    private LiveData<List<ReviewModel>> selectedLocReviewLive;
    private LiveData<ReviewModel> selectedLocCurrentUserLive;

    private LiveData<List<ImagesModel>> locationMediaImagesLive;

    public LocationDetailPageViewModel(){
        locationUserId = new MutableLiveData<>();
        currentLocationDetailLive = null;
        ownerDetails = null;
        locationSavedBool = null;
        currentUserId = FirebaseUserRepository.getInstance().getCurrentUser().getUid();
        selectedLocReviewLive = null;
        selectedLocCurrentUserLive = null;
        locationMediaImagesLive = null;
    }

    public LiveData<LocationModel> getCurrentLocationDetailLive(String placeId) {
        currentLocationDetailLive = FirestoreRepository.getInstance().getCurrentLocationLiveData(placeId);

        return currentLocationDetailLive;
    }

    public MutableLiveData<String> getLocationUserId() {
        return locationUserId;
    }

    public LiveData<UserBasicInfo> getGetOwnerDetails(String userId) {
        ownerDetails = FirestoreRepository.getInstance().getSelectedLocationOwnerDetails(userId);
        return ownerDetails;
    }
    public void updatePlacesSearchCount(String placeId){

        FirestoreRepository.getInstance().placesSearchCountIncrement(placeId,currentUserId);
    }

    public LiveData<Boolean> getLocationSavedBool(String placeId) {
        locationSavedBool = FirestoreRepository.getInstance().checkLocationSaved(placeId,currentUserId);
        return locationSavedBool;
    }

    public LiveData<List<ReviewModel>> getSelectedLocReviewLive(String placeId) {
        selectedLocReviewLive= FirestoreRepository.getInstance().getSelectedLocReviewLiveData(placeId);
        return selectedLocReviewLive;
    }

    public void addLocationToSaved(String placeId){
        FirestoreRepository.getInstance().addToSavedLocation(placeId,currentUserId);
    }
    public void removeLocationFromSaved(String placeId){
        FirestoreRepository.getInstance().removeFromSavedLocation(placeId,currentUserId);
    }

    public void uploadReviewToFirestore(String placeId,String review,double rating){
        ReviewModel model = new ReviewModel(placeId,currentUserId,review,rating, Timestamp.now());
        FirestoreRepository.getInstance().addLocationReview(model);
    }

    public LiveData<ReviewModel> getSelectedLocCurrentUserLive(String placeId) {
        selectedLocCurrentUserLive = FirestoreRepository.getInstance().getSelectedLocCurrentUserReview(placeId,currentUserId);
        return selectedLocCurrentUserLive;
    }

    public void deleteReviewFromFirestore(String placeId){
        FirestoreRepository.getInstance().deleteLocaitonReview(placeId,currentUserId);
    }


    public LiveData<List<ImagesModel>> loadLocationImagesData(String placeId){
        locationMediaImagesLive = FirestoreRepository.getInstance().getMyLocationMediaImagesData(placeId);
        return locationMediaImagesLive;
    }


    public void addToRecentlyViewed(String placeId){
        RecentlyViewedLocation location = new RecentlyViewedLocation();
        location.setDocumentId(placeId);
        location.setDocumentType(1);
        location.setViewTimestamp(Timestamp.now());

        FirestoreRepository.getInstance().addToRecentlyViewed(location,currentUserId);
    }
}
