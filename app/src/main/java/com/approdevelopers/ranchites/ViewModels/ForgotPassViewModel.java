package com.approdevelopers.ranchites.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPassViewModel extends ViewModel {

    private LiveData<Boolean> phoneNumberExists;

    MutableLiveData<String> otpVerificationId;
    private final FirebaseAuth auth;
    private FirebaseUser verifiedUser;

    public  ForgotPassViewModel(){
        auth = FirebaseAuth.getInstance();
        phoneNumberExists = null;
        otpVerificationId = new MutableLiveData<>();
        verifiedUser = null;
    }

    public LiveData<Boolean> getPhoneNumberExists(String phoneNo) {
        phoneNumberExists = FirestoreRepository.getInstance().checkPhoneNumberExists(phoneNo);
        return phoneNumberExists;
    }



    public MutableLiveData<String> getOtpVerificationId() {
        return otpVerificationId;
    }



    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseUser getVerifiedUser() {
        return verifiedUser;
    }

    public void setVerifiedUser(FirebaseUser verifiedUser) {
        this.verifiedUser = verifiedUser;
    }

    public Task<Void> updateUserPassword(String userId, String password){
        return FirestoreRepository.getInstance().updateUserPasswordInFirestore(userId,password);
    }


}
