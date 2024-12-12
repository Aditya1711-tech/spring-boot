package com.ess.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateAndNetMinutes {
    private LocalDate date;
    private LocalTime netHours;

    public DateAndNetMinutes(LocalDate date, LocalTime netHours) {
        this.date = date;
        this.netHours = netHours;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getNetHours() {
        return netHours;
    }

    public void setNetHours(LocalTime netHours) {
        this.netHours = netHours;
    }

    @Override
    public String toString() {
        return "DateAndNetMinutes{" +
                "date=" + date +
                ", netHours=" + netHours +
                '}';
    }
}
