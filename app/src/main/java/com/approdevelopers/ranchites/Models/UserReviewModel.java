package com.approdevelopers.ranchites.Models;

import com.google.firebase.Timestamp;

public class UserReviewModel {
    private String uId;
    private String userId;
    private String review;
    private double rating;
    private Timestamp reviewTimestamp;

    public UserReviewModel() {
    }

    public UserReviewModel(String uId, String userId, String review, double rating, Timestamp reviewTimestamp) {
        this.uId = uId;
        this.userId = userId;
        this.review = review;
        this.rating = rating;
        this.reviewTimestamp = reviewTimestamp;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
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

    public Timestamp getReviewTimestamp() {
        return reviewTimestamp;
    }

    public void setReviewTimestamp(Timestamp reviewTimestamp) {
        this.reviewTimestamp = reviewTimestamp;
    }

    @Override
    public String toString() {
        return "UserReviewModel{" +
                "uId='" + uId + '\'' +
                ", userId='" + userId + '\'' +
                ", review='" + review + '\'' +
                ", rating=" + rating +
                ", reviewTimestamp=" + reviewTimestamp +
                '}';
    }
}
