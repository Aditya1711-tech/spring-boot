package com.ess.api.services;

import com.ess.api.entities.Employee;
import com.ess.api.entities.EmployeePersonalDetails;
import com.ess.api.exceptions.ResourceNotFoundException;
import com.ess.api.repositories.EmployeePersonalDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeePersonalDetailsService {

    @Autowired
    private EmployeePersonalDetailsRepository employeePersonalDetailsRepository;

    // Add personal details
    public EmployeePersonalDetails addDetails(Employee currentEmployee, EmployeePersonalDetails employeePersonalDetails){
        EmployeePersonalDetails existingEmployeePersonalDetails = employeePersonalDetailsRepository.findEmployeePersonalDetailsByEmployee(currentEmployee);
        if(existingEmployeePersonalDetails != null){
            return this.updateDetails(existingEmployeePersonalDetails, employeePersonalDetails);
        }
        employeePersonalDetails.setEmployee(currentEmployee);
        return employeePersonalDetailsRepository.save(employeePersonalDetails);
    }

    // Get by id
    public EmployeePersonalDetails getById(Long personalDetailsId){
        return employeePersonalDetailsRepository.findById(personalDetailsId).orElseThrow(() -> new ResourceNotFoundException("Personal details","personalDetailsId",""+personalDetailsId));
    }

    // Get by employee
    public EmployeePersonalDetails getByEmployee(Employee employee){
        EmployeePersonalDetails existingEmployeePersonalDetails = employeePersonalDetailsRepository.findEmployeePersonalDetailsByEmployee(employee);
        if(existingEmployeePersonalDetails == null){
            throw new ResourceNotFoundException("Employee personal details","Employee",employee.getFirstName() + " " + employee.getLastName());
        }
        return existingEmployeePersonalDetails;
    }

    // Update by id
    public EmployeePersonalDetails updateById(Long personalDetailsId, EmployeePersonalDetails employeePersonalDetails){
        EmployeePersonalDetails exiatingEmployeePersonalDetails = this.getById(personalDetailsId);
        return updateDetails(exiatingEmployeePersonalDetails, employeePersonalDetails);
    }

    // Update by employee
    public EmployeePersonalDetails UpdateByEmployee(Employee employee, EmployeePersonalDetails employeePersonalDetails){
        EmployeePersonalDetails existingEmployeePersonalDetails = employeePersonalDetailsRepository.findEmployeePersonalDetailsByEmployee(employee);
        if(existingEmployeePersonalDetails == null){
            throw new ResourceNotFoundException("Employee personal details","Employee",employee.getFirstName() + " " + employee.getLastName());
        }
        return this.updateDetails(existingEmployeePersonalDetails, employeePersonalDetails);
    }

    // Update personalDetails
    public EmployeePersonalDetails updateDetails(EmployeePersonalDetails existing, EmployeePersonalDetails upComing) {
        if (upComing.getPersonalEmail() != null) {
            existing.setPersonalEmail(upComing.getPersonalEmail());
        }
        if (upComing.getPersonalMobile() != null) {
            existing.setPersonalMobile(upComing.getPersonalMobile());
        }
        if (upComing.getDateOfBirth() != null) {
            existing.setDateOfBirth(upComing.getDateOfBirth());
        }
        if (upComing.getEmergencyContactName1() != null) {
            existing.setEmergencyContactName1(upComing.getEmergencyContactName1());
        }
        if (upComing.getEmergencyContactNumber1() != null) {
            existing.setEmergencyContactNumber1(upComing.getEmergencyContactNumber1());
        }
        if (upComing.getEmergencyContactName2() != null) {
            existing.setEmergencyContactName2(upComing.getEmergencyContactName2());
        }
        if (upComing.getEmergencyContactNumber2() != null) {
            existing.setEmergencyContactNumber2(upComing.getEmergencyContactNumber2());
        }
        if (upComing.getCurrentCountry() != null) {
            existing.setCurrentCountry(upComing.getCurrentCountry());
        }
        if (upComing.getCurrentState() != null) {
            existing.setCurrentState(upComing.getCurrentState());
        }
        if (upComing.getCurrentCity() != null) {
            existing.setCurrentCity(upComing.getCurrentCity());
        }
        if (upComing.getCurrentPinCode() != null) {
            existing.setCurrentPinCode(upComing.getCurrentPinCode());
        }
        if (upComing.getCurrentAddress() != null) {
            existing.setCurrentAddress(upComing.getCurrentAddress());
        }
        if (upComing.getPermanentCountry() != null) {
            existing.setPermanentCountry(upComing.getPermanentCountry());
        }
        if (upComing.getPermanentState() != null) {
            existing.setPermanentState(upComing.getPermanentState());
        }
        if (upComing.getPermanentCity() != null) {
            existing.setPermanentCity(upComing.getPermanentCity());
        }
        if (upComing.getPermanentPinCode() != null) {
            existing.setPermanentPinCode(upComing.getPermanentPinCode());
        }
        if (upComing.getPermanentAddress() != null) {
            existing.setPermanentAddress(upComing.getPermanentAddress());
        }
        return employeePersonalDetailsRepository.save(existing);
    }
}
