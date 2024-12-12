package com.ess.api.request;

import com.ess.api.entities.AttendanceCorrection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UpdateCorrectionStatusRequest {
    private AttendanceCorrection attendanceCorrection;
    private AddNote addNote;
    private Long requestEmployeeId;
}
