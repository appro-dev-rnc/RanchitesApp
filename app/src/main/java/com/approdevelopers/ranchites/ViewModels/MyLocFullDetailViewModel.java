package com.approdevelopers.ranchites.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.approdevelopers.ranchites.Models.ImagesModel;
import com.approdevelopers.ranchites.Models.LocationModel;
import com.approdevelopers.ranchites.Models.ReviewModel;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;

import java.util.List;

public class MyLocFullDetailViewModel extends ViewModel {

    private LiveData<LocationModel> currentLocationDetailLiveData;
    private FirestoreRepository firestoreRepository;
    private LiveData<List<ImagesModel>> locationMediaImagesLive;
    private LiveData<List<ReviewModel>> selectedLocReviewLive;

    public MyLocFullDetailViewModel(){
        firestoreRepository = FirestoreRepository.getInstance();
        currentLocationDetailLiveData =null;
        selectedLocReviewLive = null;
        locationMediaImagesLive = null;
    }

    public LiveData<LocationModel> getCurrentLocationDetailLiveData(String locationId) {
        currentLocationDetailLiveData =firestoreRepository.getCurrentLocationLiveData(locationId);
        return currentLocationDetailLiveData;
    }



    public LiveData<List<ImagesModel>> loadLocationMediaImage(String locationId){
        locationMediaImagesLive = FirestoreRepository.getInstance().getMyLocationMediaImagesData(locationId);
        return locationMediaImagesLive;
    }


    public LiveData<List<ReviewModel>> getSelectedLocReviewLive(String locationId) {
        selectedLocReviewLive= FirestoreRepository.getInstance().getSelectedLocReviewLiveData(locationId);
        return selectedLocReviewLive;
    }


}
