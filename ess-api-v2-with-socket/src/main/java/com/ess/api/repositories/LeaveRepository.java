package com.ess.api.repositories;

import com.ess.api.entities.Employee;
import com.ess.api.entities.Leave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    public Optional<List<Leave>> findByEmployee(Employee employee);
}
