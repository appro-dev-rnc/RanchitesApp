package com.approdevelopers.ranchites.Models;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

public class LocationModel {

    private String documentId;
    private String title;
    private String desc;
    private String address;
    private String category;
    private String imageUrl;
    private String ownerId;
    private GeoPoint locGeopoint;
    private Timestamp timestamp;
    private int searchCount;
    private double overallRating;
    private int documentType;
    private boolean locationShareEnabled;



    public LocationModel(String documentId, String title, String desc, String address, String category, String imageUrl, String ownerId, GeoPoint locGeopoint, Timestamp timestamp, int searchCount, double overallRating, int documentType, boolean locationShareEnabled) {
        this.documentId = documentId;
        this.title = title;
        this.desc = desc;
        this.address = address;
        this.category = category;
        this.imageUrl = imageUrl;
        this.ownerId = ownerId;
        this.locGeopoint = locGeopoint;
        this.timestamp = timestamp;
        this.searchCount = searchCount;
        this.overallRating = overallRating;
        this.documentType = documentType;
        this.locationShareEnabled = locationShareEnabled;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public int getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }

    public double getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(double overallRating) {
        this.overallRating = overallRating;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public GeoPoint getLocGeopoint() {
        return locGeopoint;
    }

    public void setLocGeopoint(GeoPoint locGeopoint) {
        this.locGeopoint = locGeopoint;
    }

    public LocationModel() {
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

    public int getDocumentType() {
        return documentType;
    }

    public void setDocumentType(int documentType) {
        this.documentType = documentType;
    }

    public boolean getLocationShareEnabled() {
        return locationShareEnabled;
    }

    public void setLocationShareEnabled(boolean locationShareEnabled) {
        this.locationShareEnabled = locationShareEnabled;
    }

    @Override
    public String toString() {
        return "LocationModel{" +
                "documentId='" + documentId + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", address='" + address + '\'' +
                ", category='" + category + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", locGeopoint=" + locGeopoint +
                ", timestamp=" + timestamp +
                ", searchCount=" + searchCount +
                ", overallRating=" + overallRating +
                ", documentType=" + documentType +
                ", locationShareEnabled=" + locationShareEnabled +
                '}';
    }
}
