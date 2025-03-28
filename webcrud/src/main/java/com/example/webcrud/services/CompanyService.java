package com.example.webcrud.services;

import java.util.List;

import com.example.webcrud.dto.Company_dto;

public interface CompanyService {
    List<Company_dto> getAllCompanies();
    Company_dto getCompanyById(Long companyID);
    Company_dto createCompany(Company_dto company_dto);
    Company_dto updateCompany(Long companyID, Company_dto company_dto);
    void deleteCompany(Long companyID);
    List<Company_dto> searchCompaniesByCompanyName(String companyName);
}
