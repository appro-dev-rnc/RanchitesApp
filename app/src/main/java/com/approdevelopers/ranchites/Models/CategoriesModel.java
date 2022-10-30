package com.approdevelopers.ranchites.Models;

public class CategoriesModel {
    private String categoryName;
    private String categoryImageUrl;
    private String documentId;
    private int categoryType;

    public CategoriesModel() {
    }

    public CategoriesModel(String categoryName, String categoryImageUrl, String documentId, int categoryType) {
        this.categoryName = categoryName;
        this.categoryImageUrl = categoryImageUrl;
        this.documentId = documentId;
        this.categoryType = categoryType;
    }



    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImageUrl() {
        return categoryImageUrl;
    }

    public void setCategoryImageUrl(String categoryImageUrl) {
        this.categoryImageUrl = categoryImageUrl;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    @Override
    public String toString() {
        return "CategoriesModel{" +
                "categoryName='" + categoryName + '\'' +
                ", categoryImageUrl='" + categoryImageUrl + '\'' +
                ", documentId='" + documentId + '\'' +
                ", categoryType=" + categoryType +
                '}';
    }
}
