package com.teleconsulting.demo.repository;

import com.teleconsulting.demo.model.AdminLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminLoginRepository extends JpaRepository<AdminLogin,Long> {
    AdminLogin findByEmail(String email);
}
