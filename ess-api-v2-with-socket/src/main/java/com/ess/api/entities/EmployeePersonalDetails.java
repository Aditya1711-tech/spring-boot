package com.ess.api.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "employee_personal_details")
public class EmployeePersonalDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "personal_email")
    private String personalEmail;

    @Column(name = "personal_mobile")
    private String personalMobile;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "emergency_contact_name_1")
    private String emergencyContactName1;

    @Column(name = "emergency_contact_number_1")
    private Long emergencyContactNumber1;

    @Column(name = "emergency_contact_name_2")
    private String emergencyContactName2;

    @Column(name = "emergency_contact_number_2")
    private Long emergencyContactNumber2;

    @Column(name = "current_country")
    private String currentCountry;

    @Column(name = "current_state")
    private String currentState;

    @Column(name = "current_city")
    private String currentCity;

    @Column(name = "current_pin_code")
    private Long currentPinCode;

    @Column(name = "current_address")
    private String currentAddress;

    @Column(name = "permanent_country")
    private String permanentCountry;

    @Column(name = "permanent_state")
    private String permanentState;

    @Column(name = "permanent_city")
    private String permanentCity;

    @Column(name = "permanent_pin_code")
    private Long permanentPinCode;

    @Column(name = "permanent_address")
    private String permanentAddress;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    // getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    public String getPersonalMobile() {
        return personalMobile;
    }

    public void setPersonalMobile(String personalMobile) {
        this.personalMobile = personalMobile;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmergencyContactName1() {
        return emergencyContactName1;
    }

    public void setEmergencyContactName1(String emergencyContactName1) {
        this.emergencyContactName1 = emergencyContactName1;
    }

    public Long getEmergencyContactNumber1() {
        return emergencyContactNumber1;
    }

    public void setEmergencyContactNumber1(Long emergencyContactNumber1) {
        this.emergencyContactNumber1 = emergencyContactNumber1;
    }

    public String getEmergencyContactName2() {
        return emergencyContactName2;
    }

    public void setEmergencyContactName2(String emergencyContactName2) {
        this.emergencyContactName2 = emergencyContactName2;
    }

    public Long getEmergencyContactNumber2() {
        return emergencyContactNumber2;
    }

    public void setEmergencyContactNumber2(Long emergencyContactNumber2) {
        this.emergencyContactNumber2 = emergencyContactNumber2;
    }

    public String getCurrentCountry() {
        return currentCountry;
    }

    public void setCurrentCountry(String currentCountry) {
        this.currentCountry = currentCountry;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public Long getCurrentPinCode() {
        return currentPinCode;
    }

    public void setCurrentPinCode(Long currentPinCode) {
        this.currentPinCode = currentPinCode;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getPermanentCountry() {
        return permanentCountry;
    }

    public void setPermanentCountry(String permanentCountry) {
        this.permanentCountry = permanentCountry;
    }

    public String getPermanentState() {
        return permanentState;
    }

    public void setPermanentState(String permanentState) {
        this.permanentState = permanentState;
    }

    public String getPermanentCity() {
        return permanentCity;
    }

    public void setPermanentCity(String permanentCity) {
        this.permanentCity = permanentCity;
    }

    public Long getPermanentPinCode() {
        return permanentPinCode;
    }

    public void setPermanentPinCode(Long permanentPinCode) {
        this.permanentPinCode = permanentPinCode;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "EmployeePersonalDetails{" +
                "id=" + id +
                ", personalEmail='" + personalEmail + '\'' +
                ", personalMobile='" + personalMobile + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", emergencyContactName1='" + emergencyContactName1 + '\'' +
                ", emergencyContactNumber1=" + emergencyContactNumber1 +
                ", emergencyContactName2='" + emergencyContactName2 + '\'' +
                ", emergencyContactNumber2=" + emergencyContactNumber2 +
                ", currentCountry='" + currentCountry + '\'' +
                ", currentState='" + currentState + '\'' +
                ", currentCity='" + currentCity + '\'' +
                ", currentPinCode=" + currentPinCode +
                ", currentAddress='" + currentAddress + '\'' +
                ", permanentCountry='" + permanentCountry + '\'' +
                ", permanentState='" + permanentState + '\'' +
                ", permanentCity='" + permanentCity + '\'' +
                ", permanentPinCode=" + permanentPinCode +
                ", permanentAddress='" + permanentAddress + '\'' +
                ", employee=" + employee +
                '}';
    }
}
