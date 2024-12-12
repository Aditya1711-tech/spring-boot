package com.ess.api.repositories;

import com.ess.api.entities.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    public Holiday findHolidayByName(String name);
}
