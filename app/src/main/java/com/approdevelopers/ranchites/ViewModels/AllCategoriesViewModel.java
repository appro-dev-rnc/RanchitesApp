package com.approdevelopers.ranchites.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.approdevelopers.ranchites.Models.CategoriesModel;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;

import java.util.List;

public class AllCategoriesViewModel extends ViewModel {

    private LiveData<List<CategoriesModel>> allCategoriesLive;

    public AllCategoriesViewModel(){
        allCategoriesLive=null;
    }

    public LiveData<List<CategoriesModel>> getAllCategoriesLive(Context context) {
        allCategoriesLive = FirestoreRepository.getInstance().getCategoriesLiveData(context);
        return allCategoriesLive;
    }



}
