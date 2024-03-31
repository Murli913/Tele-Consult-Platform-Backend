package com.teleconsulting.demo.service;
import com.teleconsulting.demo.exception.UserNotFoundException;
import com.teleconsulting.demo.model.AuthenticationResponse;
import com.teleconsulting.demo.model.Doctor;

import java.util.List;

public interface DoctorService{
    Doctor saveDoctor(Doctor doctor);
    AuthenticationResponse saveNewDoctor(Doctor doctor);
    List<Doctor> getAllDoctors();
    Doctor findByPhoneNumber(String phoneNumber);
    Doctor findById(Long id);
    Doctor updateDoctorIncomingCall(String doctorPhoneNumber, String patientPhoneNumber);
    Doctor updateDoctor(Long id,Doctor doctor);
    void deleteDoctorById(Long id) throws UserNotFoundException;
    List<Doctor> getDoctorsBySupervisorId(Long supervisorId);

}
