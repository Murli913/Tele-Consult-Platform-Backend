package com.teleconsulting.demo.controller;

import com.teleconsulting.demo.dto.Pdetails;
import com.teleconsulting.demo.exception.UserNotFoundException;
import com.teleconsulting.demo.model.Doctor;
import com.teleconsulting.demo.model.Patient;
import com.teleconsulting.demo.repository.DoctorRepository;
import com.teleconsulting.demo.service.DoctorService;
import com.teleconsulting.demo.service.PatientService;
import com.teleconsulting.demo.service.RoomJoinRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.*;

@RestController
@RequestMapping("/doctor")
@CrossOrigin("http://localhost:5173")
public class    DoctorController {
    private final DoctorService doctorService;
    private final PatientService patientService;

    public DoctorController(DoctorService doctorService, PatientService patientService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
    }
    @GetMapping("/supervisor/{supervisorId}") // List of Doc under Sr Doc
    public ResponseEntity<List<Doctor>> getDoctorsBySupervisorId(@PathVariable("supervisorId") Long supervisorId) {
        List<Doctor> doctors = doctorService.getDoctorsUnderSeniorDoctor(supervisorId);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }
    @GetMapping("/doctor/{id}") //  Return Doc details from its id
    Doctor getUserById(@PathVariable Long id) {
        return doctorService.getById(id);
    }
    @PostMapping("/join-room") // Update incoming call
    public ResponseEntity<?> joinRoom(@RequestBody RoomJoinRequest request) {
        Doctor doctor = doctorService.findByPhoneNumber(request.getDoctorPhoneNumber());
        if (doctor != null) {
            doctor.setIncomingCall(request.getPatientPhoneNumber());
            doctorService.saveDoctor(doctor);
            return ResponseEntity.ok("Incoming Call status updated");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
    }
    @GetMapping("/{doctorId}/incoming-call") // Return Incoming call
    public ResponseEntity<?> getIncomingCall(@PathVariable Long doctorId) {
        Doctor doctor = doctorService.findById(doctorId);
        if (doctor != null && !doctor.isDeleteFlag()) {
            return ResponseEntity.ok(doctor.getIncomingCall());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
    }
    @GetMapping("/phone/{doctorId}") // Return Doc phone number by its ID
    public ResponseEntity<String> getDoctorById(@PathVariable Long doctorId) {
        Doctor doctor = doctorService.findById(doctorId);
        if (doctor != null && !doctor.isDeleteFlag()) {
            return ResponseEntity.ok(doctor.getPhoneNumber());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{doctorId}/reject-call") // Update Incoming Call to Null after end call
    public ResponseEntity<?> rejectCall(@PathVariable("doctorId") Long doctorId) {
        Doctor doctor = doctorService.findById(doctorId);
        if (doctor != null && !doctor.isDeleteFlag()) {
            doctor.setIncomingCall(null);
            doctorService.saveDoctor(doctor);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
    }
    @PutMapping("/{patientId}") // Doctor can access
    public ResponseEntity<Patient> updatePatient(@PathVariable Long patientId, @RequestBody Pdetails pdetails) {
        Patient patient = patientService.updatePatient(patientId, pdetails );
        if(!patient.isDeleteFlag()) {
            return ResponseEntity.ok(patient);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/pt/{patientId}") // Doctor can access
    public ResponseEntity<?> getPatientNameAndAge(@PathVariable Long patientId) {
        try {
            // Retrieve the patient information by patientId
            Patient patient = patientService.findById(patientId);

            // Check if the patient exists
            if (patient == null && patient.isDeleteFlag()) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found if patient is not found
            }

            // Create a DTO (Data Transfer Object) to send only name and age
            Map<String, Object> patientInfo = new HashMap<>();
            patientInfo.put("name", patient.getName());
            patientInfo.put("gender", patient.getGender());

            // Return the patient name and gender in the response body
            return ResponseEntity.ok(patientInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving patient information");

        }
    }
    //aziz
    @GetMapping("/doctor-details/{email}")
    public ResponseEntity<?> getUserDetailsByEmail(@PathVariable String email) {
        System.out.println("\n Inside getUserFrom Email \n" + email);
        Doctor userDetails = doctorService.getUserByEmail(email).orElse(null);
        if (userDetails != null && !userDetails.isDeleteFlag()) {
            return ResponseEntity.ok(userDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/fetchname/{doctorid}")
    public ResponseEntity<?> getName(@PathVariable Long doctorid) {
        Doctor doctor = doctorService.findById(doctorid);
        if (doctor != null && !doctor.isDeleteFlag()) {
            return ResponseEntity.ok(doctor.getName());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
    }

    @GetMapping("/id")
    public ResponseEntity<Patient> getPatientById(@RequestParam String phoneNumber) {
        Patient patient = patientService.getPatientByPhoneNumber(phoneNumber);
        if(patient != null && !patient.isDeleteFlag()) {
            return ResponseEntity.ok(patient);
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping(params = "phoneNumber") // Get patient details from phone number
    public ResponseEntity<Patient> getPatientByPhoneNumber(@RequestParam String phoneNumber) {
        Patient patient = patientService.getPatientByPhoneNumber(phoneNumber);
        if(patient != null && !patient.isDeleteFlag()) {
            return ResponseEntity.ok(patient);
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/alldoctors")
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @PostMapping("/addpt")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient createdPatient = patientService.createPatient(patient);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
}
    //murli
    @GetMapping("/all")
    public ResponseEntity<List<Doctor>> getAllDoctorsExceptPassword() {
        List<Doctor> doctors = doctorService.getAllDoctorsExceptPassword();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }
    //murli
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long id) {
        try {
            doctorService.deleteDoctorById(id);
            return ResponseEntity.ok("Doctor with ID " + id + " has been deleted.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found with ID: " + id);
        }
    }

    //murli
    @GetMapping("/count")
    public ResponseEntity<Long> countDoctors() {
        Long count = doctorService.countDoctors();
        return ResponseEntity.ok(count);
    }
    //murli
    @GetMapping("/under-senior/{seniorDoctorId}")
    public ResponseEntity<List<Doctor>> getDoctorsUnderSeniorDoctor(@PathVariable("seniorDoctorId") Long seniorDoctorId) {
        List<Doctor> doctors = doctorService.getDoctorsUnderSeniorDoctor(seniorDoctorId);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }
    //murli
    @PutMapping("/updateDoctor/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @RequestBody Doctor updatedDoctor) {
        try {
            Doctor doctor = doctorService.updateDoctors(id, updatedDoctor);
            return ResponseEntity.ok(doctor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //murli
    @PutMapping("/{doctorId}/update-sdid/{newSdid}")
    public ResponseEntity<?> updateDoctorSdid(@PathVariable Long doctorId, @PathVariable Long newSdid) {
        doctorService.updateDoctorSdid(doctorId, newSdid);
        return ResponseEntity.ok("Supervisor Doctor ID updated successfully");
    }
    //murli
    @GetMapping("/by-email/{email}")
    public ResponseEntity<Long> getDoctorIdByEmail(@PathVariable String email) {
        Doctor doctor = doctorService.findByEmail(email);
        if (doctor != null) {
            return ResponseEntity.ok(doctor.getId());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //murli
    @GetMapping("/{doctorId}/details")
    public ResponseEntity<?> getDoctorNameAndPhoneNumber(@PathVariable Long doctorId) {
        Doctor doctor = doctorService.getDoctorNameAndPhoneNumber(doctorId);
        if (doctor != null) {
            return ResponseEntity.ok(doctor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
