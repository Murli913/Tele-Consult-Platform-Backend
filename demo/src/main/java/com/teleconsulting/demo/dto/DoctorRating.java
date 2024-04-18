package com.teleconsulting.demo.dto;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorRating {
    Long id;
    int rating;
}