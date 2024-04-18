package com.teleconsulting.demo.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

public class CallDTO {

    private Long doctorId;
    private String doctorName;
    private String doctorGender;
    private String doctorPhoneNumber;
    private String doctorEmail;
    private LocalDate callDate;
    private LocalTime callTime;
    private String prescription;
    private LocalTime endTime;

    public Long getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getDoctorGender() {
        return doctorGender;
    }

    public String getDoctorPhoneNumber() {
        return doctorPhoneNumber;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public LocalDate getCallDate() {
        return callDate;
    }

    public LocalTime getCallTime() {
        return callTime;
    }

    public String getPrescription() {
        return prescription;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}
