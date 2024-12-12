package com.ess.api.repositories;

import com.ess.api.entities.ProjectLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectLogRepository extends JpaRepository<ProjectLog, Long> {
    public List<ProjectLog> findByProjectId(Long projectId);
}
