package com.example.nive_pt1681.recipeapplication.model;

import java.util.UUID;

/**
 * Created by nive-pt1681 on 19/03/18.
 */

public class Report {
    private UUID reportId;
    private String reportImage;
    private String reportedUserImage;
    private String reportedUserName;
    private String reportedProblem;
    private String reportedRegarding;
    private long reportedDate;
    private int vulnerability;
    private String recipeId;

    public UUID getReportId() {
        return reportId;
    }

    public void setReportId(UUID reportId) {
        this.reportId = reportId;
    }

    public String getReportImage() {
        return reportImage;
    }

    public void setReportImage(String reportImage) {
        this.reportImage = reportImage;
    }

    public String getReportedUserName() {
        return reportedUserName;
    }

    public void setReportedUserName(String reportedUserName) {
        this.reportedUserName = reportedUserName;
    }

    public String getReportedProblem() {
        return reportedProblem;
    }

    public void setReportedProblem(String reportedProblem) {
        this.reportedProblem = reportedProblem;
    }

    public String getReportedRegarding() {
        return reportedRegarding;
    }

    public void setReportedRegarding(String reportedRegarding) {
        this.reportedRegarding = reportedRegarding;
    }

    public long getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(long reportedDate) {
        this.reportedDate = reportedDate;
    }

    public int getVulnerability() {
        return vulnerability;
    }

    public void setVulnerability(int vulnerability) {
        this.vulnerability = vulnerability;
    }
    public String getReportedUserImage() {
        return reportedUserImage;
    }

    public void setReportedUserImage(String reportedUserImage) {
        this.reportedUserImage = reportedUserImage;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }
}
