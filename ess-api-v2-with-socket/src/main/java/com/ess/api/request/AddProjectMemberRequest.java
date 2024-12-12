package com.ess.api.request;

import com.ess.api.entities.Project;

public class AddProjectMemberRequest {

    private long projectId;
    private long employeeId;
    private Project.RoleInProject role;

    public AddProjectMemberRequest(long projectId, long employeeId, Project.RoleInProject role) {
        this.projectId = projectId;
        this.employeeId = employeeId;
        this.role = role;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public Project.RoleInProject getRole() {
        return role;
    }

    public void setRole(Project.RoleInProject role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "AddProjectMemberRequest{" +
                "projectId=" + projectId +
                ", employeeId=" + employeeId +
                ", role=" + role +
                '}';
    }
}
