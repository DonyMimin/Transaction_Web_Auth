package com.example.webcrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.webcrud.Entity.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface Product_repository extends JpaRepository<Product, Long> {
    List<Product> findByProductNameContainingIgnoreCase(String productName);
    Optional<Product> findByProductName(String productName);
}
