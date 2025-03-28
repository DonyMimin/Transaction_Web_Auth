package com.example.webcrud.repository;

import com.example.webcrud.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface User_repository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}