package com.approdevelopers.ranchites.Models;

public class RecentlyViewedDetailedModel {

    private String documentId;
    private String imageUrl;
    private String  title;
    private int documentType;

    public RecentlyViewedDetailedModel() {
    }

    public int getDocumentType() {
        return documentType;
    }

    public void setDocumentType(int documentType) {
        this.documentType = documentType;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "RecentlyViewedDetailedModel{" +
                "documentId='" + documentId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", title='" + title + '\'' +
                ", documentType=" + documentType +
                '}';
    }
}
