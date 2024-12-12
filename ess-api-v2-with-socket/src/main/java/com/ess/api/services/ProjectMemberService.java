package com.ess.api.services;

import com.ess.api.entities.*;
import com.ess.api.exceptions.ResourceAlreadyExistsException;
import com.ess.api.repositories.ProjectMemberRepository;
import com.ess.api.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectMemberService {

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private ProjectLogService projectLogService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ProjectRepository projectRepository;

    // Add Project Member
    public ProjectLog addMember(Project project, Employee employee, Project.RoleInProject role){
        ProjectMember existsMember = projectMemberRepository.findByEmployeeAndProject(employee, project);

        if(existsMember != null && existsMember.getRole().equals(role)){
            throw new ResourceAlreadyExistsException(employee.getEmail(),  " as " + role + " in project " , project.getName());
        }else if(existsMember != null){
            Project.RoleInProject oldRole = existsMember.getRole();
            existsMember.setRole(role);
            projectMemberRepository.save(existsMember);
            return projectLogService.addLogToProject(project, "Role of " + employee.getEmail() + " changed from " + oldRole.toString() + " to " + existsMember.getRole().toString());
        }

        ProjectMember newMember = new ProjectMember(project, employee, role);
        ProjectMember savedMember = projectMemberRepository.save(newMember);
        return projectLogService.addLogToProject(project, employee.getEmail() + " Added to project: " + project.getName() + " with role " + savedMember.getRole().toString());
    }

    public Map<String, ArrayList<Project>> getProjectMembersWithRoleInProject(Project.RoleInProject role){
        List<ProjectMember> listOfMembers = projectMemberRepository.findAll();
        Map<String, ArrayList<Project>> memberWithProject = new LinkedHashMap<>();
        listOfMembers.forEach(projectMember -> {
            if(projectMember.getRole().toString().equalsIgnoreCase(role.toString())){
                String memberName = projectMember.getEmployee().getFirstName() + " " +projectMember.getEmployee().getLastName();
                Project project = projectMember.getProject();
                if(memberWithProject.containsKey(memberName)){
                    ArrayList<Project> existingList =  memberWithProject.get(memberName);
                    existingList.add(project);
                    memberWithProject.put(memberName, existingList);
                }else{
                    ArrayList<Project> newList =  new ArrayList<>();
                    newList.add(project);
                    memberWithProject.put(memberName, newList);
                }
            }
        });
        return memberWithProject;
    }

    // Get list of projects of given employee
    public List<Project> getAllTheProjectsWithGivenEmployee(long employeeId){
        Employee givenEmployee = employeeService.getEmployee(employeeId);
        List<Project> allProjects = projectRepository.findAll();

        List<Project> projectsWithGivenEmployee = new ArrayList<>();
        allProjects.forEach(project -> project.getMembers().forEach(projectMember -> {
            if(projectMember.getEmployee().getId() == employeeId){
                projectsWithGivenEmployee.add(project);
            }
        }));
        return projectsWithGivenEmployee;
    }
}
