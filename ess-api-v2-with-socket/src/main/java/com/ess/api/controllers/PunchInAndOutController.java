package com.ess.api.controllers;

import com.ess.api.entities.Employee;
import com.ess.api.entities.PunchIn;
import com.ess.api.entities.PunchOut;
import com.ess.api.repositories.PunchInRepository;
import com.ess.api.request.GetPunchInAndOutRequest;
import com.ess.api.response.DateAndNetMinutes;
import com.ess.api.response.Punch;
import com.ess.api.services.PunchInAndOutService;
import com.ess.api.services.PunchInService;
import com.ess.api.services.PunchOutService;
import com.ess.api.utils.GetCurrentEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/punches")
public class PunchInAndOutController {

    @Autowired
    private PunchInService punchInService;

    @Autowired
    private PunchOutService punchOutService;

    @Autowired
    private GetCurrentEmployee getCurrentEmployee;

    @Autowired
    private PunchInAndOutService punchInAndOutService;

    @PostMapping
    public ResponseEntity<List<Punch>> getAllPunchesOfCurrentUserByDate(Authentication authentication, @RequestBody GetPunchInAndOutRequest getPunchInAndOutRequest ){
        int day = Integer.parseInt(getPunchInAndOutRequest.getDay());
        int month = Integer.parseInt(getPunchInAndOutRequest.getMonth());
        int year = Integer.parseInt(getPunchInAndOutRequest.getYear());

        LocalDate date = LocalDate.of(year, month, day);

        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        List<Punch> allPunchInAndOut = punchInAndOutService.getAllPunchesByDateAndEmployee(currentEmployee, date);
        return ResponseEntity.ok(allPunchInAndOut);
    }

    @GetMapping("/allDates")
    public ResponseEntity<?> getAllDates(Authentication authentication){
        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        Set<LocalDate> allDates = punchInAndOutService.getAllDates(currentEmployee);


        ArrayList<DateAndNetMinutes> dateAndNetMinutes = new ArrayList<>();
        for (LocalDate date:allDates) {
            long netMinutes = punchInAndOutService.countNetMinutes(currentEmployee, date);
            Duration d = Duration.ofMinutes(netMinutes);
            LocalTime time = LocalTime.MIN.plus(d);
            DateAndNetMinutes dnm = new DateAndNetMinutes(date, time);
//            System.out.println(dnm);
            dateAndNetMinutes.add(dnm);
        }

        return ResponseEntity.ok(dateAndNetMinutes);
    }

    // Get today's punches of current employee
    @GetMapping("/today")
    public ResponseEntity<?> getTodayAllPunchesOfCurrentEmployee(Authentication authentication){
        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        List<Punch> todayPunches = punchInAndOutService.getAllPunchesByDateAndEmployee(currentEmployee, LocalDate.now());
        return ResponseEntity.ok(todayPunches);
    }
}
