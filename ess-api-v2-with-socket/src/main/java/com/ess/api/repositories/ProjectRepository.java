package com.ess.api.repositories;

import com.ess.api.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    public Project findByName(String name);
}
