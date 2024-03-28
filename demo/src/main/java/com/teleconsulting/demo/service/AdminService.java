package com.teleconsulting.demo.service;

import com.teleconsulting.demo.model.Admin;
import com.teleconsulting.demo.model.AuthenticationResponse;

public interface AdminService {
    AuthenticationResponse saveNewAdmin(Admin admin);
}
