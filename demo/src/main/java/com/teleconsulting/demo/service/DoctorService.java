package com.teleconsulting.demo.service;
import com.teleconsulting.demo.dto.Ddetails;
import com.teleconsulting.demo.dto.DoctorRating;
import com.teleconsulting.demo.dto.RegDoc;
import com.teleconsulting.demo.exception.UserNotFoundException;
import com.teleconsulting.demo.dto.AuthenticationResponse;
import com.teleconsulting.demo.model.Doctor;

import java.util.List;
import java.util.Optional;

import com.teleconsulting.demo.model.Doctor;
import org.springframework.stereotype.Service;


public interface DoctorService{
    Doctor getById(Long id);
    Doctor saveDoctor(Doctor doctor);
    AuthenticationResponse saveNewDoctor(RegDoc regDoc);
    List<Doctor> getAllDoctors();
    Doctor findByPhoneNumber(String phoneNumber);
    Doctor findById(Long id);
    Doctor updateDoctorIncomingCall(String doctorPhoneNumber, String patientPhoneNumber);
    Doctor updateDoctor(Long id,Doctor doctor);
    void deleteDoctorById(Long id) throws UserNotFoundException;
    List<Doctor> getDoctorsUnderSeniorDoctor(Long supervisorId);
   //murli
    List<Doctor> getAllDoctorsExceptPassword();
    //murli
    Long countDoctors();
    void updateRating(Long id, int rating);
    List<DoctorRating> getAllRatings();
    Doctor updateDoctors(Long id, Doctor updatedDoctor);
    void updateDoctorSdid(Long doctorId, Long newSdid);
    Doctor findByEmail(String email);
    Doctor getDoctorNameAndPhoneNumber(Long doctorId);
    List<Doctor> getAllSrDoctors();
    //AZIZ
    List<Doctor> findAllAvailableDoctors();
    Optional<Doctor> getUserByEmail(String token);
    //teja
    List<Ddetails> getSnrDoctors();
}
