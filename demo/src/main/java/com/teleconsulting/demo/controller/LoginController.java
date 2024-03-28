package com.teleconsulting.demo.controller;

import com.teleconsulting.demo.model.AuthenticationRequest;
import com.teleconsulting.demo.model.AuthenticationResponse;
import com.teleconsulting.demo.model.Patient;
import com.teleconsulting.demo.security.JwtService;
import com.teleconsulting.demo.service.PatientService;
import com.teleconsulting.demo.service.UserDetailsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController {
    private final PatientService patientService;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final UserDetailsServiceImpl userDetailsService;

    public LoginController(PatientService patientService, AuthenticationManager authenticationManager, JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
        this.patientService = patientService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword());
        try{
            authenticationManager.authenticate(authenticationToken);
        }catch (BadCredentialsException e){
            throw new RuntimeException("Invalid Username or Password");
        }
//        Doctor existingDoctor = doctorService.findByPhoneNumber(doctor.getPhoneNumber());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.jwtService.generateToken(userDetails);
        AuthenticationResponse response = AuthenticationResponse.builder().token(token).message(userDetails.getUsername()).build();
        return new ResponseEntity<>(response,HttpStatus.OK);
//        if (existingDoctor != null && existingDoctor.getPassword().equals(doctor.getPassword())) {
//
//            Long doctorId = existingDoctor.getId();
//            return ResponseEntity.ok().body(Map.of("doctorId", doctorId));
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid phone number or password");
//        }
    }

    @PostMapping("/register/patient")
    public ResponseEntity<?> RegisterPatient(@RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.saveNewPatient(patient));
    }
}
