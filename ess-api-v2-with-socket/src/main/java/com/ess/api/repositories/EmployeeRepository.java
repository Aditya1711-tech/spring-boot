package com.ess.api.repositories;

import com.ess.api.entities.Employee;
import com.ess.api.entities.Role;
import com.ess.api.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    public Optional<Employee> findByEmail(String email);

    public List<Employee> findAllEmployeesByTeam(Team team);

    public List<Employee> findAllEmployeesByRole(Role role);
}
