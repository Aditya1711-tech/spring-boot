package com.ess.api.controllers;

import com.ess.api.entities.Employee;
import com.ess.api.entities.Leave;
import com.ess.api.request.AddNote;
import com.ess.api.request.ApproveOrRejectRequest;
import com.ess.api.response.ApiResponse;
import com.ess.api.services.EmployeeService;
import com.ess.api.services.LeaveService;
import com.ess.api.utils.GetCurrentEmployee;
import com.ess.api.utils.SendMail;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/api/leave")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private GetCurrentEmployee getCurrentEmployee;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SendMail sendMail;
    // Add request
    @PostMapping
    public ResponseEntity<Leave> addLeaveRequest(Authentication authentication, @RequestBody Leave leave){
        System.out.println(leave);
        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        leave.setEmployee(currentEmployee);
        long diffInDays = ChronoUnit.DAYS.between(leave.getFrom(), leave.getTo());
        leave.setDays(diffInDays);
        Leave addedLeaveRequest = leaveService.addLeaveRequest(leave);
        return ResponseEntity.ok(addedLeaveRequest);
    }

    // Get all
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllLeaves(Authentication authentication){
        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        if(!currentEmployee.getRole().getName().equalsIgnoreCase("ADMIN")){
            ApiResponse message = new ApiResponse("You are not allowed", false);
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        List<Leave> listOfLeaves = leaveService.getAllLeaves();
//        System.out.println(listOfLeaves);
        return  ResponseEntity.ok(listOfLeaves);
    }

    // Get by Employee
    @GetMapping
    public ResponseEntity<List<Leave>> getAllLeaveRequestsByEmployee(Authentication authentication){
        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        List<Leave> listOfRequests = leaveService.getAllLeaveRequestsByEmployee(currentEmployee);
        listOfRequests = listOfRequests.reversed();
        return ResponseEntity.ok(listOfRequests);
    }

    // Update request
    @PutMapping("/{leaveId}")
    public ResponseEntity<Leave> updateLeaveRequest(@PathVariable Long leaveId, @RequestBody Leave leave){
        Leave updateDLeaveRequest = leaveService.updateLeaveRequest(leaveId , leave);
        return ResponseEntity.ok(updateDLeaveRequest);
    }

    // Approve or Reject leave
    @PutMapping("/changeStatus/{leaveId}")
    public ResponseEntity<?> changeStatus(@PathVariable Long leaveId, @RequestBody ApproveOrRejectRequest approveOrRejectRequest, Authentication authentication) throws MessagingException, IOException {
        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        Employee requester = employeeService.getEmployee(approveOrRejectRequest.getRequestEmployeeId());
        String note = approveOrRejectRequest.getNote().getNote();
        Leave updateDLeaveRequest = leaveService.updateLeaveRequest(leaveId , approveOrRejectRequest.getLeave());

        sendMail.sendApproveOrRejectedRequestMail(requester.getEmail(), updateDLeaveRequest.getStatus().toString(), currentEmployee.getFirstName() + " " + currentEmployee.getLastName(), note);
        return ResponseEntity.ok(updateDLeaveRequest);
    }

    // Get requests according to team
    @GetMapping("/team/{teamId}/getAll")
    public ResponseEntity<?> getAllTheRequestFromGivenTeam(@PathVariable long teamId){
        List<Leave> allTheLeaveRequestsInTeam = leaveService.getLeaveRequestsFromGivenTeam(teamId);
        return ResponseEntity.ok(allTheLeaveRequestsInTeam);
    }
}
