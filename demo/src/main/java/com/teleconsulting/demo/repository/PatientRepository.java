package com.teleconsulting.demo.repository;

import com.teleconsulting.demo.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findByPhoneNumber(String phoneNumber);
    Optional<Patient> findById(Long id);
    Optional<Patient> findByEmail(String email);
}
