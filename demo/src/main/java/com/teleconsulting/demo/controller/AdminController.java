package com.teleconsulting.demo.controller;

import com.teleconsulting.demo.dto.RegDoc;
import com.teleconsulting.demo.exception.UserNotFoundException;
import com.teleconsulting.demo.model.Doctor;
import com.teleconsulting.demo.model.Patient;
import com.teleconsulting.demo.repository.PatientRepository;
import com.teleconsulting.demo.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;

    public AdminController(PatientRepository patientRepository, DoctorService doctorService) {
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<?> RegisterDoctor(@RequestBody RegDoc regDoc) {
        return ResponseEntity.ok(doctorService.saveNewDoctor(regDoc));
    }
    @GetMapping("/getSrDoctors")
    List<Doctor> getAllSrDoctors(){
        return doctorService.getAllSrDoctors();
    }
    @GetMapping("/getdoctors")
    List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }
    @DeleteMapping("/doctor/{id}")
    String deleteDoctor(@PathVariable Long id){
        try{
            doctorService.deleteDoctorById(id);
            return  "Doctor with id "+id+" has been deleted success.";
        }catch (UserNotFoundException e){
            return "Doctor not found with id"+id;
        }
    }
    @PutMapping("/doctors/{id}")
    public ResponseEntity<?> updateDoctor(@RequestBody Doctor newDoctor, @PathVariable Long id) {
        System.out.println("\nInto admin controller");
        return ResponseEntity.ok(doctorService.updateDoctor(id,newDoctor));
    }
    @GetMapping("/patient")
    List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
    @DeleteMapping("/patient/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable Long id){
        if(!patientRepository.existsById(id)){
            return ResponseEntity.ok("Patient ID not found");
        }
        patientRepository.deleteById(id);
        return ResponseEntity.ok("Patient with id "+id+" has been deleted success.");
    }
}
