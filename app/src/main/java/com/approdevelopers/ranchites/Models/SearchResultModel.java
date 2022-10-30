package com.approdevelopers.ranchites.Models;

import com.google.firebase.firestore.GeoPoint;

public class SearchResultModel {
    private String title;
    private String documentId;
    private String imageUrl;
    private String category;
    private String desc;
    private String address;
    private int documentType;
    private GeoPoint locGeopoint;
    private double overallRating;
    private double distanceFromUser;

    public SearchResultModel() {
    }

    public SearchResultModel(String title, String documentId, String imageUrl, String category, String desc, String address, int documentType, GeoPoint locGeopoint, double overallRating) {
        this.title = title;
        this.documentId = documentId;
        this.imageUrl = imageUrl;
        this.category = category;
        this.desc = desc;
        this.address = address;
        this.documentType = documentType;
        this.locGeopoint = locGeopoint;
        this.overallRating = overallRating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getDocumentType() {
        return documentType;
    }

    public void setDocumentType(int documentType) {
        this.documentType = documentType;
    }

    public GeoPoint getLocGeopoint() {
        return locGeopoint;
    }

    public void setLocGeopoint(GeoPoint locGeopoint) {
        this.locGeopoint = locGeopoint;
    }

    public double getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(double overallRating) {
        this.overallRating = overallRating;
    }

    public double getDistanceFromUser() {
        return distanceFromUser;
    }

    public void setDistanceFromUser(double distanceFromUser) {
        this.distanceFromUser = distanceFromUser;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "SearchResultModel{" +
                "title='" + title + '\'' +
                ", documentId='" + documentId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", category='" + category + '\'' +
                ", desc='" + desc + '\'' +
                ", address='" + address + '\'' +
                ", documentType=" + documentType +
                ", locGeopoint=" + locGeopoint +
                ", overallRating=" + overallRating +
                ", distanceFromUser=" + distanceFromUser +
                '}';
    }

    public String getStringParsedTitleDescCategory(){
        return title+"\n"+"\n"+category+"\n"+desc;
    }
}
