package com.ess.api.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "punchin")
public class PunchIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "punchin_id")
    private long id;

    @Column(name = "punchin_date")
    private LocalDate date;

    @Column(name = "punchin_time")
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public PunchIn(Employee employee){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        this.date = LocalDate.now();
        this.time = LocalTime.now();
        this.employee = employee;
    }

    public PunchIn(LocalDate date, LocalTime time, Employee employee) {
        this.date = date;
        this.time = time;
        this.employee = employee;
    }

    public PunchIn(long id, LocalDate date, LocalTime time, Employee employee) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.employee = employee;
    }

    public PunchIn() {
        super();
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

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "PunchIn [id=" + id + ", date=" + date + ", time=" + time + ", employee=" + employee + "]";
    }

}
