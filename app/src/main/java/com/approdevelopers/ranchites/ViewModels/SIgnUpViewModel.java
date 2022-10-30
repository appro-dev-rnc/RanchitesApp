package com.approdevelopers.ranchites.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SIgnUpViewModel extends ViewModel {

    MutableLiveData<String> otpVerificationId;

    public SIgnUpViewModel(){
        otpVerificationId = new MutableLiveData<>();
    }

    public MutableLiveData<String> getOtpVerificationId() {
        return otpVerificationId;
    }
}
