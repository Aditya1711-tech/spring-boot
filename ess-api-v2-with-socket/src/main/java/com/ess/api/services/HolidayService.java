package com.ess.api.services;

import com.ess.api.entities.Holiday;
import com.ess.api.exceptions.ResourceAlreadyExistsException;
import com.ess.api.exceptions.ResourceNotFoundException;
import com.ess.api.repositories.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HolidayService {

    @Autowired
    private HolidayRepository holidayRepository;

    // Add holiday
    public Holiday addHoliday(Holiday holiday){
        Holiday holidayWithName = holidayRepository.findHolidayByName(holiday.getName());
        if(holidayWithName != null && holidayWithName.getDate().equals(holiday.getDate())){
            throw new ResourceAlreadyExistsException("Holiday", "name and date", holiday.getName() + " " + holiday.getDate());
        }
        return holidayRepository.save(holiday);
    }

    // Get holidays with given year
    public List<Holiday> getByGivenYear(int year){
        List<Holiday> allHolidays = holidayRepository.findAll();
        List<Holiday> holidaysInGivenYear = new ArrayList<>(allHolidays.stream().filter(holiday -> holiday.getDate().getYear() == year).toList());
        holidaysInGivenYear.sort(Comparator.comparing(Holiday::getDate));
        return holidaysInGivenYear;
    }

    // Get all the possible years from holidays
    public Set<Integer> getAllPossibleYears(){
        List<Holiday> allHolidays = holidayRepository.findAll();
        Set<Integer> possibleYears = new TreeSet<>();
        allHolidays.forEach(holiday -> {
            possibleYears.add(holiday.getDate().getYear());
        });
        return possibleYears;
    }

    // Get map of year and list of holidays in that year
    public Map<Integer, List<Holiday>> getMapOfYearAndHolidays(){
        Map<Integer, List<Holiday>> mapOfYearAndHolidays = new LinkedHashMap<>();
        //TODO: Implement service



        return mapOfYearAndHolidays;
    }

    // Get all holidays
    public List<Holiday> getAllHoliDays(){
        return holidayRepository.findAll();
    }

    // Get by id
    public Holiday getHolidayById(long holidayId){
        return holidayRepository.findById(holidayId).orElseThrow(() -> new ResourceNotFoundException("Holiday", "HolidayId", ""+holidayId));
    }

    // Update by id
    public Holiday updateHolidayById(long holidayId, Holiday holiday){
        Holiday holidayToUpdate = this.getHolidayById(holidayId);
        if(holiday.getName() != null) holidayToUpdate.setName(holiday.getName());
        if(holiday.getDate() != null){
            holidayToUpdate.setDate(holiday.getDate());
            holidayToUpdate.setDay(holiday.getDate().getDayOfWeek());
        }
        return holidayRepository.save(holidayToUpdate);
    }

    // Delete by id
    public Holiday deleteHolidayById(Long holidayId){
        Holiday holidayToDelete = this.getHolidayById(holidayId);
        holidayRepository.deleteById(holidayId);
        return holidayToDelete;
    }
}
