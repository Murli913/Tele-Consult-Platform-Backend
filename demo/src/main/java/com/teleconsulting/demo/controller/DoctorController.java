package com.teleconsulting.demo.controller;

import com.teleconsulting.demo.dto.Pdetails;
import com.teleconsulting.demo.exception.UserNotFoundException;
import com.teleconsulting.demo.model.Doctor;
import com.teleconsulting.demo.model.Patient;
import com.teleconsulting.demo.repository.DoctorRepository;
import com.teleconsulting.demo.service.DoctorService;
import com.teleconsulting.demo.service.PatientService;
import com.teleconsulting.demo.service.RoomJoinRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.*;



@RestController
@RequestMapping("/doctor")
public class    DoctorController {
    private final DoctorService doctorService;
    private final DoctorRepository doctorRepository;
    private final PatientService patientService;

    public DoctorController(DoctorService doctorService, DoctorRepository doctorRepository, PatientService patientService) {
        this.doctorService = doctorService;
        this.doctorRepository = doctorRepository;
        this.patientService = patientService;
    }
    @GetMapping("/supervisor/{supervisorId}") // List of Doc under Sr Doc
    public ResponseEntity<List<Doctor>> getDoctorsBySupervisorId(@PathVariable("supervisorId") Long supervisorId) {
        List<Doctor> doctors = doctorService.getDoctorsBySupervisorId(supervisorId);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }
    @GetMapping("/doctor/{id}") // Return Doc details from its id
    Doctor getUserById(@PathVariable Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
    @PostMapping("/join-room") // Update incoming call
    public ResponseEntity<?> joinRoom(@RequestBody RoomJoinRequest request) {
        Doctor doctor = doctorService.findByPhoneNumber(request.getDoctorPhoneNumber());
        if (doctor != null) {
            doctor.setIncomingCall(request.getPatientPhoneNumber());
            doctorService.saveDoctor(doctor);
            return ResponseEntity.ok("Incoming Call status updated");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
    }


    @GetMapping("/{doctorId}/incoming-call") // Return Incoming call
    public ResponseEntity<?> getIncomingCall(@PathVariable Long doctorId) {
        Doctor doctor = doctorService.findById(doctorId);
        if (doctor != null) {
            return ResponseEntity.ok(doctor.getIncomingCall());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
    }
    @GetMapping("/{doctorId}") // Return Doc phone number by its ID
    public ResponseEntity<String> getDoctorById(@PathVariable Long doctorId) {
        Doctor doctor = doctorService.findById(doctorId);
        if (doctor != null) {
            return ResponseEntity.ok(doctor.getPhoneNumber());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{doctorId}/reject-call") // Update Incoming Call to Null after end call
    public ResponseEntity<?> rejectCall(@PathVariable("doctorId") Long doctorId) {
        Doctor doctor = doctorService.findById(doctorId);
        if (doctor != null) {
            doctor.setIncomingCall(null);
            doctorService.saveDoctor(doctor);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
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
