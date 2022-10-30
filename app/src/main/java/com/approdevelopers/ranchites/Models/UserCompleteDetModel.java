package com.approdevelopers.ranchites.Models;

public class UserCompleteDetModel {

    private String uId;
    private String userName ;
    private String userPhone;
    private String userEmail;
    private String userProfileImageUrl;
    private boolean searchByProfessionEnabled;
    private String userProfession;
    private String userAbout;
    private double userRating;

    public UserCompleteDetModel() {
    }

    public UserCompleteDetModel(String uId, String userName, String userPhoneNo, String userEmail, String userProfileImageUrl, boolean searchByProfessionEnabled, String userProfession, String userAbout, double userRating) {
        this.uId = uId;
        this.userName = userName;
        this.userPhone = userPhoneNo;
        this.userEmail = userEmail;
        this.userProfileImageUrl = userProfileImageUrl;
        this.searchByProfessionEnabled = searchByProfessionEnabled;
        this.userProfession = userProfession;
        this.userAbout = userAbout;
        this.userRating = userRating;
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

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }

    public boolean getSearchByProfessionEnabled() {
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

    @Override
    public String toString() {
        return "UserCompleteDetModel{" +
                "uId='" + uId + '\'' +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userProfileImageUrl='" + userProfileImageUrl + '\'' +
                ", searchByProfessionEnabled=" + searchByProfessionEnabled +
                ", userProfession='" + userProfession + '\'' +
                ", userAbout='" + userAbout + '\'' +
                ", userRating=" + userRating +
                '}';
    }
}
