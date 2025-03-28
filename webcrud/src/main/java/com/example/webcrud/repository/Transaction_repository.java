package com.example.webcrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.webcrud.Entity.Transaction;

import java.util.List;

@Repository
public interface Transaction_repository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByProductNameContainingIgnoreCase(String productName);
}