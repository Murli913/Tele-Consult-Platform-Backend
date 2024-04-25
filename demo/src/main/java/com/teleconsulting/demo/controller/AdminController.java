package com.teleconsulting.demo.controller;

import com.teleconsulting.demo.dto.RegDoc;
import com.teleconsulting.demo.exception.UserNotFoundException;
import com.teleconsulting.demo.model.Doctor;
import com.teleconsulting.demo.model.Patient;
import com.teleconsulting.demo.repository.PatientRepository;
import com.teleconsulting.demo.service.DoctorService;
import com.teleconsulting.demo.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin("http://localhost:5173")
public class AdminController {
    private final PatientService patientService;
    private final DoctorService doctorService;
    public AdminController(PatientService patientService, DoctorService doctorService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
    }
    @PostMapping("/register/doctor")
    public ResponseEntity<?> RegisterDoctor(@RequestBody RegDoc regDoc) {
        System.out.println("\nInside Admin Controller Register Doc\n");
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
        return patientService.findAll();
    }
    @DeleteMapping("/patient/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable Long id){
        patientService.deletePatient(id);
        return ResponseEntity.ok("Patient with id "+id+" has been deleted success.");
    }
}
