package com.approdevelopers.ranchites.Models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

public class EditLocationModel {
    private String documentId;
    private String title;
    private String desc;
    private String address;
    private String category;
    private String imageUrl;
    private GeoPoint locGeopoint;
    private boolean locationShareEnabled;
    private Timestamp updateTimeStamp;

    public EditLocationModel() {
    }



    public Timestamp getUpdateTimeStamp() {
        return updateTimeStamp;
    }

    public void setUpdateTimeStamp(Timestamp updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }

    public EditLocationModel(String documentId, String title, String desc, String address, String category, String imageUrl, GeoPoint locGeopoint, boolean locationShareEnabled, Timestamp updateTimeStamp) {
        this.documentId = documentId;
        this.title = title;
        this.desc = desc;
        this.address = address;
        this.category = category;
        this.imageUrl = imageUrl;
        this.locGeopoint = locGeopoint;
        this.locationShareEnabled = locationShareEnabled;
        this.updateTimeStamp = updateTimeStamp;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public GeoPoint getLocGeopoint() {
        return locGeopoint;
    }

    public void setLocGeopoint(GeoPoint locGeopoint) {
        this.locGeopoint = locGeopoint;
    }

    public boolean getLocationShareEnabled() {
        return locationShareEnabled;
    }

    public void setLocationShareEnabled(boolean locationShareEnabled) {
        this.locationShareEnabled = locationShareEnabled;
    }

    @Override
    public String toString() {
        return "EditLocationModel{" +
                "documentId='" + documentId + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", address='" + address + '\'' +
                ", category='" + category + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", locGeopoint=" + locGeopoint +
                ", locationShareEnabled=" + locationShareEnabled +
                '}';
    }
}

