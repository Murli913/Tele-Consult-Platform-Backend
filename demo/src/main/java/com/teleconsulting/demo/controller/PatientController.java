package com.teleconsulting.demo.controller;

import com.teleconsulting.demo.exception.UserNotFoundException;
import com.teleconsulting.demo.model.Doctor;
import com.teleconsulting.demo.model.Patient;
import com.teleconsulting.demo.repository.DoctorRepository;
import com.teleconsulting.demo.repository.PatientRepository;
import com.teleconsulting.demo.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    private PatientService patientService;
    @Autowired
    private PatientRepository patientRepository;
    @PostMapping("/add")
    public String add(@RequestBody Patient patient)
    {
        patientService.savePatient(patient);
        return "New Patient Added";
    }
    @GetMapping("/patient")
    List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
    @GetMapping("/patient/{id}")
    Patient getUserById(@PathVariable Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
    @DeleteMapping("/patient/{id}")
    String deletePatient(@PathVariable Long id){
        if(!patientRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }
        patientRepository.deleteById(id);
        return  "Patient with id "+id+" has been deleted success.";
    }
    @PutMapping("/patient/{id}")
    Patient updatePatient(@RequestBody Patient newPatient, @PathVariable Long id) {
        return patientRepository.findById(id)
                .map(Patient -> {
                    Patient.setGender(newPatient.getGender());
                    Patient.setName(newPatient.getName());
                    Patient.setPhoneNumber(newPatient.getPhoneNumber());

                    return patientRepository.save(Patient);
                }).orElseThrow(() -> new UserNotFoundException(id));
    }


}
