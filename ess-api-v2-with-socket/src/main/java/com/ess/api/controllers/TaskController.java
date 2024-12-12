package com.ess.api.controllers;

import com.ess.api.entities.*;
import com.ess.api.request.AddSubTaskRequest;
import com.ess.api.request.AddTaskRequest;
import com.ess.api.response.ApiResponse;
import com.ess.api.services.*;
import com.ess.api.utils.GetCurrentEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/task")
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private GetCurrentEmployee getCurrentEmployee;

    @Autowired
    private SubTaskService subTaskService;

    // Add task
    @PostMapping
    public ResponseEntity<?> createNewTask(@RequestBody AddTaskRequest addTaskRequest, Authentication authentication){
        System.out.println(addTaskRequest);
        Project project = projectService.getProjectById(addTaskRequest.getProjectId());
        Employee assignedBy = getCurrentEmployee.getCurrentEmployee(authentication);

        AtomicBoolean managerFound = new AtomicBoolean(false);
        if(!assignedBy.getRole().getName().equalsIgnoreCase("admin")){
            project.getMembers().forEach(projectMember -> {
                if(projectMember.getEmployee() == assignedBy && projectMember.getRole() == Project.RoleInProject.MANAGER){
                    managerFound.set(true);
                }
            });
        }else{
            managerFound.set(true);
        }
        if(!managerFound.get()){
            ApiResponse response = new ApiResponse("You are not authorized", false);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Employee assignTo = employeeService.getEmployee(addTaskRequest.getAssignedTo());
        Task newTask = new Task(addTaskRequest.getName(), addTaskRequest.getDescription(), project, assignedBy, assignTo, addTaskRequest.getStatus(), addTaskRequest.getPriority());
        ProjectLog newTaskLog = taskService.addTask(newTask);

        return ResponseEntity.ok(newTaskLog);
    }

    // get tasks by employee
    @GetMapping("/assignedToMe")
    public ResponseEntity<?> getTaskAssignedToCurrentEmployee(Authentication authentication){
        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        List<Task> filteredTasks = taskService.getAllTasksByAssignedTo(currentEmployee);
        return ResponseEntity.ok(filteredTasks);
    }

    // Get tasks of project
    @GetMapping("/{projectId}")
    public ResponseEntity<?> getTasksAssignedInProject(@PathVariable long projectId){
        Project project = projectService.getProjectById(projectId);
        List<Task> allTasks = taskService.getAllTasks();

        List<Task> listOfTasksAssignedInGivenProject = allTasks.stream().filter(task -> task.getProject() == project).toList();
        return ResponseEntity.ok(listOfTasksAssignedInGivenProject);
    }

    // Search into tasks
    @GetMapping("/{projectId}/{keyword}")
    public ResponseEntity<?> searchIntoTasks(@PathVariable long projectId, @PathVariable String keyword){
        List<Task> listOfSearchedTasks = taskService.search(projectId, keyword);
        return ResponseEntity.ok(listOfSearchedTasks);
    }

    //Get task by id
    @GetMapping("/withId/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable long taskId){
        Task taskById = taskService.getTaskById(taskId);
        return ResponseEntity.ok(taskById);
    }

    @PutMapping("/updateStatus/{taskId}")
    public ResponseEntity<?> updateTaskStatus(@PathVariable long taskId, @RequestBody Task task, Authentication authentication){
        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        ProjectLog updateStatusLog = taskService.updateTaskStatus(taskId, task, currentEmployee);
        return ResponseEntity.ok(updateStatusLog);
    }

    @PutMapping("/updatePriority/{taskId}")
    public ResponseEntity<?> updateTaskPriority(@PathVariable long taskId, @RequestBody Task task, Authentication authentication){
        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        ProjectLog updatePriorityLog = taskService.updateTaskPriority(taskId, task, currentEmployee);
        return ResponseEntity.ok(updatePriorityLog);
    }

    @PutMapping("/updateTask/{taskId}")
    public ResponseEntity<?> updateTask(@RequestBody Task task, @PathVariable long taskId, Authentication authentication){
        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        ProjectLog updateTaskLog = taskService.updateTask(taskId, task, currentEmployee);
        return ResponseEntity.ok(updateTaskLog);
    }

    // Add subTask
    @PostMapping("/addSubTask/{taskId}")
    public ResponseEntity<?> addSubTask(@RequestBody AddSubTaskRequest addSubTaskRequest, @PathVariable Long taskId, Authentication authentication){
        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        Task task = taskService.getTaskById(taskId);
        ProjectLog subTaskLog = subTaskService.addSubTask(addSubTaskRequest, task, currentEmployee);
        return ResponseEntity.ok(subTaskLog);
    }

    // Search subTasks
    @GetMapping("/search/subtasks/{taskId}/{keyword}")
    public ResponseEntity<?> searchSubTasks(@PathVariable Long taskId, @PathVariable String keyword){
        List<SubTask> listToSend = subTaskService.searchSubTasksInTask(keyword, taskId);
        return ResponseEntity.ok(listToSend);
    }
}
