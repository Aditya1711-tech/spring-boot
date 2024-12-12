package com.ess.api.repositories;

import com.ess.api.entities.AttendanceCorrection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendaceCorrectionRepository extends JpaRepository<AttendanceCorrection, Long> {
}
