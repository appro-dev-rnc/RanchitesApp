package com.approdevelopers.ranchites.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.approdevelopers.ranchites.Models.CityProfessionalsModel;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;

import java.util.List;

public class ProfessionalsDetViewModel extends ViewModel {


    private LiveData<List<CityProfessionalsModel>> cityProfessionalsLive;

    public ProfessionalsDetViewModel(){
        cityProfessionalsLive = null;
    }

    public LiveData<List<CityProfessionalsModel>> getCityProfessionalsLive() {
        cityProfessionalsLive = FirestoreRepository.getInstance().getCityProfessionalsLiveData(0);
        return cityProfessionalsLive;
    }
}
