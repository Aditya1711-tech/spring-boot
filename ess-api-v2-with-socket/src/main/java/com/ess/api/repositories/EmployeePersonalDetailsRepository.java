package com.ess.api.repositories;

import com.ess.api.entities.Employee;
import com.ess.api.entities.EmployeePersonalDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeePersonalDetailsRepository extends JpaRepository<EmployeePersonalDetails, Long> {
    public EmployeePersonalDetails findEmployeePersonalDetailsByEmployee(Employee employee);
}
