package com.ess.api.controllers;

import com.ess.api.entities.AttendanceCorrection;
import com.ess.api.entities.Employee;
import com.ess.api.entities.Leave;
import com.ess.api.request.AttendanceCorrectionRequest;
import com.ess.api.request.UpdateCorrectionStatusRequest;
import com.ess.api.response.ApiResponse;
import com.ess.api.services.AttendanceCorrectionService;
import com.ess.api.utils.GetCurrentEmployee;
import jakarta.mail.MessagingException;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/correction")
@CrossOrigin(origins = "http://localhost:5173")
public class AttendanceCorrectionController {

    @Autowired
    private AttendanceCorrectionService attendanceCorrectionService;

    @Autowired
    private GetCurrentEmployee getCurrentEmployee;

    // Add request
    @PostMapping
    public ResponseEntity<?> addRequest(Authentication authentication,@RequestBody AttendanceCorrectionRequest attendanceCorrectionRequest){
        Employee currentEmployee =  getCurrentEmployee.getCurrentEmployee(authentication);
        ApiResponse response = attendanceCorrectionService.addRequest(currentEmployee, attendanceCorrectionRequest);
        if(!response.isSuccess()){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Get all
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(Authentication authentication){
        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        if(!currentEmployee.getRole().getName().equalsIgnoreCase("admin") &&  !currentEmployee.getRole().getName().equalsIgnoreCase("manager")){
            ApiResponse response = new ApiResponse("You are not authorized", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        List<AttendanceCorrection> list = attendanceCorrectionService.getAll();
        return ResponseEntity.ok(list);
    }

    // Update status
    @PutMapping("/updateStatus/{correctionId}")
    public ResponseEntity<?> updateStatus(Authentication authentication, @PathVariable long correctionId, @RequestBody UpdateCorrectionStatusRequest updateCorrectionStatusRequest) throws MessagingException, IOException {
        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        if(!currentEmployee.getRole().getName().equalsIgnoreCase("admin") &&  !currentEmployee.getRole().getName().equalsIgnoreCase("manager")){
            ApiResponse response = new ApiResponse("You are not authorized", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        AttendanceCorrection updatedAttendanceCorrection = attendanceCorrectionService.updateStatus(updateCorrectionStatusRequest.getAttendanceCorrection().getStatus(), correctionId, updateCorrectionStatusRequest.getAddNote(), currentEmployee);
        return ResponseEntity.ok(updatedAttendanceCorrection);
    }

}
