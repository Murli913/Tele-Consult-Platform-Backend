package com.teleconsulting.demo.controller;

import com.teleconsulting.demo.exception.UserNotFoundException;
import com.teleconsulting.demo.model.Appointment;
import com.teleconsulting.demo.model.Patient;
import com.teleconsulting.demo.repository.AppointmentRepository;
import com.teleconsulting.demo.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/appointment")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private AppointmentRepository appointmentRepository;

     @PostMapping("/add")
    public String add(@RequestBody Appointment appointment)
     {
         appointmentService.saveAppointment(appointment);
         return "New Appointment added";
     }

    @GetMapping("/appointment")
    List<Appointment> getAllAppointment() {
        return appointmentRepository.findAll();
    }
    @GetMapping("/appointment/{id}")
    Appointment getUserById(@PathVariable Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
    @DeleteMapping("/appointment/{id}")
    String deleteAppointment(@PathVariable Long id){
        if(!appointmentRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }
        appointmentRepository.deleteById(id);
        return  "Appointment with id "+id+" has been deleted success.";
    }
    @PutMapping("/appointment/{id}")
    Appointment updateAppointment(@RequestBody Appointment newAppointment, @PathVariable Long id) {
        return appointmentRepository.findById(id)
                .map(Appointment -> {
                    Appointment.setPatientname(newAppointment.getPatientname());
                    Appointment.setDoctorname(newAppointment.getDoctorname());
                    Appointment.setApttime(newAppointment.getApttime());
                    Appointment.setDptdate(newAppointment.getDptdate());
                    return appointmentRepository.save(Appointment);
                }).orElseThrow(() -> new UserNotFoundException(id));
    }
}
