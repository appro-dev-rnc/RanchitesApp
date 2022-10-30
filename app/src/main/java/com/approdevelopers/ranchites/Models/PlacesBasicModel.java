package com.approdevelopers.ranchites.Models;

public class PlacesBasicModel {
    private String documentId;
    private String title;
    private String desc;
    private String address;
    private String category;
    private String imageUrl;
    private double overallRating;
    private int documentType;

    public PlacesBasicModel() {
    }

    public PlacesBasicModel(String documentId, String title, String desc, String address, String category, String imageUrl, double overallRating, int documentType) {
        this.documentId = documentId;
        this.title = title;
        this.desc = desc;
        this.address = address;
        this.category = category;
        this.imageUrl = imageUrl;
        this.overallRating = overallRating;
        this.documentType = documentType;
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

    public double getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(double overallRating) {
        this.overallRating = overallRating;
    }

    public int getDocumentType() {
        return documentType;
    }

    public void setDocumentType(int documentType) {
        this.documentType = documentType;
    }

    @Override
    public String toString() {
        return "PlacesBasicModel{" +
                "documentId='" + documentId + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", address='" + address + '\'' +
                ", category='" + category + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", overallRating=" + overallRating +
                ", documentType=" + documentType +
                '}';
    }
}
