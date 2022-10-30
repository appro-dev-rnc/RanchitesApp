package com.approdevelopers.ranchites.Models;

import androidx.annotation.NonNull;

public class CityUpdatesModel {

    private String documentId;
    private String imageUrl;
    private String titleText;
    private String descText;
    private int documentType;
    private String website;



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

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getDescText() {
        return descText;
    }

    public void setDescText(String descText) {
        this.descText = descText;
    }

    public int getDocumentType() {
        return documentType;
    }

    public void setDocumentType(int documentType) {
        this.documentType = documentType;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @NonNull
    @Override
    public String toString() {
        return "CityUpdatesModel{" +
                "documentId='" + documentId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", titleText='" + titleText + '\'' +
                ", descText='" + descText + '\'' +
                ", documentType=" + documentType +
                ", website='" + website + '\'' +
                '}';
    }
}
