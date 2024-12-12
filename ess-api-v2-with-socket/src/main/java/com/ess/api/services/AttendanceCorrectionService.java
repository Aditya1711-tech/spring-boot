package com.ess.api.services;

import com.ess.api.entities.*;
import com.ess.api.exceptions.ResourceNotFoundException;
import com.ess.api.repositories.AttendaceCorrectionRepository;
import com.ess.api.repositories.PunchInRepository;
import com.ess.api.repositories.PunchOutRepository;
import com.ess.api.request.AddNote;
import com.ess.api.request.AttendanceCorrectionRequest;
import com.ess.api.response.ApiResponse;
import com.ess.api.utils.SendMail;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AttendanceCorrectionService {

    @Autowired
    private AttendaceCorrectionRepository attendaceCorrectionRepository;

    @Autowired
    private PunchInAndOutService punchInAndOutService;

    @Autowired
    private PunchInRepository punchInRepository;

    @Autowired
    private PunchOutRepository punchOutRepository;

    @Autowired
    private SendMail sendMail;

    // Add request
    public ApiResponse addRequest(Employee employee, AttendanceCorrectionRequest attendanceCorrectionRequest){
        LocalDate date = LocalDate.of(attendanceCorrectionRequest.getYear(), attendanceCorrectionRequest.getMonth(), attendanceCorrectionRequest.getDay());

        long netMinutesOfGivenDate = punchInAndOutService.countNetMinutes(employee, date);
        if(netMinutesOfGivenDate >= 450){
            return new ApiResponse("Your net hours are already 7.5 for "+date,false);
        }

        AttendanceCorrection existingAttendaceCorrection = this.getByEmployeeAndDate(employee.getId(), date);
        if(existingAttendaceCorrection != null){
            return new ApiResponse("You already had requested for "+date,false);
        }

        AttendanceCorrection attendanceCorrection = new AttendanceCorrection(date, attendanceCorrectionRequest.getRemark(), Leave.LeaveStatus.PENDING, employee);
        attendanceCorrection.setEmployee(employee);
        attendaceCorrectionRepository.save(attendanceCorrection);

        return new ApiResponse("Attendance correction request added successfully", true);
    }

    // Get by id
    public AttendanceCorrection getById(Long correctionId){
        return attendaceCorrectionRepository.findById(correctionId).orElseThrow(() -> new ResourceNotFoundException("AttendaceCorrection", "id", correctionId.toString()));
    }

    // Get all
    public List<AttendanceCorrection> getAll(){
        return attendaceCorrectionRepository.findAll();
    }

    // Update status
    public AttendanceCorrection updateStatus(Leave.LeaveStatus status, Long correctionId, AddNote addNote, Employee currentEmployee) throws MessagingException, IOException {
        AttendanceCorrection existingattendanceCorrection = this.getById(correctionId);
        existingattendanceCorrection.setStatus(status);
        if(status == Leave.LeaveStatus.APPROVED){
            punchInAndOutService.deletePunchesOfGivenDateOfGivenEmployee(existingattendanceCorrection.getEmployee(), existingattendanceCorrection.getDate());
            PunchIn newPunchIn = new PunchIn(existingattendanceCorrection.getDate(), LocalTime.of(10,0,0), existingattendanceCorrection.getEmployee());
            PunchOut newPunchOut = new PunchOut(existingattendanceCorrection.getDate(), LocalTime.of(17,30,0), existingattendanceCorrection.getEmployee());
            punchInRepository.save(newPunchIn);
            punchOutRepository.save(newPunchOut);
        }
        sendMail.sendApproveOrRejectedRequestMail(existingattendanceCorrection.getEmployee().getEmail(), existingattendanceCorrection.getStatus().toString(), currentEmployee.getFirstName()  + " " + currentEmployee.getLastName(), addNote.getNote());

        return attendaceCorrectionRepository.save(existingattendanceCorrection);
    }

    // Get by employee and date
    public AttendanceCorrection getByEmployeeAndDate(Long employeeId, LocalDate date){
        List<AttendanceCorrection> allTheAttendanceCorrection = this.getAll();
        for (AttendanceCorrection attendanceCorrection : allTheAttendanceCorrection) {
            if (attendanceCorrection.getEmployee().getId() == employeeId && attendanceCorrection.getDate().equals(date)) {
                return attendanceCorrection;
            }
        }
        return null;
    }
}
