package com.teleconsulting.demo.service;

import com.teleconsulting.demo.dto.Pdetails;
import com.teleconsulting.demo.model.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    void savePatient(Patient patient);
    Patient createPatient(Patient patient);
    Patient getPatientByPhoneNumber(String phoneNumber);
    Patient findById(Long id);
    Patient updatePatient(Long patientId, Pdetails pdetails);
    Object saveNewPatient(Patient patient);
    Long countPatients();
    Optional<Patient> getUserByEmail(String token);
    List<Patient> getALLPatient();
    void deletePatient(Long id);
    List<Patient> findAll();
}
