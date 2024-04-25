package com.teleconsulting.demo.service;

import com.teleconsulting.demo.model.Doctor;
import com.teleconsulting.demo.model.Patient;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
public class CallHandlingService {

    private final DoctorService doctorService;
    public static Queue<Doctor> availableDoctorsQueue = new LinkedList<>();
    public static Queue<Patient> waitingPatientsQueue = new LinkedList<>();
    public CallHandlingService(DoctorService doctorService) {
        this.doctorService = doctorService;
    }
    public void addAvailableDoctor(Doctor doctor) {
        availableDoctorsQueue.add(doctor);
    }
    public void addAllAvailableDoctors(List<Doctor> doctors) {
        availableDoctorsQueue.addAll(doctors);
    }
    public Doctor getAvailableDoctor() {
        return availableDoctorsQueue.poll();
    }
    public void addWaitingPatient(Patient patient) {
        waitingPatientsQueue.add(patient);
    }
    public Patient getWaitingPatient() {
        return waitingPatientsQueue.poll();
    }
    @PostConstruct
    public void initializeAvailableDoctorsQueue() {
        List<Doctor> availableDoctors = doctorService.findAllAvailableDoctors();
        if(availableDoctors != null) {
            System.out.println("Available Doctors:");
            for (Doctor doctor : availableDoctors) {
                System.out.println(doctor);
            }
            addAllAvailableDoctors(availableDoctors);
        }
    }
}