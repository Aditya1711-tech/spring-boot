package com.ess.api.response;

import com.ess.api.entities.Employee;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Punch {
    private long id;
    private LocalDate date;
    private LocalTime time;
    private Employee employee;
    private boolean isPunchIn;
    private boolean isPunchOut;

    public Punch() {
        super();
    }

    public Punch(long id, LocalDate date, LocalTime time, Employee employee, boolean isPunchIn, boolean isPunchOut) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        this.id = id;
        this.date = date;
        this.time = LocalTime.parse(time.format(formatter));
        this.employee = employee;
        this.isPunchIn = isPunchIn;
        this.isPunchOut = isPunchOut;
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

    public boolean isPunchIn() {
        return isPunchIn;
    }

    public void setPunchIn(boolean punchIn) {
        isPunchIn = punchIn;
    }

    public boolean isPunchOut() {
        return isPunchOut;
    }

    public void setPunchOut(boolean punchOut) {
        isPunchOut = punchOut;
    }

    @Override
    public String toString() {
        return "Punch{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + time +
                ", employee=" + employee +
                ", isPunchIn=" + isPunchIn +
                ", isPunchOut=" + isPunchOut +
                '}';
    }
}
