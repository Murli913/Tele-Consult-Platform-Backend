package com.teleconsulting.demo.service;


import com.teleconsulting.demo.model.CallHistory;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendAppointmentNotification(CallHistory appointment) throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("azizrocky1951@gmail.com");
        message.setTo(appointment.getDoctor().getEmail());
        message.setSubject("Appointment Notification");
        message.setText("Dear " + appointment.getDoctor().getName() + ",\n\n"
                + "This is a reminder of your appointment scheduled for today.\n\n"
                + "Appointment details:\n"
                + "Date: " + appointment.getCallDate() + "\n"
                + "Time: " + appointment.getCallTime() + "\n"
                + "Thank you.");

        mailSender.send(message);
}
}
