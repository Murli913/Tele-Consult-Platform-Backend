package com.teleconsulting.demo.repository;

import com.teleconsulting.demo.model.CallHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CallHistoryRepository extends JpaRepository<CallHistory, Long> {
    List<CallHistory> findByCallDate(LocalDate callDate);

    List<CallHistory> findByCallDateAndCallTimeBetween(LocalDate callDate, LocalTime startTime, LocalTime endTime);
}
