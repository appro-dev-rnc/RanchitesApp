package com.approdevelopers.ranchites.Models;

public class FeaturedLocation {

    //private variables
    private String title;
    private String category;
    private String imageUrl;
    private String documentId;
    private double overallRating;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public double getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(double overallRating) {
        this.overallRating = overallRating;
    }

    @Override
    public String toString() {
        return "FeaturedLocation{" +
                "title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", documentId='" + documentId + '\'' +
                ", overallRating=" + overallRating +
                '}';
    }
}
