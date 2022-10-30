package com.approdevelopers.ranchites.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.approdevelopers.ranchites.Models.BestRatedLocation;
import com.approdevelopers.ranchites.Models.CategoriesModel;
import com.approdevelopers.ranchites.Models.CityProfessionalsModel;
import com.approdevelopers.ranchites.Models.CityUpdatesModel;
import com.approdevelopers.ranchites.Models.FeaturedLocation;
import com.approdevelopers.ranchites.Models.LocationModel;
import com.approdevelopers.ranchites.Models.MostSearchedLocation;
import com.approdevelopers.ranchites.Models.RecentlyViewedDetailedModel;
import com.approdevelopers.ranchites.Models.RecentlyViewedLocation;
import com.approdevelopers.ranchites.Models.User;
import com.approdevelopers.ranchites.Models.UserBasicInfo;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainViewModel extends ViewModel {

    //liveData and Variables
    private  FirestoreRepository mFirestoreRepository;

    private LiveData<List<User>> userLiveDataResult;
    private LiveData<List<CityUpdatesModel>> cityUpdateLiveResult;
    private LiveData<List<FeaturedLocation>> featuredLocLiveResult;
    private LiveData<List<MostSearchedLocation>> mostSearchedLocLiveResult;
    private LiveData<List<BestRatedLocation>> bestRatedLocLiveResult;
    private LiveData<List<RecentlyViewedLocation>> recentlyViewedLocLiveResult;
    private LiveData<List<RecentlyViewedDetailedModel>> recentlyViewedDetailedLive;
    private LiveData<List<CategoriesModel>> categoriesLive;
    private String currentUserId =null;
    private LiveData<UserBasicInfo> currentUserProfData;
    private LiveData<List<CityProfessionalsModel>> cityProfessionalsLive;
    private static final int LIMIT = 9;
    private LiveData<List<LocationModel>> currentUserLocationLiveData;
    private CityUpdatesModel cityUpdateBannerSmall ;
    private CityUpdatesModel cityUpdateBannerLarge ;


    private MutableLiveData<Boolean> networkAvalable;

    FirebaseUser currentUser;


    //MainViewModel Constructor
    public MainViewModel(){
       currentUser = FirebaseAuth.getInstance().getCurrentUser();
       if (currentUser!=null){
           currentUserId = currentUser.getUid();
       }
        this.mFirestoreRepository = FirestoreRepository.getInstance();
        userLiveDataResult = mFirestoreRepository.getUserListLiveData();
        cityUpdateLiveResult = null;
        featuredLocLiveResult = null;
        mostSearchedLocLiveResult = mFirestoreRepository.getMostSearchedLocLiveData();
        bestRatedLocLiveResult = null;
        recentlyViewedLocLiveResult =null;


        categoriesLive = null;
        currentUserProfData = null;
        cityProfessionalsLive = null;
        recentlyViewedDetailedLive = null;
        networkAvalable  = new MutableLiveData<>();
        currentUserLocationLiveData=null;
        cityUpdateBannerSmall =null;
        cityUpdateBannerLarge = null;


    }

    public MutableLiveData<Boolean> getNetworkAvalable() {
        return networkAvalable;
    }


    public CityUpdatesModel getCityUpdateBannerSmall() {
        return cityUpdateBannerSmall;
    }

    public void setCityUpdateBannerSmall(CityUpdatesModel cityUpdateBannerSmall) {
        this.cityUpdateBannerSmall = cityUpdateBannerSmall;
    }

    public CityUpdatesModel getCityUpdateBannerLarge() {
        return cityUpdateBannerLarge;
    }

    public void setCityUpdateBannerLarge(CityUpdatesModel cityUpdateBannerLarge) {
        this.cityUpdateBannerLarge = cityUpdateBannerLarge;
    }

    //cityUpdatesLiveResult getter
    public LiveData<List<CityUpdatesModel>> getCityUpdateLiveResult(Context applicationContext) {
        cityUpdateLiveResult = mFirestoreRepository.getCityUpdatesLiveData(applicationContext);
        return cityUpdateLiveResult;
    }

    //UsersList LiveData getter
    public LiveData<List<User>> getUserLiveDataResult() {
        return userLiveDataResult ;
    }


    //getter for featuredLocations livedata
    public LiveData<List<FeaturedLocation>> getFeaturedLocLiveResult() {
        featuredLocLiveResult = mFirestoreRepository.getFeaturedLocLiveData(LIMIT);

        return featuredLocLiveResult;
    }



    //getter for mostSearchedLoc LiveData
    public LiveData<List<MostSearchedLocation>> getMostSearchedLocLiveResult() {
        return mostSearchedLocLiveResult;
    }

    //getter for BestRatedLoc Livedata
    public LiveData<List<BestRatedLocation>> getBestRatedLocLiveResult() {
        bestRatedLocLiveResult =  mFirestoreRepository.getBestRatedLocLiveData(4);
        return bestRatedLocLiveResult;
    }

    //getter for recentlyViewedLoc Livedata
    public LiveData<List<RecentlyViewedLocation>> getRecentlyViewedLocLiveResult() {
        recentlyViewedLocLiveResult = mFirestoreRepository.getRecentlyViewedLocLiveData(currentUserId,LIMIT);
        return recentlyViewedLocLiveResult;
    }

    public LiveData<List<CategoriesModel>> getCategoriesLive(Context applicationContext) {
        categoriesLive = FirestoreRepository.getInstance().getCategoriesLiveData(applicationContext);
        return categoriesLive;
    }

    public LiveData<UserBasicInfo> getCurrentUserProfData() {
        currentUserProfData = FirestoreRepository.getInstance().getUserProfileData(currentUserId);
        return currentUserProfData;
    }

    public LiveData<List<CityProfessionalsModel>> getCityProfessionalsLive() {
        cityProfessionalsLive = FirestoreRepository.getInstance().getCityProfessionalsLiveData(LIMIT);
        return cityProfessionalsLive;
    }

    public LiveData<List<RecentlyViewedDetailedModel>> getRecentlyViewedDetailedLive(List<RecentlyViewedLocation> recentlyViewedBaseList) {
        recentlyViewedDetailedLive = FirestoreRepository.getInstance().getRecentlyViewedDetailedLiveData(currentUserId,recentlyViewedBaseList);
        return recentlyViewedDetailedLive;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public LiveData<List<LocationModel>> getCurrentUserLocationLiveData() {
        currentUserLocationLiveData = FirestoreRepository.getInstance().getCurrentUserLocationsLiveData(currentUserId);
        return currentUserLocationLiveData;
    }
}
