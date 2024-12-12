package com.ess.api.services;

import com.ess.api.entities.Employee;
import com.ess.api.entities.PunchIn;
import com.ess.api.repositories.PunchInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PunchInService {

    @Autowired
    private PunchInRepository punchInRepository;

    // Add
    public PunchIn doPunch(PunchIn punchIn){
        return punchInRepository.save(punchIn);
    }

    // Get All
    public List<PunchIn> getAllPunchIn(){
        return punchInRepository.findAll();
    }

    // Get all by Date and employee
    public List<PunchIn> getAllPunchInByDateAndEmployee(LocalDate date, Employee employee){
        return punchInRepository.findByDateAndEmployee(date, employee);
    }
}
