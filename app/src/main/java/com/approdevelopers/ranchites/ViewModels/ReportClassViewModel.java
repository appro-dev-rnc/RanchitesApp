package com.approdevelopers.ranchites.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.approdevelopers.ranchites.Models.ReportModel;
import com.approdevelopers.ranchites.Repository.FirestoreRepository;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ReportClassViewModel extends ViewModel {

    private String currentUserId = null;
    private LiveData<Integer> reportResultCode;
    private String reportReason;
    private String reportDesc;
    private LiveData<List<String>> reportsCategory;
    private FirebaseUser currentUser;

    public ReportClassViewModel(){
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!=null){
            currentUserId = currentUser.getUid();

        }
        reportResultCode =null;
        reportsCategory = null;
        reportReason = null;
        reportDesc = null;
    }

    public LiveData<List<String>> getReportsCategory(int documentType) {
        reportsCategory = FirestoreRepository.getInstance().getReportsCategory(documentType);
        return reportsCategory;
    }

    public LiveData<Integer> addToReports(int documentType, String placeId, String reportReason, String reportDesc){

        ReportModel reportModel = new ReportModel(documentType,placeId,currentUserId,reportReason,reportDesc, Timestamp.now());
        reportResultCode = FirestoreRepository.getInstance().addToReports(reportModel);
        return reportResultCode;
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
}
