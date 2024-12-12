package com.ess.api.response;

import java.time.LocalDate;
import java.time.LocalTime;

public class EmployeeAnalysisResponse {
    private String monthlyNetMinutes;
    private long totalActiveDays;
    private LocalTime averageWorkMinutes;

    public EmployeeAnalysisResponse(String monthlyNetMinutes, long totalActiveDays, LocalTime averageWorkMinutes) {
        this.monthlyNetMinutes = monthlyNetMinutes;
        this.totalActiveDays = totalActiveDays;
        this.averageWorkMinutes = averageWorkMinutes;
    }

    public EmployeeAnalysisResponse() {
    }

    public String getMonthlyNetMinutes() {
        return monthlyNetMinutes;
    }

    public void setMonthlyNetMinutes(String monthlyNetMinutes) {
        this.monthlyNetMinutes = monthlyNetMinutes;
    }

    public long getTotalActiveDays() {
        return totalActiveDays;
    }

    public void setTotalActiveDays(long totalActiveDays) {
        this.totalActiveDays = totalActiveDays;
    }

    public LocalTime getAverageWorkHours() {
        return averageWorkMinutes;
    }

    public void setAverageWorkHours(LocalTime averageWorkMinutes) {
        this.averageWorkMinutes = averageWorkMinutes;
    }

    @Override
    public String toString() {
        return "EmployeeAnalysisResponse{" +
                "monthlyNetMinutes=" + monthlyNetMinutes +
                ", totalActiveDays=" + totalActiveDays +
                ", averageWorkMinutes=" + averageWorkMinutes +
                '}';
    }
}
