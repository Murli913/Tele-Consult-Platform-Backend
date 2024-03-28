package com.teleconsulting.demo.controller;

import com.teleconsulting.demo.dto.Pdetails;
import com.teleconsulting.demo.exception.UserNotFoundException;
import com.teleconsulting.demo.model.Doctor;
import com.teleconsulting.demo.model.Patient;
import com.teleconsulting.demo.repository.DoctorRepository;
import com.teleconsulting.demo.repository.PatientRepository;
import com.teleconsulting.demo.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    private PatientService patientService;
    @Autowired
    private PatientRepository patientRepository;
    @PostMapping("/add") // Patient
    public String add(@RequestBody Patient patient)
    {
        patientService.savePatient(patient);
        return "New Patient Added";
    }
    @GetMapping("/patient") // Move to Admin
    List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
    @GetMapping("/patient/{id}") // Get Patient details by its ID
    Patient getUserById(@PathVariable Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
    @DeleteMapping("/patient/{id}") // Move to Admin
    String deletePatient(@PathVariable Long id){
        if(!patientRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }
        patientRepository.deleteById(id);
        return  "Patient with id "+id+" has been deleted success.";
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

    @PutMapping("/{patientId}") // Doctor can access
    public ResponseEntity<Patient> updatePatient(@PathVariable Long patientId, @RequestBody Pdetails pdetails) {
        Patient patient = patientService.updatePatient(patientId, pdetails );
        return ResponseEntity.ok(patient);
}
    @GetMapping("/{patientId}") // Doctor can access
    public ResponseEntity<?> getPatientNameAndAge(@PathVariable Long patientId) {
        try {
            // Retrieve the patient information by patientId
            Patient patient = patientService.findById(patientId);

            // Check if the patient exists
            if (patient == null) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found if patient is not found
            }

            // Create a DTO (Data Transfer Object) to send only name and age
            Map<String, Object> patientInfo = new HashMap<>();
            patientInfo.put("name", patient.getName());
            patientInfo.put("gender", patient.getGender());

            // Return the patient name and gender in the response body
            return ResponseEntity.ok(patientInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving patient information");

        }
    }



}
