package com.ess.api.services;

import com.ess.api.entities.Project;
import com.ess.api.entities.ProjectLog;
import com.ess.api.repositories.ProjectLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectLogService {

    @Autowired
    private ProjectLogRepository projectLogRepository;

    // Add log to project
    public ProjectLog addLogToProject(Project project, String message) {
        ProjectLog newLog = new ProjectLog(project, message);
        return projectLogRepository.save(newLog);
    }

    // Get logs for project
    public List<ProjectLog> getLogsForProject(long projectId) {
        return projectLogRepository.findByProjectId(projectId);
    }
}
