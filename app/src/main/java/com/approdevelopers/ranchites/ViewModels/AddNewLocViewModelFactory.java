package com.approdevelopers.ranchites.ViewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AddNewLocViewModelFactory implements ViewModelProvider.Factory {
    private String locationDocId;

    public AddNewLocViewModelFactory(String locationDocId){
        this.locationDocId = locationDocId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddNewLocViewModel(locationDocId);
    }
}
