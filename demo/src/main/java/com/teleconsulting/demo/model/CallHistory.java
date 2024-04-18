package com.teleconsulting.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CallHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "did")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "pid")
    private Patient patient;

    private LocalDate callDate;
    private LocalTime callTime;
    private String prescription;
    private LocalTime endTime;
    private String reason;
}
