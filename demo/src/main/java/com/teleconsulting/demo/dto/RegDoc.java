package com.teleconsulting.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegDoc {
    private String name;
    private String gender;
    private String phoneNumber;
    private String email;
    private String password;
    private Long supervisorDoctor;
}