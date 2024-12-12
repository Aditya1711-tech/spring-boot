package com.ess.api.services;

import com.ess.api.entities.Employee;
import com.ess.api.entities.PunchOut;
import com.ess.api.repositories.PunchOutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PunchOutService {

    @Autowired
    private PunchOutRepository punchOutRepository;

    // Add
    public PunchOut doPunch(PunchOut punchOut){
        return punchOutRepository.save(punchOut);
    }

    // Get all by date and employee
    public List<PunchOut> getAllPunchOutByDateAndEmployee(LocalDate date, Employee employee){
        return punchOutRepository.findByDateAndEmployee(date, employee);
    }
}
