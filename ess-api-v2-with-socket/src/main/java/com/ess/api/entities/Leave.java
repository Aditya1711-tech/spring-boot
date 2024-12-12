package com.ess.api.entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Table(name = "leaves")
public class Leave {

    public enum LeaveType {
        PAID, UNPAID;

    }
    public enum LeaveStatus {
        PENDING, APPROVED, REJECTED;

    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name =  "leave_type", nullable = false)
    private LeaveType type;

    @Column(name = "leave_reason")
    private String reason;

    @Column(name =  "leave_from")
    private LocalDate from;

    @Column(name = "leave_to")
    private LocalDate to;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    @JsonBackReference
    private Employee employee;

    @Column(name = "leave_days", columnDefinition = "BIGINT DEFAULT 1")
    private long days;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_status", columnDefinition = "VARCHAR(20) DEFAULT 'PENDING'")
    private LeaveStatus status;

    @JsonCreator
    public Leave(@JsonProperty("from") LocalDate from,
                 @JsonProperty("to") LocalDate to,
                 @JsonProperty("reason") String reason,
                 @JsonProperty("type") LeaveType type){
        this.from = from;
        this.to = to;
        this.reason = reason;
        this.type = type;
        this.status = LeaveStatus.PENDING;
    }

    public Leave(long id, LeaveType type, String reason, LocalDate from, LocalDate to, Employee employee, long days, LeaveStatus status) {
        this.id = id;
        this.type = type;
        this.reason = reason;
        this.from = from;
        this.to = to;
        this.employee = employee;
        this.days = days;
        this.status = status;
    }

    public Leave() {
        super();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LeaveType getType() {
        return type;
    }

    public void setType(LeaveType type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Leave [id=" + id + ", type=" + type + ", from=" + from + ", to=" + to + ", employee=" + employee
                + ", days=" + days + ", status=" + status + "]";
    }


    public long calculateTotalLeaveDays(LocalDate from, LocalDate to) {
        return ChronoUnit.DAYS.between(from, to);
    }

}
