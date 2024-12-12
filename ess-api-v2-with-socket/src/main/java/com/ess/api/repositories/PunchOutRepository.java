package com.ess.api.repositories;

import com.ess.api.entities.Employee;
import com.ess.api.entities.PunchOut;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PunchOutRepository extends JpaRepository<PunchOut, Long> {
    public List<PunchOut> findByDateAndEmployee(LocalDate date, Employee employee);
}
