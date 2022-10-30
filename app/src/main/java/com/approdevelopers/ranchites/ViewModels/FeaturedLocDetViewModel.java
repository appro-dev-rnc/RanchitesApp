package com.approdevelopers.ranchites.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.approdevelopers.ranchites.Models.FeaturedLocation;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;

import java.util.List;

public class FeaturedLocDetViewModel extends ViewModel {



    private LiveData<List<FeaturedLocation>> featuredLocLiveResult;

     public FeaturedLocDetViewModel(){
        this.featuredLocLiveResult = null;

    }

    public LiveData<List<FeaturedLocation>> getFeaturedLocLiveResult() {
        featuredLocLiveResult = FirestoreRepository.getInstance().getFeaturedLocLiveData(50);
        return featuredLocLiveResult;
    }


}
