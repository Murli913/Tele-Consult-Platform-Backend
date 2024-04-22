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

    // Queue to store available doctors
    private final DoctorService doctorService;
    public static Queue<Doctor> availableDoctorsQueue = new LinkedList<>();

    // Queue to store waiting patients
    public static Queue<Patient> waitingPatientsQueue = new LinkedList<>();

    public CallHandlingService(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // Method to add a doctor to the available doctors queue
    public void addAvailableDoctor(Doctor doctor) {
        availableDoctorsQueue.add(doctor);
    }

    public void addAllAvailableDoctors(List<Doctor> doctors) {
        availableDoctorsQueue.addAll(doctors);
    }

    // Method to remove and return the first available doctor from the queue
    public Doctor getAvailableDoctor() {
        return availableDoctorsQueue.poll();
    }

    // Method to add a patient to the waiting patients queue
    public void addWaitingPatient(Patient patient) {
        waitingPatientsQueue.add(patient);
    }

    // Method to remove and return the first waiting patient from the queue
    public Patient getWaitingPatient() {
        return waitingPatientsQueue.poll();
    }

    @PostConstruct
    public void initializeAvailableDoctorsQueue() {
        List<Doctor> availableDoctors = doctorService.findAllAvailableDoctors();
        System.out.println("Available Doctors:");
        for (Doctor doctor : availableDoctors) {
            System.out.println(doctor);
        }
        addAllAvailableDoctors(availableDoctors);
    }

    // Other methods and logic for call handling...
}