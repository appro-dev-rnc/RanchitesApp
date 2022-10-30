package com.approdevelopers.ranchites.Models;

public class CityProfessionalsModel {
    private String uId;
    private String userName;
    private boolean searchByProfessionEnabled;
    private String userProfession;
    private String userAbout;
    private double userRating;
    private String userProfileImageUrl;

    public CityProfessionalsModel() {
    }


    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isSearchByProfessionEnabled() {
        return searchByProfessionEnabled;
    }

    public void setSearchByProfessionEnabled(boolean searchByProfessionEnabled) {
        this.searchByProfessionEnabled = searchByProfessionEnabled;
    }

    public String getUserProfession() {
        return userProfession;
    }

    public void setUserProfession(String userProfession) {
        this.userProfession = userProfession;
    }

    public String getUserAbout() {
        return userAbout;
    }

    public void setUserAbout(String userAbout) {
        this.userAbout = userAbout;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }

    @Override
    public String toString() {
        return "CityProfessionalsModel{" +
                "uId='" + uId + '\'' +
                ", userName='" + userName + '\'' +
                ", searchByProfessionEnabled=" + searchByProfessionEnabled +
                ", userProfession='" + userProfession + '\'' +
                ", userAbout='" + userAbout + '\'' +
                ", userRating=" + userRating +
                ", userProfileImageUrl='" + userProfileImageUrl + '\'' +
                '}';
    }
}
