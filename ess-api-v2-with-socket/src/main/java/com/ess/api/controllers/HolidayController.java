package com.ess.api.controllers;

import com.ess.api.entities.Employee;
import com.ess.api.entities.Holiday;
import com.ess.api.response.ApiResponse;
import com.ess.api.services.HolidayService;
import com.ess.api.utils.GetCurrentEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/holiday")
@CrossOrigin(origins = "http://localhost:5173")
public class HolidayController {

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private GetCurrentEmployee getCurrentEmployee;

    // Add holiday
    @PostMapping
    public ResponseEntity<?> addHoliday(@RequestBody Holiday holiday, Authentication authentication){
        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        if(!currentEmployee.getRole().getName().equalsIgnoreCase("admin")){
            ApiResponse response = new ApiResponse("You are not authorized", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Holiday newAddedHoliday = holidayService.addHoliday(holiday);
        return ResponseEntity.ok(newAddedHoliday);
    }

    // Get all possible years
    @GetMapping("/allYears")
    public ResponseEntity<?> getAllPossibleYears(){
        Set<Integer> allPossibleYears = holidayService.getAllPossibleYears();
        return ResponseEntity.ok(allPossibleYears);
    }

    // Get holidays with given year
    @GetMapping("/{year}")
    public ResponseEntity<?> getAllHolidaysFromYear(@PathVariable int year){
        List<Holiday> listOfHolidaysOfGivenYear = holidayService.getByGivenYear(year);
        return ResponseEntity.ok(listOfHolidaysOfGivenYear);
    }

    // Get all
    @GetMapping
    public ResponseEntity<?> getAllHolidays(){
        List<Holiday> listOfAllHolidays = holidayService.getAllHoliDays();
        return ResponseEntity.ok(listOfAllHolidays);
    }

    @GetMapping("/withId/{holidayId}")
    public ResponseEntity<?> getHolidayById(@PathVariable long holidayId){
        Holiday holiday = holidayService.getHolidayById(holidayId);
        return ResponseEntity.ok(holiday);
    }

    @DeleteMapping("/{holidayId}")
    public ResponseEntity<?> deleteHoliday(@PathVariable long holidayId){
        Holiday holidayToDelete = holidayService.deleteHolidayById(holidayId);
        return ResponseEntity.ok(holidayToDelete);
    }

    @PutMapping("/{holidayId}")
    public ResponseEntity<?> updateHoliday(@PathVariable long holidayId, @RequestBody Holiday holiday){
        Holiday holidayToUpdate = holidayService.updateHolidayById(holidayId, holiday);
        return ResponseEntity.ok(holidayToUpdate);
    }
}
