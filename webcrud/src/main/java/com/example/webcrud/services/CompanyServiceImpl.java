package com.example.webcrud.services;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.webcrud.dto.Company_dto;
import com.example.webcrud.repository.Company_repository;

import jakarta.persistence.EntityNotFoundException;

import com.example.webcrud.Entity.Company;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {
    
    // Initialize variable
    private final Company_repository companyRepository;

    // Add contructor
    public CompanyServiceImpl(Company_repository companyRepository) {
        this.companyRepository = companyRepository;
    }

    // Get all companies
    @Override
    public List<Company_dto> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get company
    @Override
    public Company_dto getCompanyById(Long companyID) {
        Company company = companyRepository.findById(companyID)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + companyID));
        return convertToDTO(company);
    }

    // Create company
    @Override
    @Transactional
    public Company_dto createCompany(Company_dto companyDTO) {
        Company company = convertToEntity(companyDTO);
        Company savedCompany = companyRepository.save(company);
        return convertToDTO(savedCompany);
    }

    // Update company
    @Override
    @Transactional
    public Company_dto updateCompany(Long companyID, Company_dto companyDTO) {
        Company existingCompany = companyRepository.findById(companyID)
              .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + companyID));

        // Update fields
        existingCompany.setCompanyName(companyDTO.getCompanyName());

        Company updatedCompany = companyRepository.save(existingCompany);
        return convertToDTO(updatedCompany);
    }

    // Delete company
    @Override
    @Transactional
    public void deleteCompany(Long companyID) {
        if (!companyRepository.existsById(companyID)) {
            throw new EntityNotFoundException("Product not found with id: " + companyID);
        }
        companyRepository.deleteById(companyID);
    }

    // Search company
    @Override
    public List<Company_dto> searchCompaniesByCompanyName(String companyName) {
        return companyRepository.findByCompanyNameContainingIgnoreCase(companyName)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    // Helper methods for DTO conversion
    // Convert to DTO to make way safer and give a nessesary resp
    private Company_dto convertToDTO(Company company) {
        Company_dto companyDTO = new Company_dto();
        BeanUtils.copyProperties(company, companyDTO);
        return companyDTO;
    }

    private Company convertToEntity(Company_dto companyDTO) {
        Company company = new Company();
        BeanUtils.copyProperties(companyDTO, company);
        return company;
    }
}