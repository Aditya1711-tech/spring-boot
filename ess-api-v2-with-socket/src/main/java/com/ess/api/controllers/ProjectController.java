package com.ess.api.controllers;

import com.ess.api.entities.Employee;
import com.ess.api.entities.Project;
import com.ess.api.entities.ProjectLog;
import com.ess.api.request.AddProjectMemberRequest;
import com.ess.api.response.ApiResponse;
import com.ess.api.services.EmployeeService;
import com.ess.api.services.ProjectService;
import com.ess.api.utils.GetCurrentEmployee;
import com.ess.api.utils.SendMail;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/project")
@CrossOrigin(origins = "http://localhost:5173")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private GetCurrentEmployee getCurrentEmployee;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SendMail sendMail;

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody Project project){
        ProjectLog createdLog = projectService.addProject(project);
        return ResponseEntity.ok(createdLog);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getALlProjects(Authentication authentication) throws InterruptedException {
        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        /*if(!currentEmployee.getRole().getName().equalsIgnoreCase("admin")){
            ApiResponse response = new ApiResponse("You are not authorized", false);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }*/
        List<Project> allProjects = projectService.getAllProjects();
        return ResponseEntity.ok(allProjects);
    }

    // Get by id
    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable long projectId){
        Project project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(project);
    }

    // Add project member
    @PostMapping("/addMember")
    public ResponseEntity<?> addProjectMember(@RequestBody AddProjectMemberRequest addProjectMemberRequest, Authentication authentication) throws MessagingException, IOException {
        ProjectLog addMemberLog = projectService.addEmployeeToProject(addProjectMemberRequest.getProjectId(), addProjectMemberRequest.getEmployeeId(), addProjectMemberRequest.getRole());
        Employee assignBy = getCurrentEmployee.getCurrentEmployee(authentication);
        Employee assignTo = employeeService.getEmployee(addProjectMemberRequest.getEmployeeId());

        sendMail.sedNewMemberMail(assignTo.getEmail(), assignBy.getFirstName() + " " + assignBy.getLastName(), addMemberLog.getProject().getName(), addMemberLog.getProject().getId());
        return ResponseEntity.ok(addMemberLog);
    }

    // Get project from given taskId
    @GetMapping("/getProjectWithTask/{taskId}")
    public ResponseEntity<?> getProjectFromGivenTaskId(@PathVariable long taskId){
        Project projectWithGivenTask = projectService.getProjectFromProjectTask(taskId);
        return ResponseEntity.ok(projectWithGivenTask);
    }
}
