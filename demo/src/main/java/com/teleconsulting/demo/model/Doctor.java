package com.teleconsulting.demo.model;

import jakarta.persistence.*;

@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String gender;
    private String phoneNumber;



    public Doctor getSupervisorDoctor() {
        return supervisorDoctor;
    }

    public void setSupervisorDoctor(Doctor supervisorDoctor) {
        this.supervisorDoctor = supervisorDoctor;
    }

    @ManyToOne
    @JoinColumn(name = "sdid")
    private Doctor supervisorDoctor;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
// Getters and setters
}
