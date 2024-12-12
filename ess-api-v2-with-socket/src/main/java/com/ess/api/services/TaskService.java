package com.ess.api.services;

import com.ess.api.entities.Employee;
import com.ess.api.entities.Project;
import com.ess.api.entities.ProjectLog;
import com.ess.api.entities.Task;
import com.ess.api.exceptions.ResourceAlreadyExistsException;
import com.ess.api.exceptions.ResourceNotFoundException;
import com.ess.api.repositories.ProjectRepository;
import com.ess.api.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectLogService projectLogService;

    @Autowired
    private ProjectRepository projectRepository;

    // Add task
    public ProjectLog addTask(Task task){
        Task existingTask = taskRepository.findTaskByName(task.getName());
        if(existingTask != null){
            throw new ResourceAlreadyExistsException("Task ", " with name ", task.getName());
        }
        Task savedTask = taskRepository.save(task);
        return projectLogService.addLogToProject(task.getProject(), savedTask.getName() + " task assign to " + savedTask.getAssignTo().getEmail() + " by " + savedTask.getAssignBy().getEmail());
    }

    // Get all
    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    // Get all by assignedTo
    public List<Task> getAllTasksByAssignedTo(Employee assignedTo){
        return this.getAllTasks().stream().filter(task -> task.getAssignTo() == assignedTo).toList();
    }


    // Get all by assignedBy
    public List<Task> getAllTasksByAssignedBy(Employee assignedBy){
        return this.getAllTasks().stream().filter(task -> task.getAssignBy() == assignedBy).toList();
    }

    // Get by id
    public Task getTaskById(long taskId){
        return taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task", "taskId", ""+taskId));
    }

    // Update task status
    public ProjectLog updateTaskStatus(Long taskId, Task task, Employee employee){
        Task oldTask = this.getTaskById(taskId);
        Task.TaskStatus oldStatus = oldTask.getStatus();
        oldTask.setStatus(task.getStatus());
        Task updatedTask = taskRepository.save(oldTask);
        return projectLogService.addLogToProject(oldTask.getProject(), " updated task status of " + updatedTask.getName() + " from " + oldStatus + " to " + updatedTask.getStatus() + " by " + employee.getEmail());
    }

    // Update task priority
    public  ProjectLog updateTaskPriority(Long taskId, Task task, Employee employee){
        Task oldTask = this.getTaskById(taskId);
        Task.TaskPriority oldPriority = oldTask.getPriority();
        oldTask.setPriority(task.getPriority());
        Task updatedTask = taskRepository.save(oldTask);
        return projectLogService.addLogToProject(oldTask.getProject(), " updated task priority of " + updatedTask.getName() + " from " + oldPriority + " to " + updatedTask.getPriority() + " by " + employee.getEmail());
    }

    // Update Task by id
    public ProjectLog updateTask(Long taskId, Task task, Employee employee){
        Task existingTask = this.getTaskById(taskId);
        boolean isUpdated = false;
        if(task.getAssignTo() != null && (task.getAssignTo().getId() != existingTask.getAssignTo().getId())){
            isUpdated = true;
            String oldAssignTo = existingTask.getAssignTo().getEmail();
            existingTask.setAssignTo(task.getAssignTo());
            String newAssignTo = existingTask.getAssignTo().getEmail();
            projectLogService.addLogToProject(existingTask.getProject(), " updated assign to " + oldAssignTo + " from " +  newAssignTo + " by " + employee.getEmail());
        }
        if(task.getAssignBy() != null && (task.getAssignBy().getId() !=  existingTask.getAssignBy().getId())){
            isUpdated = true;
            String oldAssignBy = existingTask.getAssignBy().getEmail();
            existingTask.setAssignBy(task.getAssignBy());
            String newAssignBy = existingTask.getAssignBy().getEmail();
            projectLogService.addLogToProject(existingTask.getProject(), " updated assign By " + oldAssignBy + " from " +  newAssignBy + " by " + employee.getEmail());
        }
        if(task.getDescription() != null && !Objects.equals(task.getDescription(), existingTask.getDescription())){
            isUpdated = true;
            String oldDescription = existingTask.getDescription();
            existingTask.setDescription(task.getDescription());
            String newDescription = existingTask.getDescription();
            projectLogService.addLogToProject(existingTask.getProject(), " updated description from " + oldDescription + " to " +  newDescription + " by " + employee.getEmail());
        }
        if(task.getStatus() != null && task.getStatus() != existingTask.getStatus()){
            isUpdated = true;
            String oldStatus = existingTask.getStatus().toString();
            existingTask.setStatus(task.getStatus());
            String newStatus = existingTask.getStatus().toString();
            projectLogService.addLogToProject(existingTask.getProject(), " updated status from " + oldStatus + " to " +  newStatus + " by " + employee.getEmail());
        }
        if(task.getPriority() != null && task.getPriority() != existingTask.getPriority()){
            isUpdated = true;
            String oldPriority = existingTask.getPriority().toString();
            existingTask.setPriority(task.getPriority());
            String newPriority = existingTask.getPriority().toString();
            projectLogService.addLogToProject(existingTask.getProject(), " updated priority from " + oldPriority + " to " +  newPriority + " by " + employee.getEmail());
        }
        Task updatedTask = taskRepository.save(existingTask);

        return isUpdated ? projectLogService.addLogToProject(updatedTask.getProject(), " Updated task " + task.getName() + " by " +  employee.getEmail()) : new ProjectLog(task.getProject(), task.getName() + " updated");
    }


    // Search tasks
    public List<Task> search(long projectId, String keyword) {
        List<Task> searchedTasks = taskRepository.findByNameContainingOrDescriptionContainingOrAssignToFirstNameContains(keyword, keyword, keyword);
        searchedTasks = searchedTasks.stream().filter(task -> task.getProject().getId() == projectId).toList();
        return searchedTasks;
    }
}
