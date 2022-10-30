package com.approdevelopers.ranchites.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.approdevelopers.ranchites.Models.CityUpdatesModel;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class CityUpdatesDetViewModel extends ViewModel {

    private String currentUserId;
    private LiveData<CityUpdatesModel> cityUpdateModel;
    private LiveData<Boolean> likedByUser;
    private LiveData<Integer> likesCount;
    private LiveData<List<String>> cityUpdateImagesUrlList;
    private FirebaseUser currentUser;
    public CityUpdatesDetViewModel(){
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!=null){
            currentUserId = currentUser.getUid();
        }

        cityUpdateModel = null;
        likedByUser = null;
        likesCount = null;
        cityUpdateImagesUrlList = null;
    }

    public LiveData<CityUpdatesModel> getCityUpdateModel(String cityUpdateId) {
        cityUpdateModel = FirestoreRepository.getInstance().getCityUpdateDetail(cityUpdateId);
        return cityUpdateModel;
    }

    public LiveData<Boolean> getLikedByUser(String cityUpdateId) {
        likedByUser = FirestoreRepository.getInstance().checkUserLike(cityUpdateId,currentUserId);
        return likedByUser;
    }

    public LiveData<Integer> getLikesCount(String cityUpdateId) {
        likesCount =FirestoreRepository.getInstance().getCityUpdateLikesCount(cityUpdateId);
        return likesCount;
    }

    public void addToLiked(String cityUpdateId){
        FirestoreRepository.getInstance().likeCityUpdate(cityUpdateId,currentUserId);


    }public void removeFromLiked(String cityUpdateId){
        FirestoreRepository.getInstance().unLikeCityUpdate(cityUpdateId,currentUserId);

    }

    public LiveData<List<String>> getCityUpdateImagesUrlList(String cityUpdateId) {
        cityUpdateImagesUrlList =FirestoreRepository.getInstance().cityUpdatesImagesList(cityUpdateId);
        return cityUpdateImagesUrlList;
    }
}
