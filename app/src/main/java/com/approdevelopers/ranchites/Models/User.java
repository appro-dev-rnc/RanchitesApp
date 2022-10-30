package com.approdevelopers.ranchites.Models;

public class User {

    private String uId;
    private String userName ;
    private String userPhone;
    private String userEmail;
    private String userPassword;
    private String userProfileImageUrl;
    private boolean searchByProfessionEnabled;
    private String userProfession;
    private String userAbout;
    private int documentType;

    public User() {
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

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }


    public User(String uId, String userName, String userPhone, String userEmail, String userPassword, String userProfileImageUrl, boolean searchByProfessionEnabled, String userProfession, String userAbout, int documentType) {
        this.uId = uId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userProfileImageUrl = userProfileImageUrl;
        this.searchByProfessionEnabled = searchByProfessionEnabled;
        this.userProfession = userProfession;
        this.userAbout = userAbout;
        this.documentType = documentType;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    @Override
    public String toString() {
        return "User{" +
                "uId='" + uId + '\'' +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userProfileImageUrl='" + userProfileImageUrl + '\'' +
                ", searchByProfessionEnabled=" + searchByProfessionEnabled +
                ", userProfession='" + userProfession + '\'' +
                ", userAbout='" + userAbout + '\'' +
                ", documentType=" + documentType +
                '}';
    }
}
