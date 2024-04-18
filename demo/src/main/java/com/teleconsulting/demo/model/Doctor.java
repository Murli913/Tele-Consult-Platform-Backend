package com.teleconsulting.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "supervisorDoctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String gender;
    @NonNull
    private String phoneNumber;
    @NonNull
    private String email;
    @NonNull
    private String password;
    private String incomingCall;
    @Enumerated(value = EnumType.STRING)
    Role role;
    @ManyToOne
    @JoinColumn(name = "sdid")
    private Doctor supervisorDoctor;
    private int totalRating;
    private int appointmentCount;
}
