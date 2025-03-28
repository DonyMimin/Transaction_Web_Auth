package com.example.webcrud.controller.web;

import com.example.webcrud.dto.AuthRequest;
import com.example.webcrud.services.RegisterService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class RegisterWebController {
    // Initialize variable
    private final RegisterService registerService;

    // Add constructor
    @Autowired
    public RegisterWebController(RegisterService registerService) {
        this.registerService = registerService;
    }

    // Show empty form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("register", new AuthRequest());
        return "register";
    }

    // Make new User
    @PostMapping
    public String createAccount(@Valid @ModelAttribute("register") AuthRequest authRequest,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "register";
        }
        
        try {
            registerService.createAccount(authRequest);
            redirectAttributes.addFlashAttribute("successMessage", "User created successfully!");
            return "redirect:/login";
        } catch (RuntimeException e) {
            result.rejectValue("username", "error.username", "Username already exists");
            return "register";
        }
    }
}