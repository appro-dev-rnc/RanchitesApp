package com.approdevelopers.ranchites.Models;

public class BestRatedLocation {

    private String title;
    private String imageUrl;
    private String documentId;
    private double overallRating;
    private String category;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BestRatedLocation() {
    }

    @Override
    public String toString() {
        return "BestRatedLocation{" +
                "title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", documentId='" + documentId + '\'' +
                ", overallRating=" + overallRating +
                ", category='" + category + '\'' +
                '}';
    }
}
