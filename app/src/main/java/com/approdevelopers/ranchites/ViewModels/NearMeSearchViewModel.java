package com.approdevelopers.ranchites.ViewModels;

import android.location.Location;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.approdevelopers.ranchites.Models.SearchResultModel;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;

import java.util.List;

public class NearMeSearchViewModel extends ViewModel {


    private LiveData<List<SearchResultModel>> nearMeSearchResultLive;
    private MutableLiveData<Location> userLocation;
    private LiveData<List<String>> searchGuideKeywords;

    private int locationCheckCount =0;

    public NearMeSearchViewModel(){

        userLocation = new MutableLiveData<>();
        searchGuideKeywords=null;

    }



    public LiveData<List<String>> getSearchGuideKeywords() {
        searchGuideKeywords = FirestoreRepository.getInstance().getNearSearchKeywordsList();
        return searchGuideKeywords;
    }

    public LiveData<List<SearchResultModel>> getNearMeSearchResultLive() {
        nearMeSearchResultLive = FirestoreRepository.getInstance().getNearMeSearchResultsLive();
        return nearMeSearchResultLive;
    }
    public void searchNearMeKeyword(String queryKeyword){
        locationCheckCount=0;
        FirestoreRepository.getInstance().searchNearMeForKeywordLive(queryKeyword);
    }

    public MutableLiveData<Location> getUserLocation() {
        return userLocation;
    }

    public int getLocationCheckCount() {
        return locationCheckCount;
    }

    public void setLocationCheckCount(int locationCheckCount) {
        this.locationCheckCount = locationCheckCount;
    }


}
