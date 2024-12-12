package com.ess.api.repositories;

import com.ess.api.entities.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubTaskRepository extends JpaRepository<SubTask, Long> {
    public List<SubTask> findByDescriptionContaining(String keyword);
}
