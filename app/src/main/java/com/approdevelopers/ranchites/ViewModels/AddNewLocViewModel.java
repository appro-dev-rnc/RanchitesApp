package com.approdevelopers.ranchites.ViewModels;


import android.graphics.Bitmap;
import android.location.Location;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.approdevelopers.ranchites.Models.EditLocationModel;

import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddNewLocViewModel extends ViewModel {

    private MutableLiveData<Bitmap> locBannerBitmap ;
    private String title;
    private String documentId;
    private String address;
    private String desc;
    private String category;
    private GeoPoint markedLocation;
    private String imageUrl;
    private String ownerId;
    private String ownerEmail;
    private String ownerPhone;
    private boolean locationShareEnabled;
    private LiveData<EditLocationModel> editLocationModel;
    private String locationDocId;
    private MutableLiveData<Location> userCurrentLocation;
    private LiveData<List<String>> categoriesNameList;





    private FirebaseUser currentUser;



    public AddNewLocViewModel(String locationDocId){
        this.locationDocId = locationDocId;
        locBannerBitmap = new MutableLiveData<>(null);
        title = null;
        address = null;
        desc =null;
        documentId = null;
        category = null;
        markedLocation =null;
        imageUrl = null;
        locationShareEnabled = false;
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        userCurrentLocation  =  new MutableLiveData<>();
        categoriesNameList=null;



    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }


    public String getOwnerEmail() {
        if (currentUser!=null){
            ownerEmail = currentUser.getEmail();

        }
        return ownerEmail;
    }

    public String getOwnerPhone() {
        if (currentUser!=null){
            ownerPhone = currentUser.getPhoneNumber();

        }
        return ownerPhone;
    }

    public String getOwnerId() {
        if (currentUser!=null){
            ownerId = currentUser.getUid();

        }
        return ownerId;
    }

    public boolean getLocationShareEnabled() {
        return locationShareEnabled;
    }

    public void setLocationShareEnabled(boolean locationShareEnabled) {
        this.locationShareEnabled = locationShareEnabled;
    }

    public MutableLiveData<Bitmap> getLocBannerBitmap() {
        return locBannerBitmap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public GeoPoint getMarkedLocation() {
        return markedLocation;
    }


    public void setMarkedLocation(GeoPoint markedLocation) {
        this.markedLocation = markedLocation;
    }

    public boolean validateTitle(){
        return title != null && !title.isEmpty();
    }
    public boolean validateDesc(){
        return desc != null && !desc.isEmpty();
    }
    public boolean validateAddress(){
        return address != null && !address.isEmpty();
    }
    public boolean validateCategory(){
        return category!=null && !category.isEmpty();
    }

    public boolean validateImageBitmap(){
        return locBannerBitmap.getValue()!=null;
    }
    public boolean validateImageUrl(){
        return imageUrl != null && !imageUrl.isEmpty();
    }
    public boolean validateMarkedLocation(){
        return markedLocation!=null;
    }


    public LiveData<EditLocationModel> getEditLocationModel() {
            editLocationModel = FirestoreRepository.getInstance().getMyEditLocationModel(locationDocId);
            return editLocationModel;
    }

    public MutableLiveData<Location> getUserCurrentLocation() {
        return userCurrentLocation;
    }
    public LiveData<List<String>> getCategoriesNameList() {
        categoriesNameList = FirestoreRepository.getInstance().getCategoriesNameList();
        return categoriesNameList;
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
        finalWordsList.add(title.trim());
        finalWordsList.add(category.trim());
        finalWordsList.addAll(generateAboutStringList(desc));

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
