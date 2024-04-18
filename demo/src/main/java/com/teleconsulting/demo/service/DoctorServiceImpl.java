package com.teleconsulting.demo.service;

import com.teleconsulting.demo.dto.Ddetails;
import com.teleconsulting.demo.dto.RegDoc;
import com.teleconsulting.demo.exception.UserNotFoundException;
import com.teleconsulting.demo.dto.AuthenticationResponse;
import com.teleconsulting.demo.model.Doctor;
import com.teleconsulting.demo.model.Role;
import com.teleconsulting.demo.repository.DoctorRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<Ddetails> getSnrDoctors() {
        List<Doctor> doctors = doctorRepository.findBySupervisorDoctorIsNull();
        return doctors.stream()
                .map(doctor -> {
                    Ddetails doctorDetails = new Ddetails();
                    doctorDetails.setId(doctor.getId());
                    doctorDetails.setName(doctor.getName());
                    return doctorDetails;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void updateRating(Long id, int rating) {
        Doctor doctor = doctorRepository.findById(id).orElse(null);
        if(doctor != null)
        {
            int tempRating = doctor.getTotalRating();
            tempRating += rating;
            int tempCount = doctor.getAppointmentCount();
            tempCount++;
            doctor.setTotalRating(tempRating);
            doctor.setAppointmentCount(tempCount);
            doctorRepository.save(doctor);
        }
    }

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }
    @Override
    public AuthenticationResponse saveNewDoctor(RegDoc regDoc) {
        Doctor doctor2 = new Doctor();
        doctor2 = doctorRepository.findByEmail(regDoc.getEmail()).orElse(null);
        if(doctor2 == null)
        {
            Doctor doctor1 = new Doctor();
            doctor1.setName(regDoc.getName());
            doctor1.setEmail(regDoc.getEmail());
            doctor1.setGender(regDoc.getGender());
            doctor1.setPassword(passwordEncoder.encode(regDoc.getPassword()));
            doctor1.setPhoneNumber(regDoc.getPhoneNumber());
            System.out.println(regDoc.getSupervisorDoctor()+"\n");
            if(regDoc.getSupervisorDoctor() == null)
            {
                System.out.println("Hello Sr Doc \n");
                doctor1.setRole(Role.valueOf(Role.SRDOC.toString()));
                System.out.println("\nRole is "+Role.valueOf(Role.SRDOC.toString()));
            }
            else
            {
                doctor1.setRole(Role.valueOf(Role.DOCTOR.toString()));
            }
            if (regDoc.getSupervisorDoctor() != null) {
                Doctor supervisorDoctor = doctorRepository.findById(regDoc.getSupervisorDoctor()).orElse(null);
                doctor1.setSupervisorDoctor(supervisorDoctor);
            }
            else
            {
                System.out.println("Super is set to null\n");
                doctor1.setSupervisorDoctor(null);
            }
            doctor1.setIncomingCall(null);
            System.out.println(doctor1.getSupervisorDoctor());
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
    public List<Doctor> getAllSrDoctors() {
        return doctorRepository.findAllSrDoc();
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
