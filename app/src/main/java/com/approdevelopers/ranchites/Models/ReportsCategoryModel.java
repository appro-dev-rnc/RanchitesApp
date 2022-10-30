package com.approdevelopers.ranchites.Models;

public class ReportsCategoryModel {
    private String documentId;
    private String reportName;
    private int reportType;


    public ReportsCategoryModel(String documentId, String reportName, int reportType) {
        this.documentId = documentId;
        this.reportName = reportName;
        this.reportType = reportType;
    }

    public ReportsCategoryModel() {
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public int getReportType() {
        return reportType;
    }

    public void setReportType(int reportType) {
        this.reportType = reportType;
    }

    @Override
    public String toString() {
        return "ReportsCategoryModel{" +
                "documentId='" + documentId + '\'' +
                ", reportName='" + reportName + '\'' +
                ", reportType=" + reportType +
                '}';
    }
}
