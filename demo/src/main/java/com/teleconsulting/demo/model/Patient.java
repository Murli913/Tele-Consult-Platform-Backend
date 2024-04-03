package com.teleconsulting.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String gender;
    @NonNull
    private String phoneNumber;
    private String email;
    private String password;
    @Enumerated(value = EnumType.STRING)
    Role role;
}
