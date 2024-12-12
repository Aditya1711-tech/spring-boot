package com.ess.api.services;

import com.ess.api.entities.Employee;
import com.ess.api.entities.Leave;
import com.ess.api.exceptions.ResourceNotFoundException;
import com.ess.api.repositories.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private EmployeeService employeeService;

    // Add request
    public Leave addLeaveRequest(Leave leave){
        return leaveRepository.save(leave);
    }

    // Get all
    public List<Leave> getAllLeaves(){
        return leaveRepository.findAll();
    }

    // Get by Employee
    public List<Leave> getAllLeaveRequestsByEmployee(Employee employee){
        return leaveRepository.findByEmployee(employee).orElseThrow(() -> new ResourceNotFoundException("Leave request", "User name", employee.getEmail()));
    }

    //Get by id
    public Leave getLeaveRequestById(Long leaveId){
        return leaveRepository.findById(leaveId).orElseThrow(() -> new ResourceNotFoundException("Leave request", "Leave id", leaveId.toString()));
    }

    // Update
    public Leave updateLeaveRequest(Long leaveId, Leave leave){
        Leave leaveReq = this.getLeaveRequestById(leaveId);
        leaveReq.setFrom(leave.getFrom());
        leaveReq.setTo(leave.getTo());
        long diffInDays = ChronoUnit.DAYS.between(leave.getFrom(), leave.getTo());
        leaveReq.setDays(diffInDays);
        leaveReq.setStatus(leave.getStatus());
        leaveReq.setReason(leave.getReason());
        leaveReq.setType(leave.getType());
        return leaveRepository.save(leaveReq);
    }

    // Get requests according to team
    public List<Leave> getLeaveRequestsFromGivenTeam(Long teamId){
        List<Employee> allEmployees = employeeService.getAllEmployees();
        List<Employee> employeesWithGivenTeam = allEmployees.stream().filter(employee -> employee.getTeam().getId() == teamId).toList();
        List<Leave> allTheLeaveRequestsInTeam = new ArrayList<>();
        employeesWithGivenTeam.forEach(employee -> {
            allTheLeaveRequestsInTeam.addAll(employee.getLeaves());
        });
        return allTheLeaveRequestsInTeam;
    }
}
