package com.ess.api.controllers;

import com.ess.api.entities.Employee;
import com.ess.api.entities.Project;
import com.ess.api.response.ApiResponse;
import com.ess.api.services.ProjectMemberService;
import com.ess.api.utils.GetCurrentEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projectMember")
@CrossOrigin(origins = "http://localhost:5173")
public class ProjectMemberController {

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private GetCurrentEmployee getCurrentEmployee;

    @GetMapping("/{role}")
    public ResponseEntity<?> getProjectMembersWithRoleInProject(Authentication authentication, @PathVariable Project.RoleInProject role){
        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        /*if(!currentEmployee.getRole().getName().equalsIgnoreCase("admin")){
            ApiResponse response = new ApiResponse("You are not authorized", false);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }*/
        Map<String, ArrayList<Project>> memberWithProject = projectMemberService.getProjectMembersWithRoleInProject(role);
        return ResponseEntity.ok(memberWithProject);
    }

    // Get all projects of given employee
    @GetMapping("/{employeeId}/allProjects")
    public ResponseEntity<?> getAllProjectsOfEmployee(@PathVariable long employeeId){
        List<Project> listOfProjects = projectMemberService.getAllTheProjectsWithGivenEmployee(employeeId);
        return ResponseEntity.ok(listOfProjects);
    }
}
