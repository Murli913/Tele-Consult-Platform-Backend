package com.teleconsulting.demo.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ddetails {
    private Long id;
    private String name;
    private String gender;
    private String phoneNumber;
    private String email;
    private Float totalRating;
    private int appointmentCount;
}