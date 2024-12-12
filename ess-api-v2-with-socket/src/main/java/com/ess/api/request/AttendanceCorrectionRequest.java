package com.ess.api.request;

public class AttendanceCorrectionRequest {
    private int year;
    private int month;
    private int day;
    private String remark;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "AttendanceCorrectionRequest{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", remark='" + remark + '\'' +
                '}';
    }
}
