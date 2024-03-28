package com.teleconsulting.demo.controller;

import com.teleconsulting.demo.model.Doctor;
import com.teleconsulting.demo.security.JwtService;
import com.teleconsulting.demo.service.DoctorService;
import com.teleconsulting.demo.service.PatientService;
import com.teleconsulting.demo.service.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.print.Doc;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public AdminController(PatientService patientService, DoctorService doctorService, AuthenticationManager authenticationManager, JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }
    @PostMapping("/register/doctor")
    public ResponseEntity<?> RegisterDoctor(@RequestBody Doctor doctor) {
        return ResponseEntity.ok(doctorService.saveNewDoctor(doctor));
    }
}
