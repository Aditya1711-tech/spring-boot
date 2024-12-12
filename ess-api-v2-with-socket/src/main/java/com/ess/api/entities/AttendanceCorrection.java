package com.ess.api.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "attendance_correction")
public class AttendanceCorrection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name =  "date")
    private LocalDate date;

    @Column(name = "remark")
    private String remark;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_status", columnDefinition = "VARCHAR(20) DEFAULT 'PENDING'")
    private Leave.LeaveStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    @JsonBackReference
    private Employee employee;

    public AttendanceCorrection(LocalDate date, String remark, Leave.LeaveStatus status, Employee employee) {
        this.date = date;
        this.remark = remark;
        this.status = status;
        this.employee = employee;
    }

    public AttendanceCorrection() {
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Leave.LeaveStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "AttendanceCorrection{" +
                "id=" + id +
                ", date=" + date +
                ", remark='" + remark + '\'' +
                ", status=" + status +
                ", employee=" + employee +
                '}';
    }

    public void setStatus(Leave.LeaveStatus status) {
        this.status = status;
    }

}
