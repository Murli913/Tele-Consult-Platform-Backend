package com.teleconsulting.demo.repository;

import com.teleconsulting.demo.model.CallHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CallHistoryRepository extends JpaRepository<CallHistory, Long> {
    List<CallHistory> findByCallDate(LocalDate callDate);

    List<CallHistory> findByCallDateAndCallTimeBetween(LocalDate callDate, LocalTime startTime, LocalTime endTime);

    List<CallHistory> findByDoctorId(Long doctorId);
    List<CallHistory> findByDoctorIdIn(List<Long> doctorIds);
    List<CallHistory> findByDoctorIdAndCallDate(Long doctorId, LocalDate callDate);
    List<CallHistory> findByDoctorIdAndCallDateAndCallTimeBetween(Long doctorId, LocalDate callDate, LocalTime startTime, LocalTime endTime);
}
