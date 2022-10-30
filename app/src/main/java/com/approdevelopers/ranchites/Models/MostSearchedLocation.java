package com.approdevelopers.ranchites.Models;

public class MostSearchedLocation {

    private String title;
    private String category;
    private String imageUrl;
    private double overallRating;
    private int searchCount;
    private String documentId;

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

    public double getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(double overallRating) {
        this.overallRating = overallRating;
    }

    public int getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }


    @Override
    public String toString() {
        return "MostSearchedLocation{" +
                "title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", overallRating=" + overallRating +
                ", searchCount=" + searchCount +
                ", documentId='" + documentId + '\'' +
                '}';
    }
}
