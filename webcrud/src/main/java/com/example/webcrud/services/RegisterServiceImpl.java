package com.example.webcrud.services;

import java.util.Collections;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.webcrud.Entity.User;
import com.example.webcrud.dto.AuthRequest;
import com.example.webcrud.repository.User_repository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RegisterServiceImpl implements RegisterService {
    // Initialize variables
    private final User_repository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Add constructor
    public RegisterServiceImpl(User_repository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Create user
    @Override
    @Transactional
    public AuthRequest createAccount(AuthRequest authRequest) {
        // Check if username already exists
        if (userRepository.findByUsername(authRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = convertToEntity(authRequest);
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Save role default value
        user.setRoles(Collections.singleton("ROLE_USER"));
        
        // You might want to set default role here
        // user.setRoles(Collections.singletonList("USER"));

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    // Update user
    @Override
    @Transactional
    public AuthRequest updateAccount(Long id, AuthRequest authRequest) {
        User existingUser = userRepository.findById(id)
              .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        // Update fields
        existingUser.setUsername(authRequest.getUsername());
        // Only update password if a new one is provided
        if (authRequest.getPassword() != null && !authRequest.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        }

        User updatedUser = userRepository.save(existingUser);
        return convertToDTO(updatedUser);
    }

    // Delete user
    @Override
    @Transactional
    public void deleteAccount(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    // Helper methods for DTO conversion
    private AuthRequest convertToDTO(User user) {
        AuthRequest authRequest = new AuthRequest();
        BeanUtils.copyProperties(user, authRequest);
        // Clear password for security
        authRequest.setPassword(null);
        return authRequest;
    }

    private User convertToEntity(AuthRequest authRequest) {
        User user = new User();
        BeanUtils.copyProperties(authRequest, user);
        return user;
    }
}