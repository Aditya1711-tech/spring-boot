package com.ess.api.utils;

import com.ess.api.entities.Employee;
import com.ess.api.repositories.EmployeeRepository;
import com.ess.api.security.services.UserDetailsImpl;
import com.ess.api.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class GetCurrentEmployee {

    @Autowired
    private EmployeeService employeeService;

    public Employee getCurrentEmployee(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String empEmail = userDetails.getEmail();
        return employeeService.getEmployeeByEmail(empEmail);
    }
}
