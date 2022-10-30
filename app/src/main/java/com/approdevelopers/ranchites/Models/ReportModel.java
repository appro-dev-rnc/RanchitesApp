package com.approdevelopers.ranchites.Models;

import com.google.firebase.Timestamp;

public class ReportModel {
    private int documentType;
    private String placeId;
    private String userId;
    private String reportReason;
    private String reportDesc;
    private Timestamp reportTimestamp;

    public ReportModel() {
    }

    public ReportModel(int documentType,String placeId, String userId, String reportReason, String reportDesc, Timestamp reportTimestamp) {
        this.placeId = placeId;
        this.userId = userId;
        this.reportReason = reportReason;
        this.reportDesc = reportDesc;
        this.reportTimestamp = reportTimestamp;
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

    public String getReportReason() {
        return reportReason;
    }

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }

    public String getReportDesc() {
        return reportDesc;
    }

    public void setReportDesc(String reportDesc) {
        this.reportDesc = reportDesc;
    }

    public Timestamp getReportTimestamp() {
        return reportTimestamp;
    }

    public void setReportTimestamp(Timestamp reportTimestamp) {
        this.reportTimestamp = reportTimestamp;
    }

    public int getDocumentType() {
        return documentType;
    }

    public void setDocumentType(int documentType) {
        this.documentType = documentType;
    }

    @Override
    public String toString() {
        return "ReportModel{" +
                "documentType=" + documentType +
                ", placeId='" + placeId + '\'' +
                ", userId='" + userId + '\'' +
                ", reportReason='" + reportReason + '\'' +
                ", reportDesc='" + reportDesc + '\'' +
                ", reportTimestamp=" + reportTimestamp +
                '}';
    }
}
