package com.teleconsulting.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Date;
import java.sql.Time;

@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String patientname;
    private String doctorname;
    private Time apttime;
    private Date dptdate;

    public Appointment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientname() {
        return patientname;
    }

    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }

    public String getDoctorname() {
        return doctorname;
    }

    public void setDoctorname(String doctorname) {
        this.doctorname = doctorname;
    }

    public Time getApttime() {
        return apttime;
    }

    public void setApttime(Time apttime) {
        this.apttime = apttime;
    }

    public Date getDptdate() {
        return dptdate;
    }

    public void setDptdate(Date dptdate) {
        this.dptdate = dptdate;
    }
}
