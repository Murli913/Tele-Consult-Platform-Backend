package com.teleconsulting.demo.service;

import com.teleconsulting.demo.exception.UserNotFoundException;
import com.teleconsulting.demo.model.Doctor;
import com.teleconsulting.demo.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService{
    @Autowired
    private DoctorRepository doctorRepository;
    @Override
    public List<Doctor> getDoctorsBySupervisorId(Long supervisorId) {
        return doctorRepository.findBySupervisorDoctorId(supervisorId);
    }

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
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
    public void deleteDoctorById(Long id) {
        if(!doctorRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }
        doctorRepository.deleteById(id);
    }
}
