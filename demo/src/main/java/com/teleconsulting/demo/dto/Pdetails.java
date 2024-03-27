package com.teleconsulting.demo.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pdetails {
    private String name;
    private String gender;

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
}
