package com.teleconsulting.demo.controller;

import com.teleconsulting.demo.exception.UserNotFoundException;
import com.teleconsulting.demo.model.Patient;
import com.teleconsulting.demo.repository.PatientRepository;
import com.teleconsulting.demo.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/patient")
public class PatientController {
    private final PatientService patientService;
    private final PatientRepository patientRepository;

    public PatientController(PatientService patientService, PatientRepository patientRepository) {
        this.patientService = patientService;
        this.patientRepository = patientRepository;
    }
//    @PostMapping("/add") // Patient
//    public String add(@RequestBody Patient patient)
//    {
//        patientService.savePatient(patient);
//        return "New Patient Added";
//    }

    @GetMapping("/patient/{id}") // Get Patient details by its ID
    Patient getUserById(@PathVariable Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
    @PutMapping("/patient/{id}") //
    Patient updatePatient(@RequestBody Patient newPatient, @PathVariable Long id) {
        return patientRepository.findById(id)
                .map(Patient -> {
                    Patient.setGender(newPatient.getGender());
                    Patient.setName(newPatient.getName());
                    Patient.setPhoneNumber(newPatient.getPhoneNumber());

                    return patientRepository.save(Patient);
                }).orElseThrow(() -> new UserNotFoundException(id));
    }

    @PostMapping // To create new patient for
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient createdPatient = patientService.createPatient(patient);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
    }
    @GetMapping(params = "phoneNumber") // Get patient details from phone number
    public ResponseEntity<Patient> getPatientByPhoneNumber(@RequestParam String phoneNumber) {
        Patient patient = patientService.getPatientByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(patient);
    }
    @GetMapping("/id") // Get ID from phone number
    public ResponseEntity<Patient> getPatientById(@RequestParam String phoneNumber) {
        Patient patient = patientService.getPatientByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(patient);
    }
}