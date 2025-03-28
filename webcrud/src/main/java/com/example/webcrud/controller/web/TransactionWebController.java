package com.example.webcrud.controller.web;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.webcrud.dto.Transaction_dto;
import com.example.webcrud.security.JwtTokenProvider;
import com.example.webcrud.services.TransactionService;
import com.example.webcrud.services.ProductService;
import com.example.webcrud.services.CompanyService;

@Controller
@RequestMapping("/transactions")
public class TransactionWebController {

    // Initialize variable 
    private final TransactionService transactionService;
    private final ProductService productService;
    private final CompanyService companyService;

    // Add Constructor
    @Autowired 
    public TransactionWebController(
        TransactionService transactionService, 
        ProductService productService, 
        CompanyService companyService
    ) {
        this.transactionService = transactionService;
        this.productService = productService;
        this.companyService = companyService;
    }

    // Get all transactions
    @GetMapping
    public String getAllTransactions(Model model) {
        model.addAttribute("transactions", transactionService.getAllTransactions());
        return "transaction/list";
    }

    // Show create form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("transaction", new Transaction_dto());
        model.addAttribute("products", productService.getAllProducts()); // Ensure products are passed
        model.addAttribute("companies", companyService.getAllCompanies()); // Ensure companies are passed
        return "transaction/form";
    }

    // Create new transaction
    @PostMapping
    public String createTransaction(@Valid @ModelAttribute("transaction") Transaction_dto transactionDTO,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        if (result.hasErrors()) {
            model.addAttribute("products", productService.getAllProducts());
            model.addAttribute("companies", companyService.getAllCompanies());
            return "transaction/form";
        }

        try {
            transactionService.createTransaction(transactionDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Transaction created successfully!");
            return "redirect:/transactions";
        } catch (IllegalArgumentException e) {
            model.addAttribute("products", productService.getAllProducts());
            model.addAttribute("companies", companyService.getAllCompanies());
            model.addAttribute("errorMessage", e.getMessage());
            return "transaction/form";
        }
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Transaction_dto transaction = transactionService.getTransactionById(id);
        model.addAttribute("transaction", transaction);
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("companies", companyService.getAllCompanies());
        return "transaction/form";
    }

    // Update transaction
    @PostMapping("/update/{id}")
    public String updateTransaction(@PathVariable Long id,
                                    @Valid @ModelAttribute("transaction") Transaction_dto transactionDTO,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        // Ensure products and companies are always added to the model
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("companies", companyService.getAllCompanies());

        // Check for validation errors
        if (result.hasErrors()) {
            return "transaction/form";
        }

        transactionService.updateTransaction(id, transactionDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Transaction updated successfully!");
        return "redirect:/transactions";
    }

    // Delete transaction
    @GetMapping("/delete/{id}")
    public String deleteTransaction(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        transactionService.deleteTransaction(id);
        redirectAttributes.addFlashAttribute("successMessage", "Transaction deleted successfully!");
        return "redirect:/transactions";
    }

    // Search transactions
    @GetMapping("/search")
    public String searchTransactions(@RequestParam String productName, Model model) {
        model.addAttribute("transactions", transactionService.searchTransactionsByProductName(productName));
        model.addAttribute("searchTerm", productName);
        return "transaction/list";
    }
}
