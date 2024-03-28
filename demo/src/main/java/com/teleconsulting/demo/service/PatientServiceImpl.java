package com.teleconsulting.demo.service;

import com.teleconsulting.demo.dto.Pdetails;
import com.teleconsulting.demo.model.AuthenticationResponse;
import com.teleconsulting.demo.model.Patient;
import com.teleconsulting.demo.model.Role;
import com.teleconsulting.demo.repository.PatientRepository;
import com.teleconsulting.demo.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl implements PatientService{
    @Autowired
    private  final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtService jwtService;
    public PatientServiceImpl(PatientRepository patientRepository, JwtService jwtService) {
        this.patientRepository = patientRepository;
        this.jwtService = jwtService;
    }

    @Override
    public void savePatient(Patient patient) {
        patientRepository.save(patient);
    }


    @Override
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient getPatientByPhoneNumber(String phoneNumber) {
        return patientRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Patient findById(Long id) {
        return patientRepository.findById(id).orElse(null);
    }

    @Override
    public Patient updatePatient(Long patientId, Pdetails pdetails) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        // Update patient details
        patient.setName(pdetails.getName());
        patient.setGender(pdetails.getGender());

        // Save and return the updated patient
        return patientRepository.save(patient);
}

    @Override
    public AuthenticationResponse saveNewPatient(Patient patient){
        Patient patient1 = new Patient();
        patient1.setPassword(passwordEncoder.encode(patient.getPassword()));
        patient1.setEmail(patient.getEmail());
        patient1.setName(patient.getName());
        patient1.setGender(patient.getGender());
        patient1.setPhoneNumber(patient.getPhoneNumber());
        patient1.setRole(Role.valueOf("USER"));
        patient1 = patientRepository.save(patient1);
        String jwt = jwtService.generateToken(patient1);
        return new AuthenticationResponse(jwt, "User Registration was Successful!!");
    }
}
