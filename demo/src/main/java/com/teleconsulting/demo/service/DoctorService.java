package com.teleconsulting.demo.service;
import com.teleconsulting.demo.exception.UserNotFoundException;
import com.teleconsulting.demo.model.Doctor;
import com.teleconsulting.demo.repository.DoctorRepository;

import java.util.List;

public interface DoctorService{
    public Doctor saveDoctor(Doctor doctor);

    List<Doctor> getAllDoctors();

    Doctor findByPhoneNumber(String phoneNumber);

    void deleteDoctorById(Long id) throws UserNotFoundException;


    List<Doctor> getDoctorsBySupervisorId(Long supervisorId);

}
