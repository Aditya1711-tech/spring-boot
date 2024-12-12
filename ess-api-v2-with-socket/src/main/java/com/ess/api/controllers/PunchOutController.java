package com.ess.api.controllers;

import com.ess.api.entities.Employee;
import com.ess.api.entities.PunchOut;
import com.ess.api.response.ApiResponse;
import com.ess.api.response.Punch;
import com.ess.api.services.EmployeeService;
import com.ess.api.services.PunchInAndOutService;
import com.ess.api.services.PunchOutService;
import com.ess.api.utils.GetCurrentEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/punchOut")
@CrossOrigin(origins = "http://localhost:5173")
public class PunchOutController {

    @Autowired
    private PunchOutService punchOutService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private GetCurrentEmployee getCurrentEmployee;

    @Autowired
    private PunchInAndOutService punchInAndOutService;

    @PostMapping
    public ResponseEntity<?> doPunchOut(Authentication authentication){
        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        List<Punch> todayPunches = punchInAndOutService.getAllPunchesByDateAndEmployee(currentEmployee, LocalDate.now());
        if(!todayPunches.isEmpty() && todayPunches.getLast().isPunchOut()){
            ApiResponse response = new ApiResponse("You are already punchedOut", false);
            return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
        }
        PunchOut punchOut = new PunchOut(currentEmployee);
        PunchOut punchedOut = punchOutService.doPunch(punchOut);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        ApiResponse response = new ApiResponse("Punched out at " + LocalTime.now().format(formatter), true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
