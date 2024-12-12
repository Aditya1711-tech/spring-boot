package com.ess.api.controllers;

import com.ess.api.entities.Employee;
import com.ess.api.entities.PunchIn;
import com.ess.api.response.ApiResponse;
import com.ess.api.response.Punch;
import com.ess.api.security.services.UserDetailsImpl;
import com.ess.api.services.EmployeeService;
import com.ess.api.services.PunchInAndOutService;
import com.ess.api.services.PunchInService;
import com.ess.api.utils.GetCurrentEmployee;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/punchIn")
@CrossOrigin(origins = "http://localhost:5173")
public class PunchInController {

    @Autowired
    private PunchInService punchInService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private GetCurrentEmployee getCurrentEmployee;

    @Autowired
    private PunchInAndOutService punchInAndOutService;
    // Punch in
    @PostMapping
    public ResponseEntity<?> doPunchIn(Authentication authentication){
        Employee currenEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        List<Punch> todayPunches = punchInAndOutService.getAllPunchesByDateAndEmployee(currenEmployee, LocalDate.now());
        if(!todayPunches.isEmpty() && todayPunches.getLast().isPunchIn()){
            ApiResponse response = new ApiResponse("You are already punchedIn", false);
            return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
        }
        PunchIn punchIn = new PunchIn(currenEmployee);
        PunchIn punchedIn = punchInService.doPunch(punchIn);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        ApiResponse response = new ApiResponse("Punched in at " + LocalTime.now().format(formatter), true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
