package com.teleconsulting.demo.service;

import com.teleconsulting.demo.model.Appointment;

import java.util.List;

public interface AppointmentService {
    public Appointment saveAppointment(Appointment appointment);
    List<Appointment> getAppointmentsForToday();
}
