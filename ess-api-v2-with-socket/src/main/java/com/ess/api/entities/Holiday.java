package com.ess.api.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Entity
@Table(name = "holidays")
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "holiday_id")
    private long id;

    @Column(name = "holiday_date")
    private LocalDate date;

    @Column(name = "holiday_day")
    private DayOfWeek day;

    @Column(name = "holiday_name")
    private String name;

    public Holiday() {
        super();
    }

    @JsonCreator
    public Holiday(@JsonProperty("date") LocalDate date,@JsonProperty("name") String name) {
        this.date = date;
        this.day = date.getDayOfWeek();
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Holiday{" +
                "id=" + id +
                ", date=" + date +
                ", day=" + day +
                ", name='" + name + '\'' +
                '}';
    }
}
