package com.approdevelopers.ranchites.Models;

import com.google.firebase.Timestamp;

public class ReviewModel {
    private String placeId;
    private String userId;
    private String review;
    private double rating;
    private Timestamp uploadTimestamp;

    public ReviewModel() {
    }

    public ReviewModel(String placeId, String userId, String review, double rating, Timestamp uploadTimestamp) {
        this.placeId = placeId;
        this.userId = userId;
        this.review = review;
        this.rating = rating;
        this.uploadTimestamp = uploadTimestamp;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Timestamp getUploadTimestamp() {
        return uploadTimestamp;
    }

    public void setUploadTimestamp(Timestamp uploadTimestamp) {
        this.uploadTimestamp = uploadTimestamp;
    }

    @Override
    public String toString() {
        return "ReviewModel{" +
                "placeId='" + placeId + '\'' +
                ", userId='" + userId + '\'' +
                ", review='" + review + '\'' +
                ", rating=" + rating +
                ", uploadTimestamp=" + uploadTimestamp +
                '}';
    }
}
