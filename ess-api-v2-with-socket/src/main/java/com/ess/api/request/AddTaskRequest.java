package com.ess.api.request;

import com.ess.api.entities.Task;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddTaskRequest {
    private String name;

    private String description;

    private long projectId;

    private long assignedTo;

    private Task.TaskStatus status;

    private Task.TaskPriority priority;

    public AddTaskRequest() {
        super();
    }

    @JsonCreator
    public AddTaskRequest(@JsonProperty("name") String name, @JsonProperty("description") String description,@JsonProperty("projectId") long projectId,@JsonProperty("assignedTo") long assignedTo,@JsonProperty("status") Task.TaskStatus status,@JsonProperty("priority") Task.TaskPriority priority) {
        this.name = name;
        this.description = (description != null) ? description : name.toUpperCase() + " DESCRIPTION";
        this.projectId = projectId;
        this.assignedTo = assignedTo;
        this.status = (status != null) ? status : Task.TaskStatus.TODO;
        this.priority = (priority != null) ? priority : Task.TaskPriority.NONE;
    }

    public AddTaskRequest(String name, long projectId, long assignedTo) {
        this.name = name;
        this.projectId = projectId;
        this.assignedTo = assignedTo;
        this.status = Task.TaskStatus.TODO;
        this.priority = Task.TaskPriority.NONE;
    }

    public AddTaskRequest(String name, long projectId, long assignedTo, Task.TaskStatus status) {
        this.name = name;
        this.projectId = projectId;
        this.assignedTo = assignedTo;
        this.status = status;
        this.priority = Task.TaskPriority.NONE;
    }

    public AddTaskRequest(String name, long projectId, long assignedTo, Task.TaskPriority priority) {
        this.name = name;
        this.projectId = projectId;
        this.assignedTo = assignedTo;
        this.status = Task.TaskStatus.TODO;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(long assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Task.TaskStatus getStatus() {
        return status;
    }

    public void setStatus(Task.TaskStatus status) {
        this.status = status;
    }

    public Task.TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(Task.TaskPriority priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "AddTaskRequest{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", projectId=" + projectId +
                ", assignedTo=" + assignedTo +
                ", status=" + status +
                ", priority=" + priority +
                '}';
    }
}
