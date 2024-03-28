package com.teleconsulting.demo.controller;

import com.teleconsulting.demo.exception.UserNotFoundException;
import com.teleconsulting.demo.model.Doctor;
import com.teleconsulting.demo.repository.DoctorRepository;
import com.teleconsulting.demo.service.DoctorService;
import com.teleconsulting.demo.service.RoomJoinRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.*;



@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DoctorRepository doctorRepository;
    @GetMapping("/supervisor/{supervisorId}") // List of Doc under Sr Doc
    public ResponseEntity<List<Doctor>> getDoctorsBySupervisorId(@PathVariable("supervisorId") Long supervisorId) {
        List<Doctor> doctors = doctorService.getDoctorsBySupervisorId(supervisorId);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }



    @PostMapping("/add") // Move to Admin
    public String add(@RequestBody Doctor doctor)
    {
        doctorService.saveDoctor(doctor);
        return "New Doctor Added";
    }

    // "localhost:8081/doctor/"
    @GetMapping("/getdoctor") // Move to Admin
    List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }
    @DeleteMapping("/doctor/{id}") // Move to Admin
    String deleteDoctor(@PathVariable Long id){
        try{
            doctorService.deleteDoctorById(id);
            return  "Doctor with id "+id+" has been deleted success.";
        }catch (UserNotFoundException e){
            return "Doctor not found with id"+id;
        }

    }
    @GetMapping("/doctor/{id}") // Return Doc details from its id
    Doctor getUserById(@PathVariable Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
    @PutMapping("/doctors/{id}") // Move to Admin
    Doctor updateDoctor(@RequestBody Doctor newDoctor, @PathVariable Long id) {
        return doctorRepository.findById(id)
                .map(Doctor -> {
                    Doctor.setGender(newDoctor.getGender());
                    Doctor.setName(newDoctor.getName());
                    Doctor.setPhoneNumber(newDoctor.getPhoneNumber());

                    return doctorRepository.save(Doctor);
                }).orElseThrow(() -> new UserNotFoundException(id));
    }

    @PostMapping("/join-room") // Update incoming call
    public ResponseEntity<?> joinRoom(@RequestBody RoomJoinRequest request) {
        Doctor doctor = doctorService.findByPhoneNumber(request.getDoctorPhoneNumber());
        if (doctor != null) {
            doctor.setIncomingCall(request.getPatientPhoneNumber());
            doctorService.saveDoctor(doctor);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
    }


    @GetMapping("/{doctorId}/incoming-call") // Return Incoming call
    public ResponseEntity<?> getIncomingCall(@PathVariable Long doctorId) {
        Doctor doctor = doctorService.findById(doctorId);
        if (doctor != null) {
            return ResponseEntity.ok(doctor.getIncomingCall());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
    }
    @GetMapping("/{doctorId}") // Return Doc phone number by its ID
    public ResponseEntity<String> getDoctorById(@PathVariable Long doctorId) {
        Doctor doctor = doctorService.findById(doctorId);
        if (doctor != null) {
            return ResponseEntity.ok(doctor.getPhoneNumber());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{doctorId}/reject-call") // Update Incoming Call to Null after end call
    public ResponseEntity<?> rejectCall(@PathVariable("doctorId") Long doctorId) {
        Doctor doctor = doctorService.findById(doctorId);
        if (doctor != null) {
            doctor.setIncomingCall(null);
            doctorService.saveDoctor(doctor);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
}
}
}
