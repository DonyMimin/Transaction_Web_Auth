package com.example.webcrud.services;

import com.example.webcrud.dto.AuthRequest;

public interface RegisterService {
    AuthRequest createAccount(AuthRequest authRequest);
    AuthRequest updateAccount(Long id, AuthRequest authRequest);
    void deleteAccount(Long id);
}