package com.teleconsulting.demo.service;

import com.teleconsulting.demo.exception.UserNotFoundException;
import com.teleconsulting.demo.model.AuthenticationResponse;
import com.teleconsulting.demo.model.Doctor;
import com.teleconsulting.demo.model.Role;
import com.teleconsulting.demo.repository.DoctorRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService{

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
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
        Doctor doctor2 = new Doctor();
        doctor2 = doctorRepository.findByEmail(doctor.getEmail()).orElse(null);
        if(doctor2 == null)
        {
            Doctor doctor1 = new Doctor();
            doctor1.setName(doctor.getName());
            doctor1.setEmail(doctor.getEmail());
            doctor1.setGender(doctor.getGender());
            doctor1.setPassword(passwordEncoder.encode(doctor.getPassword()));
            doctor1.setPhoneNumber(doctor.getPhoneNumber());
            doctor1.setRole(Role.valueOf(Role.DOCTOR.toString()));
            if (doctor.getSupervisorDoctor() != null && doctor.getSupervisorDoctor().getId() != null) {
                Doctor supervisorDoctor = doctorRepository.findById(doctor.getSupervisorDoctor().getId()).orElse(null);
                doctor1.setSupervisorDoctor(supervisorDoctor);
            }
            doctor1.setIncomingCall(null);
            doctorRepository.save(doctor1);
            return new AuthenticationResponse(null, "Doctor Registration was Successful");
        }
        else
            return new AuthenticationResponse(null, "Email ID already exist!!");
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
    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        System.out.println("\nUpdated DOc"+updatedDoctor+"\n");
        existingDoctor.setName(updatedDoctor.getName());
        existingDoctor.setGender(updatedDoctor.getGender());
        existingDoctor.setPhoneNumber(updatedDoctor.getPhoneNumber());
        existingDoctor.setEmail(updatedDoctor.getEmail());
        existingDoctor.setPassword(passwordEncoder.encode(updatedDoctor.getPassword()));
        existingDoctor.setRole(Role.valueOf(Role.DOCTOR.toString()));
        existingDoctor.setSupervisorDoctor(updatedDoctor.getSupervisorDoctor());
        doctorRepository.save(existingDoctor);
        // Save the updated doctor entity
        return existingDoctor;
    }

    @Override
    public void deleteDoctorById(Long id) {
        if(!doctorRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }
        doctorRepository.deleteById(id);
    }
}
