package com.ess.api.services;

import com.ess.api.entities.*;
import com.ess.api.repositories.SubTaskRepository;
import com.ess.api.request.AddSubTaskRequest;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubTaskService {

    @Autowired
    private SubTaskRepository subTaskRepository;

    @Autowired
    private ProjectLogService projectLogService;

    // Add subtask to given task
    public ProjectLog addSubTask(AddSubTaskRequest addSubTaskRequest, Task task, Employee currentEmployee){
        SubTask subTask = new SubTask(addSubTaskRequest.getDescription(), task, addSubTaskRequest.getStatus());
        SubTask savedSubTask = subTaskRepository.save(subTask);
        return projectLogService.addLogToProject(savedSubTask.getTask().getProject(), currentEmployee.getEmail() + " added child task " + savedSubTask.getDescription());
    }

    // Search subtasks into task
    public List<SubTask> searchSubTasksInTask(String keyWord, Long taskId){
        List<SubTask> lisOfSubTasks = subTaskRepository.findByDescriptionContaining(keyWord);
        return lisOfSubTasks.stream().filter(subTask -> subTask.getTask().getId() == taskId).toList();
    }
}
