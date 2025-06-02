package com.sk.system.model;

import java.time.LocalDate;

/**
 * Educational Assistance model class.
 */
public class EducationalAssistance {
    private int id;
    private int memberId;
    private String assistanceType;
    private double amount;
    private LocalDate dateGiven;
    private String remarks;

    // New fields
    private String schoolName;
    private String educationLevel;

    // Constructors
    public EducationalAssistance() {}

    public EducationalAssistance(int id, int memberId, String assistanceType, double amount, LocalDate dateGiven,
                                 String remarks, String schoolName, String educationLevel) {
        this.id = id;
        this.memberId = memberId;
        this.assistanceType = assistanceType;
        this.amount = amount;
        this.dateGiven = dateGiven;
        this.remarks = remarks;
        this.schoolName = schoolName;
        this.educationLevel = educationLevel;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getAssistanceType() {
        return assistanceType;
    }

    public void setAssistanceType(String assistanceType) {
        this.assistanceType = assistanceType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDateGiven() {
        return dateGiven;
    }

    public void setDateGiven(LocalDate dateGiven) {
        this.dateGiven = dateGiven;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    @Override
    public String toString() {
        return "EducationalAssistance{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", assistanceType='" + assistanceType + '\'' +
                ", amount=" + amount +
                ", dateGiven=" + dateGiven +
                ", remarks='" + remarks + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", educationLevel='" + educationLevel + '\'' +
                '}';
    }
}
