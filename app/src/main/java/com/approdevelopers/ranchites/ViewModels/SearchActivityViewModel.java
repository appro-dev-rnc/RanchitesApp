package com.approdevelopers.ranchites.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.approdevelopers.ranchites.Models.RecentlyViewedDetailedModel;
import com.approdevelopers.ranchites.Models.RecentlyViewedLocation;
import com.approdevelopers.ranchites.Models.SearchResultModel;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class SearchActivityViewModel extends ViewModel {

    private LiveData<List<SearchResultModel>> searchResultLiveData;

    private LiveData<List<RecentlyViewedLocation>> recentlyViewedLocLiveResult;
    private LiveData<List<RecentlyViewedDetailedModel>> recentlyViewedDetailLive;
    private LiveData<List<String>> recentSearchesLive;
    private String currentUserId =null;
    private LiveData<List<String>> searchGuideKeywords;
    private FirebaseUser currentUser;

    private MutableLiveData<String> searchQueryKeyword;



    public SearchActivityViewModel(){
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!=null){
            currentUserId = currentUser.getUid();
        }

       searchResultLiveData =null;
        recentlyViewedLocLiveResult = null;
        recentSearchesLive = null;
        recentlyViewedDetailLive = null;
        searchQueryKeyword = new MutableLiveData<>();
        searchGuideKeywords=null;

    }

    public LiveData<List<SearchResultModel>> getSearchResultLiveData(String queryKeyword) {
        searchResultLiveData = FirestoreRepository.getInstance().getSearchResultsLiveData(queryKeyword);
        return searchResultLiveData;
    }

    public void searchForResults(String queryKeyword){
        searchQueryKeyword.postValue(queryKeyword);
    }

    public LiveData<List<RecentlyViewedLocation>> getRecentlyViewedLocLiveResult() {
        recentlyViewedLocLiveResult  = FirestoreRepository.getInstance().getRecentlyViewedLocLiveData(currentUserId,10);
        return recentlyViewedLocLiveResult;
    }

    public void addToRecentSearches(String searchKeyword){
        FirestoreRepository.getInstance().addToRecentSearches(searchKeyword,currentUserId);
    }

    public LiveData<List<String>> getRecentSearchesLive() {
        recentSearchesLive = FirestoreRepository.getInstance().getRecentSearchesLive(currentUserId);
        return recentSearchesLive;
    }

    public LiveData<List<RecentlyViewedDetailedModel>> getRecentlyViewedDetailLive(List<RecentlyViewedLocation> recentBaseList) {
        recentlyViewedDetailLive = FirestoreRepository.getInstance().getRecentlyViewedDetailedLiveData(currentUserId,recentBaseList);
        return recentlyViewedDetailLive;
    }

    public void deleteFromRecentSearch(String keyword){
        FirestoreRepository.getInstance().removeFromRecentSearches(keyword,currentUserId);
    }

    public MutableLiveData<String> getSearchQueryKeyword() {
        return searchQueryKeyword;
    }

    public LiveData<List<String>> getSearchGuideKeywords() {
        searchGuideKeywords = FirestoreRepository.getInstance().getSearchKeywordsList();
        return searchGuideKeywords;
    }




}
