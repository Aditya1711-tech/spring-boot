package com.ess.api.repositories;

import com.ess.api.entities.Employee;
import com.ess.api.entities.PunchIn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PunchInRepository extends JpaRepository<PunchIn, Long> {
    public List<PunchIn> findByDateAndEmployee(LocalDate date, Employee employee);
}
