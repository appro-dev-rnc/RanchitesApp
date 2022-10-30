package com.approdevelopers.ranchites.Models;

import com.google.firebase.Timestamp;

public class RecentlyViewedLocation {
    private String documentId;
    private int documentType;
    private Timestamp viewTimestamp;

    public RecentlyViewedLocation() {
    }

    public int getDocumentType() {
        return documentType;
    }

    public void setDocumentType(int documentType) {
        this.documentType = documentType;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Timestamp getViewTimestamp() {
        return viewTimestamp;
    }

    public void setViewTimestamp(Timestamp viewTimestamp) {
        this.viewTimestamp = viewTimestamp;
    }

    @Override
    public String toString() {
        return "RecentlyViewedLocation{" +
                "documentId='" + documentId + '\'' +
                ", documentType=" + documentType +
                ", viewTimestamp=" + viewTimestamp +
                '}';
    }
}
