package com.ess.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ess.api.entities.ProjectLog;
import com.ess.api.services.ProjectLogService;

@RestController
@RequestMapping("/api/projectLog")
@CrossOrigin(origins = "http://localhost:5173")
public class ProjectLogController {

    @Autowired
    private ProjectLogService projectLogService;

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getLogsForProject(@PathVariable long projectId) {
        List<ProjectLog> logs = projectLogService.getLogsForProject(projectId);
        return ResponseEntity.ok(logs);
    }

}
