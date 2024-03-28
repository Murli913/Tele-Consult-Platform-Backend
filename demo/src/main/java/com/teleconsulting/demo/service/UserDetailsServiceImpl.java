package com.teleconsulting.demo.service;

import com.teleconsulting.demo.model.Doctor;
import com.teleconsulting.demo.repository.DoctorRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final DoctorRepository doctorRepository;

    public UserDetailsServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("\nUsername is :::::: "+username+"\n");
        Doctor doctor = doctorRepository.findByEmail(username).orElse(null);

        if(doctor != null){
            return doctor;
        }
        System.out.println("\nUser Not Found in UserDetailsServiceImple!!\n");
        throw new UsernameNotFoundException("USername Not Found!!");
    }
}
