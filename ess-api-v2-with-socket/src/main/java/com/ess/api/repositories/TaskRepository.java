package com.ess.api.repositories;

import com.ess.api.entities.Project;
import com.ess.api.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    public Task findTaskByName(String name);
    public List<Task> findByNameContainingOrDescriptionContainingOrAssignToFirstNameContains(String nameKeyword, String descriptionKeyword, String firstNameKeyword);
}
