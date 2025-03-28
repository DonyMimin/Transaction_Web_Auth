package com.example.webcrud.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.webcrud.dto.Company_dto;
import com.example.webcrud.services.CompanyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/companies")
public class Company_controller {

    // Initialize variable
    private CompanyService companyService;

    // Add constructor
    @Autowired
    public Company_controller(CompanyService companyService) {
        this.companyService = companyService;
    }

    // GET method to get all companies
    // endpoint: /api/companies
    @GetMapping
    public ResponseEntity<List<Company_dto>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    // GET method by id
    // endpoint: /api/companies/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Company_dto> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    // POST method to create company
    // endpoint: /api/companies
    @PostMapping
    public ResponseEntity<Company_dto> createCompany(@Valid @RequestBody Company_dto companyDTO) {
        return new ResponseEntity<>(companyService.createCompany(companyDTO), HttpStatus.CREATED);
    }

    // PUT method to update company by id
    // endpoint: /api/companies/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Company_dto> updateCompany(@PathVariable Long id, @Valid @RequestBody Company_dto companyDTO) {
        return ResponseEntity.ok(companyService.updateCompany(id, companyDTO));
    }

    // DELETE method to delete company by id
    // endpoint: /api/companies/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    // GET method by companeName
    // endpoint: /companies/search?companyName=companyName
    @GetMapping("/search")
    public ResponseEntity<List<Company_dto>> searchCompanies(@RequestParam String companyName) {
        return ResponseEntity.ok(companyService.searchCompaniesByCompanyName(companyName));
    }
}