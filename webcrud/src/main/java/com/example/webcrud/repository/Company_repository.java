package com.example.webcrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.webcrud.Entity.Company;

import java.util.List;

@Repository
public interface Company_repository extends JpaRepository<Company, Long> {
    List<Company> findByCompanyNameContainingIgnoreCase(String companyName);
}
