package com.teleconsulting.demo.controller;

import com.teleconsulting.demo.exception.UserNotFoundException;
import com.teleconsulting.demo.model.Doctor;
import com.teleconsulting.demo.model.Patient;
import com.teleconsulting.demo.repository.DoctorRepository;
import com.teleconsulting.demo.repository.PatientRepository;
import com.teleconsulting.demo.security.JwtService;
import com.teleconsulting.demo.service.DoctorService;
import com.teleconsulting.demo.service.PatientService;
import com.teleconsulting.demo.service.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final PatientService patientService;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final DoctorRepository doctorRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public AdminController(PatientService patientService, PatientRepository patientRepository, DoctorService doctorService, DoctorRepository doctorRepository, AuthenticationManager authenticationManager, JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
        this.patientService = patientService;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.doctorRepository = doctorRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }
    @PostMapping("/register/doctor")
    public ResponseEntity<?> RegisterDoctor(@RequestBody Doctor doctor) {
        return ResponseEntity.ok(doctorService.saveNewDoctor(doctor));
    }
    @GetMapping("/getdoctors")
    @PreAuthorize("hasRole('ADMIN')")
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
    Doctor updateDoctor(@RequestBody Doctor newDoctor, @PathVariable Long id) {
        System.out.println("\nInto admin controller");
        return doctorService.updateDoctor(id,newDoctor);
    }
    @GetMapping("/patient")
    List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
    @DeleteMapping("/patient/{id}")
    String deletePatient(@PathVariable Long id){
        if(!patientRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }
        patientRepository.deleteById(id);
        return  "Patient with id "+id+" has been deleted success.";
    }
}
