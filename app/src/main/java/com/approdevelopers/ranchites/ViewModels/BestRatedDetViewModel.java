package com.approdevelopers.ranchites.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.approdevelopers.ranchites.Models.BestRatedLocation;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;

import java.util.List;

public class BestRatedDetViewModel extends ViewModel {


    private LiveData<List<BestRatedLocation>> bestRatedLocLiveResult;

    public BestRatedDetViewModel(){
        bestRatedLocLiveResult = null;
    }

    public LiveData<List<BestRatedLocation>> getBestRatedLocLiveResult() {
        bestRatedLocLiveResult = FirestoreRepository.getInstance().getBestRatedLocLiveData(0);
        return bestRatedLocLiveResult;
    }
}
