package com.approdevelopers.ranchites.ViewModels;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.approdevelopers.ranchites.Models.CategoriesModel;
import com.approdevelopers.ranchites.Models.User;
import com.approdevelopers.ranchites.Models.UserUpdateModel;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditUserProfViewModel extends ViewModel {

    private MutableLiveData<Bitmap> userProfImgBitmap;
    private MutableLiveData<String> userProfImgUrl;
    private FirebaseUser currentUser;
    private String uId = null;
    private String userName = null;
    private String userProfession = null;
    private String userAbout = null;
    private boolean searchByProfessionEnabled = false;
    private LiveData<User> currentUserProfLiveData;
    private LiveData<List<CategoriesModel>> categoriesLiveData;
    private LiveData<List<String>> professionNameList;


    public EditUserProfViewModel(){
        userProfImgBitmap = new MutableLiveData<>(null);
        userProfImgUrl = new MutableLiveData<>();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        categoriesLiveData = null;

        if (currentUser!= null){
            uId = currentUser.getUid();
        }
        professionNameList=null;
        currentUserProfLiveData = FirestoreRepository.getInstance().getCurrentUserProfileLiveData(uId);
    }

    public LiveData<List<String>> getProfessionNameList() {
        professionNameList = FirestoreRepository.getInstance().getProfessionNameList();

        return professionNameList;
    }

    public LiveData<User> getCurrentUserProfLiveData() {
        return currentUserProfLiveData;
    }

    public MutableLiveData<Bitmap> getUserProfImgBitmap() {
        return userProfImgBitmap;
    }

    public String getuId() {
        return uId;
    }

    public MutableLiveData<String> getUserProfImgUrl() {
        return userProfImgUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserProfession(String userProfession) {
        this.userProfession = userProfession;
    }

    public void setUserAbout(String userAbout) {
        this.userAbout = userAbout;
    }

    public void setSearchByProfessionEnabled(boolean searchByProfessionEnabled) {
        this.searchByProfessionEnabled = searchByProfessionEnabled;
    }

    public LiveData<List<CategoriesModel>> getCategoriesLiveData(Context applicationContext) {

        categoriesLiveData = FirestoreRepository.getInstance().getCategoriesLiveData(applicationContext);

        return categoriesLiveData;
    }

    public boolean validateUserName(){
        return userName!= null && !userName.isEmpty();
    }
    public boolean validateProfession(){
        if (searchByProfessionEnabled){
            return userProfession!=null && !userProfession.isEmpty();
        }
        else {
            return true;
        }
    }
    public boolean validateUserAbout(){
        return userAbout!=null && !userAbout.isEmpty();
    }

    public UserUpdateModel getUserCreated(){
        return new UserUpdateModel(uId,userName,searchByProfessionEnabled,userProfession,userAbout,userProfImgUrl.getValue(),2);
    }

    private List<String> generateAboutStringList(String aboutString){
        List<String> hashtagsList = new ArrayList<>();
        Pattern MY_PATTERN = Pattern.compile("#(\\S+)");
        Matcher mat = MY_PATTERN.matcher(aboutString);
        while (mat.find()) {
            hashtagsList.add(mat.group(1));
        }

        return hashtagsList;
    }
    private List<String> generateFinalListOfWords(){

        List<String> finalWordsList = new ArrayList<>();
        if (searchByProfessionEnabled && !userProfession.equals("")){
            finalWordsList.add(userProfession.trim());
        }
        if (userAbout!=null && !userAbout.equals("")){
            finalWordsList.addAll(generateAboutStringList(userAbout));

        }

        return finalWordsList;
    }
    public List<String> generateKeywords(){
        List<String> wordsList  = generateFinalListOfWords();
        List<String> keywordsList = new ArrayList<>();

        for (String word : wordsList){
            int length = word.length();
            for(int i=0;i<length;i++){
                String current = word.substring(0,i+1);
                keywordsList.add(current);
            }
        }
        return keywordsList;
    }
}
