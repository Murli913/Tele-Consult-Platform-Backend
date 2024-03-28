package com.teleconsulting.demo.service;

import com.teleconsulting.demo.model.Admin;
import com.teleconsulting.demo.model.AuthenticationResponse;
import com.teleconsulting.demo.model.Role;
import com.teleconsulting.demo.repository.AdminRepository;
import com.teleconsulting.demo.security.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AdminServiceImpl implements AdminService{
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AdminRepository adminRepository;
    private final JwtService jwtService;

    public AdminServiceImpl(AdminRepository adminRepository, JwtService jwtService) {
        this.adminRepository = adminRepository;
        this.jwtService = jwtService;
    }

    @Override
    public AuthenticationResponse saveNewAdmin(Admin admin) {
        System.out.println("\n Add New Admin "+admin+"\n");
        Admin admin1 = new Admin();
        admin1.setEmail(admin.getUsername());
        admin1.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin1.setRole(Role.valueOf("ADMIN"));
        admin1 = adminRepository.save(admin1);
        String jwt = jwtService.generateToken(admin1);
        return new AuthenticationResponse(jwt, "Admin Registration was Successful");
    }
}
