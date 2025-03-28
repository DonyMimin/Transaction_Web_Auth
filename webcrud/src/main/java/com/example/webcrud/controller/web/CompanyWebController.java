package com.example.webcrud.controller.web;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.webcrud.dto.Company_dto;
import com.example.webcrud.services.CompanyService;

@Controller
@RequestMapping("/companies")
public class CompanyWebController {

    // Initialize variable
    private final CompanyService companyService;

    // Add constructor
    @Autowired
    public CompanyWebController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // Get company
    @GetMapping
    public String getAllCompanies(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());
        return "company/list";
    }

    // Show empty form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("company", new Company_dto());
        return "company/form";
    }

    // Membuat company
    @PostMapping
    public String createCompany(@Valid @ModelAttribute("company") Company_dto companyDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "company/form";
        }
        
        companyService.createCompany(companyDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Company created successfully!");
        return "redirect:/companies";
    }

    // Show form data by companyId
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("company", companyService.getCompanyById(id));
        return "company/form";
    }

    // Update company
    @PostMapping("/update/{id}")
    public String updateCompany(@PathVariable Long id,
                               @Valid @ModelAttribute("companies") Company_dto companyDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "company/form";
        }
        
        companyService.updateCompany(id, companyDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Company updated successfully!");
        return "redirect:/companies";
    }

    // Delete company
    @GetMapping("/delete/{id}")
    public String deleteCompany(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        companyService.deleteCompany(id);
        redirectAttributes.addFlashAttribute("successMessage", "Company deleted successfully!");
        return "redirect:/companies";
    }

    // Search bar by companyName
    @GetMapping("/search")
    public String searchCompanies(@RequestParam String companyName, Model model) {
        model.addAttribute("companies", companyService.searchCompaniesByCompanyName(companyName));
        model.addAttribute("searchTerm", companyName);
        return "company/list";
    }
}

