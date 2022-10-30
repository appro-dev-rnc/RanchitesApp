package com.approdevelopers.ranchites.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.approdevelopers.ranchites.Models.PlacesBasicModel;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class SavedPlacesViewModel extends ViewModel {

    private LiveData<List<String>> savedPlacesIdLive;
    private LiveData<List<PlacesBasicModel>> savedPlacesCompleteDetLive;
    private String currentUserId = null;
    private FirebaseUser currentUser;

    public SavedPlacesViewModel(){
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!=null){
            currentUserId = currentUser.getUid();
        }

        savedPlacesCompleteDetLive = null;
        savedPlacesIdLive  =null;

    }

    public LiveData<List<String>> getSavedPlacesIdLive() {
        savedPlacesIdLive  = FirestoreRepository.getInstance().getSavedPlacesIdList(currentUserId);

        return savedPlacesIdLive;
    }

    public LiveData<List<PlacesBasicModel>> getSavedPlacesCompleteDetLive(List<String> placeIdList) {
        savedPlacesCompleteDetLive = FirestoreRepository.getInstance().getSavedLocationsDetails(placeIdList);
        return savedPlacesCompleteDetLive;
    }

    public void change(){
        MutableLiveData<List<String>> mutableLiveData = new MutableLiveData<>();
        List<String> strings = new ArrayList<>();
        strings.add("fajlkasj");
        savedPlacesIdLive = mutableLiveData;
        mutableLiveData.postValue(strings);
    }
}
