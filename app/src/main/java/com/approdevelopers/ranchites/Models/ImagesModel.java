package com.approdevelopers.ranchites.Models;

public class ImagesModel {
    private String documentId;
    private String imageUrl;
    private String storagePath;

    public ImagesModel() {
    }



    public ImagesModel(String documentId, String imageUrl, String storagePath) {
        this.documentId = documentId;
        this.imageUrl = imageUrl;
        this.storagePath = storagePath;
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

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    @Override
    public String toString() {
        return "ImagesModel{" +
                "documentId='" + documentId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", storagePath='" + storagePath + '\'' +
                '}';
    }
}
