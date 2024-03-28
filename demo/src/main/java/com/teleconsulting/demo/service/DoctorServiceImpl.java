package com.teleconsulting.demo.service;

import com.teleconsulting.demo.exception.UserNotFoundException;
import com.teleconsulting.demo.model.AuthenticationResponse;
import com.teleconsulting.demo.model.Doctor;
import com.teleconsulting.demo.model.Role;
import com.teleconsulting.demo.repository.DoctorRepository;
import com.teleconsulting.demo.security.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService{

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtService jwtService;

    public DoctorServiceImpl(DoctorRepository doctorRepository, JwtService jwtService) {
        this.doctorRepository = doctorRepository;
        this.jwtService = jwtService;
    }

    @Override
    public List<Doctor> getDoctorsBySupervisorId(Long supervisorId) {
        return doctorRepository.findBySupervisorDoctorId(supervisorId);
    }

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }
    @Override
    public AuthenticationResponse saveNewDoctor(Doctor doctor) {
        System.out.println("\n Add New Doctor "+doctor+"\n");
        Doctor doctor1 = new Doctor();
        doctor1.setName(doctor.getName());
        doctor1.setEmail(doctor.getUsername());
        doctor1.setGender(doctor.getGender());
        doctor1.setPassword(passwordEncoder.encode(doctor.getPassword()));
        doctor1.setPhoneNumber(doctor.getPhoneNumber());
        doctor1.setRole(Role.valueOf("DOCTOR"));
        doctor1 = doctorRepository.save(doctor1);
        String jwt = jwtService.generateToken(doctor1);
        return new AuthenticationResponse(jwt, "Doctor Registration was Successful");
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
    @Override
    public Doctor findByPhoneNumber(String phoneNumber) {
        return doctorRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Doctor findById(Long id) {
        return doctorRepository.findById(id).orElse(null);
    }

    @Override
    public Doctor updateDoctorIncomingCall(String doctorPhoneNumber, String patientPhoneNumber) {
        Doctor doctor = doctorRepository.findByPhoneNumber(doctorPhoneNumber);
        if (doctor != null) {
            doctor.setIncomingCall(patientPhoneNumber);
            return doctorRepository.save(doctor);
        } else {
            return null; // Handle doctor not found scenario
        }
    }


    @Override
    public void deleteDoctorById(Long id) {
        if(!doctorRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }
        doctorRepository.deleteById(id);
    }
}
