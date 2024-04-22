package com.teleconsulting.demo.repository;

import com.teleconsulting.demo.model.CallHistory;
import com.teleconsulting.demo.model.Doctor;
import com.teleconsulting.demo.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CallHistoryRepository extends JpaRepository<CallHistory, Long> {
    List<CallHistory> findByCallDate(LocalDate callDate);

    List<CallHistory> findByCallDateAndCallTimeBetween(LocalDate callDate, LocalTime startTime, LocalTime endTime);

    List<CallHistory> findByDoctorId(Long doctorId);
    List<CallHistory> findByDoctorIdIn(List<Long> doctorIds);
    List<CallHistory> findByDoctorIdAndCallDate(Long doctorId, LocalDate callDate);
    //murli
    List<CallHistory> findByPatientId(Long patientId);
    List<CallHistory> findByDoctorIdAndCallDateAndCallTimeBetween(Long doctorId, LocalDate callDate, LocalTime startTime, LocalTime endTime);
    //MURLI
    Long countByDoctorId(Long doctorId);
    Long countDistinctByDoctorId(Long doctorId);
    //AZIZ
    List<CallHistory> findByDoctorAndPatient(Doctor doctor, Patient patient);
//teja
@Query("SELECT ch FROM CallHistory ch WHERE ch.patient.id = :patientId AND ch.callDate >= :startDate AND ch.endTime IS NULL")
List<CallHistory> findByPatientIdAndGreaterThanEqualAndEndTimeIsNull(@Param("patientId") Long patientId, @Param("startDate") LocalDate startDate);

    List<CallHistory> findByPatientIdAndEndTimeIsNotNull(Long patientId);
}
