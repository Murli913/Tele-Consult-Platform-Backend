package com.teleconsulting.demo.service;

import com.teleconsulting.demo.model.Doctor;
import com.teleconsulting.demo.model.Patient;
import com.teleconsulting.demo.repository.DoctorRepository;
import com.teleconsulting.demo.repository.PatientRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserDetailsServiceImpl(DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("\nInside UserDetailsServiceImpl\nUsername is :::::: "+username+"\n");
        if(username.equals("admin@gmail.com")) {
            return org.springframework.security.core.userdetails.User.withUsername(username)
                    .password(passwordEncoder.encode("password"))
                    .roles("ADMIN")
                    .build();
        }
        Doctor doctor = doctorRepository.findByEmail(username).orElse(null);
        if(doctor != null){
            System.out.println("\n\n"+doctor);
            return org.springframework.security.core.userdetails.User.withUsername(doctor.getEmail())
                    .password(doctor.getPassword())
                    .roles(String.valueOf(doctor.getRole()))
                    .build();
        }
        Patient patient = patientRepository.findByEmail(username).orElse(null);
        if(patient != null){
            return org.springframework.security.core.userdetails.User.withUsername(patient.getEmail())
                    .password(patient.getPassword())
                    .roles(String.valueOf(patient.getRole()))
                    .build();
        }
        System.out.println("\nUser Not Found in UserDetailsServiceImple!!\n");
        throw new UsernameNotFoundException("USername Not Found!!");
    }
}
