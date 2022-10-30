package com.approdevelopers.ranchites.Models;

public class UserUpdateModel {
    private String uId;
    private String userName;
    private boolean searchByProfessionEnabled;
    private String userProfession;
    private String userAbout;
    private String userProfileImageUrl;
    private int documentType;

    public UserUpdateModel(String uId, String userName, boolean searchByProfessionEnabled, String userProfession, String userAbout, String userProfileImageUrl, int documentType) {
        this.uId = uId;
        this.userName = userName;
        this.searchByProfessionEnabled = searchByProfessionEnabled;
        this.userProfession = userProfession;
        this.userAbout = userAbout;
        this.userProfileImageUrl = userProfileImageUrl;
        this.documentType = documentType;
    }

    public UserUpdateModel() {

    }


    public int getDocumentType() {
        return documentType;
    }

    public void setDocumentType(int documentType) {
        this.documentType = documentType;
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

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }

    @Override
    public String toString() {
        return "UserUpdateModel{" +
                "uId='" + uId + '\'' +
                ", userName='" + userName + '\'' +
                ", searchByProfessionEnabled=" + searchByProfessionEnabled +
                ", userProfession='" + userProfession + '\'' +
                ", userAbout='" + userAbout + '\'' +
                ", userProfileImageUrl='" + userProfileImageUrl + '\'' +
                ", documentType=" + documentType +
                '}';
    }
}
