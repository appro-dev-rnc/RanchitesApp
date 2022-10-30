package com.approdevelopers.ranchites.Models;

public class UserBasicInfo {

    private String uId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String userProfileImageUrl;


    //no arg constructor
    public UserBasicInfo() {
    }

    //constructor with all arguments
    public UserBasicInfo(String uId, String userName, String userEmail, String userPhone,String userProfileImageUrl) {
        this.uId = uId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userProfileImageUrl = userProfileImageUrl;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }

    @Override
    public String toString() {
        return "UserBasicInfo{" +
                "uId='" + uId + '\'' +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userProfileImageUrl='" + userProfileImageUrl + '\'' +
                '}';
    }
}
