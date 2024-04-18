package com.teleconsulting.demo.repository;

import com.teleconsulting.demo.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findBySupervisorDoctorId(Long supervisorId);
    Doctor findByPhoneNumber(String phoneNumber);
    Optional<Doctor> findById(Long id);
    Optional<Doctor> findByEmail(String email);
    List<Doctor> findBySupervisorDoctorIsNull();
    @Query("SELECT d FROM Doctor d WHERE d.supervisorDoctor IS NULL")
    List<Doctor> findAllSrDoc();
}
