package com.approdevelopers.ranchites.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.approdevelopers.ranchites.Models.RecentlyViewedDetailedModel;
import com.approdevelopers.ranchites.Models.RecentlyViewedLocation;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class RecentlyViewedDetViewModel extends ViewModel {


    private LiveData<List<RecentlyViewedLocation>> recentlyViewedLocLiveResult;
    private LiveData<List<RecentlyViewedDetailedModel>> recentlyViewedDetailLive;
    String currentUserId = null;

    FirebaseUser currentUser;


    public RecentlyViewedDetViewModel(){
        this.recentlyViewedLocLiveResult = null;
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!=null){
            currentUserId = currentUser.getUid();

        }
        recentlyViewedDetailLive = null;
    }

    public LiveData<List<RecentlyViewedLocation>> getRecentlyViewedLocLiveResult() {
        recentlyViewedLocLiveResult = FirestoreRepository.getInstance().getRecentlyViewedLocLiveData(currentUserId,40);
        return recentlyViewedLocLiveResult;
    }

    public LiveData<List<RecentlyViewedDetailedModel>> getRecentlyViewedDetailLive(List<RecentlyViewedLocation> recentBaseList) {
        recentlyViewedDetailLive = FirestoreRepository.getInstance().getRecentlyViewedDetailedLiveData(currentUserId,recentBaseList);
        return recentlyViewedDetailLive;
    }
}
