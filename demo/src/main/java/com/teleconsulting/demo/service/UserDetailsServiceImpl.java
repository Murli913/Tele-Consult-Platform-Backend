package com.teleconsulting.demo.service;

import com.teleconsulting.demo.model.Admin;
import com.teleconsulting.demo.model.Doctor;
import com.teleconsulting.demo.model.Patient;
import com.teleconsulting.demo.repository.AdminRepository;
import com.teleconsulting.demo.repository.DoctorRepository;
import com.teleconsulting.demo.repository.PatientRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AdminRepository adminRepository;

    public UserDetailsServiceImpl(DoctorRepository doctorRepository, PatientRepository patientRepository, AdminRepository adminRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("\nUsername is :::::: "+username+"\n");
        Doctor doctor = doctorRepository.findByEmail(username).orElse(null);
        if(doctor != null){
            System.out.println("\n\n"+doctor);
            return doctor;
        }
        Patient patient = patientRepository.findByEmail(username).orElse(null);
        if(patient != null){
            return patient;
        }
        Admin admin = adminRepository.findByEmail(username).orElse(null);
        if(admin != null) {
            return admin;
        }
        System.out.println("\nUser Not Found in UserDetailsServiceImple!!\n");
        throw new UsernameNotFoundException("USername Not Found!!");
    }
}
