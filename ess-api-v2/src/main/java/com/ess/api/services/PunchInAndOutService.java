package com.ess.api.services;

import com.ess.api.entities.Employee;
import com.ess.api.entities.PunchIn;
import com.ess.api.entities.PunchOut;
import com.ess.api.repositories.PunchInRepository;
import com.ess.api.repositories.PunchOutRepository;
import com.ess.api.response.Punch;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import javax.swing.text.html.HTMLDocument;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import java.util.stream.Collectors;

@Service
public class PunchInAndOutService {
    @Autowired
    private PunchInService punchInService;

    @Autowired
    private PunchOutService punchOutService;

    @Autowired
    private PunchInRepository punchInRepository;

    @Autowired
    private PunchOutRepository punchOutRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<Punch> getAllPunchesByDateAndEmployee(Employee employee, LocalDate date){
        List<PunchIn> allPunchInByDateAndEmployee = punchInService.getAllPunchInByDateAndEmployee(date, employee);
        List<PunchOut> allPunchOutByDateAndEmployee = punchOutService.getAllPunchOutByDateAndEmployee(date, employee);

        List<Punch> allPunchIn = allPunchInByDateAndEmployee
                .stream()
                .map(punchIn -> {
                    Punch punch = modelMapper.map(punchIn, Punch.class);
                    punch.setPunchIn(true);
                    punch.setPunchOut(false);
                    return punch;
                }).toList();

        List<Punch> allPunchOut = allPunchOutByDateAndEmployee
                .stream()
                .map(punchOut -> {
                    Punch punch = modelMapper.map(punchOut, Punch.class);
                    punch.setPunchIn(false);
                    punch.setPunchOut(true);
                    return punch;
                }).toList();

        List<Punch> allPunches = new ArrayList<>();
        allPunches.addAll(allPunchIn);
        allPunches.addAll(allPunchOut);

        allPunches.sort(Comparator.comparing(Punch::getTime));
        return allPunches;
    }

    public long countNetMinutes(Employee employee, LocalDate date){
        List<Punch> allPunches = this.getAllPunchesByDateAndEmployee(employee, date);

        boolean isPrevOut = true;
        ArrayList<LocalTime> timeIntervals = new ArrayList<>();
        for (Punch punch : allPunches) {
            if(isPrevOut && punch.isPunchIn()){
                timeIntervals.add(punch.getTime());
                isPrevOut = false;
            }else if(!isPrevOut && punch.isPunchOut()){
                timeIntervals.add(punch.getTime());
                isPrevOut = true;
            }
        }
        long netMinutes = 0;
//        System.out.println(timeIntervals);
        if(!timeIntervals.isEmpty()){
            if(timeIntervals.size() % 2 == 0){
                netMinutes = ChronoUnit.MINUTES.between(timeIntervals.getFirst(), timeIntervals.getLast());
            }else{
                netMinutes = ChronoUnit.MINUTES.between(timeIntervals.getFirst(), LocalTime.now());
            }
        }

//        System.out.println(netMinutes);
        for(int i=0;i< timeIntervals.size();i++){
            if(i > 0 && i % 2 == 0){
                long outDuration =   ChronoUnit.MINUTES.between(timeIntervals.get(i-1), timeIntervals.get(i));
//                System.out.println(outDuration);
                netMinutes -= outDuration;
            }
        }

        return netMinutes;
    }

    public Set<LocalDate> getAllDates(Employee employee){
        List<PunchIn> allPunchIn =  punchInRepository.findAll();
        List<PunchOut> allPunchOut = punchOutRepository.findAll();

        List<Punch> allPunchInPunch = allPunchIn
                .stream()
                .filter(punchIn -> punchIn.getEmployee() == employee)
                .map(punchIn -> {
                    Punch punch = modelMapper.map(punchIn, Punch.class);
                    punch.setPunchIn(true);
                    punch.setPunchOut(false);
                    return punch;
                }).toList();

        List<Punch> allPunchOutPunch = allPunchOut
                .stream()
                .filter(punchIn -> punchIn.getEmployee() == employee)
                .map(punchOut -> {
                    Punch punch = modelMapper.map(punchOut, Punch.class);
                    punch.setPunchIn(false);
                    punch.setPunchOut(true);
                    return punch;
                }).toList();


        List<Punch> allPunches = new ArrayList<>();
        allPunches.addAll(allPunchInPunch);
        allPunches.addAll(allPunchOutPunch);

        allPunches.sort(Comparator.comparing(Punch::getDate));
        allPunches = allPunches.reversed();

        Set<LocalDate> allDates = new LinkedHashSet<>();

        for(Punch punch:allPunches){
            allDates.add(punch.getDate());
        }

        return allDates;
    }

    public void getAllDatesWithNetHours(Employee employee){

    }
}
