package com.ess.api.services;

import com.ess.api.entities.Employee;
import com.ess.api.entities.Project;
import com.ess.api.entities.ProjectLog;
import com.ess.api.entities.Task;
import com.ess.api.exceptions.ResourceAlreadyExistsException;
import com.ess.api.exceptions.ResourceNotFoundException;
import com.ess.api.repositories.ProjectLogRepository;
import com.ess.api.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectLogService projectLogService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ProjectMemberService projectMemberService;

    // Add
    public ProjectLog addProject(Project project){
        Project existingProject = projectRepository.findByName(project.getName());
        if(existingProject != null){
            throw new ResourceAlreadyExistsException("Project ", " with name ", project.getName());
        }
        Project savedProject = projectRepository.save(project);
        return projectLogService.addLogToProject(savedProject, savedProject.getName() + " Project created");
    }

    // Get by id
    public Project getProjectById(Long projId){
        return projectRepository
                .findById(projId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "projId", ""+projId));
    }

    // Add employee to project
    public ProjectLog addEmployeeToProject(long projectId, long employeeId, Project.RoleInProject role) {
        Project project = this.getProjectById(projectId);
        Employee employee = employeeService.getEmployee(employeeId);

        return projectMemberService.addMember(project, employee, role);
    }

    // Get all
    public List<Project> getAllProjects(){
        return projectRepository.findAll();
    }

    // Get project from task id
    public Project getProjectFromProjectTask(long taskId){
        List<Project> allProjects = this.getAllProjects();
        Project projectOfGivenTask = null;

        boolean isProjectFound = false;
        for(int i=0;i<allProjects.size();i++){
            List<Task> listOfTasksInProjectI = allProjects.get(i).getTasks();
            for(int j=0;j< listOfTasksInProjectI.size();j++){
                if(listOfTasksInProjectI.get(j).getId() == taskId){
                    projectOfGivenTask = listOfTasksInProjectI.get(j).getProject();
                    isProjectFound = true;
                    break;
                }
                if(isProjectFound) break;
            }
        }
        if(!isProjectFound) throw new ResourceNotFoundException("Project with task", "TaskId", ""+taskId);
        return projectOfGivenTask;
    }

}
