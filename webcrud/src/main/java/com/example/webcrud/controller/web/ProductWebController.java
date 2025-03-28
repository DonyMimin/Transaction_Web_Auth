package com.example.webcrud.controller.web;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.webcrud.dto.Product_dto;
import com.example.webcrud.services.ProductService;

@Controller
@RequestMapping("/products")
public class ProductWebController {

    // Initialize variable
    private final ProductService productService;

    // Add constructor
    @Autowired
    public ProductWebController(ProductService productService) {
        this.productService = productService;
    }

    // Get product
    @GetMapping
    public String getAllProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "product/list";
    }

    // Show empty form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product_dto());
        return "product/form";
    }

    // Make new product
    @PostMapping
    public String createProduct(@Valid @ModelAttribute("product") Product_dto productDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "product/form";
        }
        
        productService.createProduct(productDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Product created successfully!");
        return "redirect:/products";
    }

    // Show form data by productId
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "product/form";
    }

    // Update product by id
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id,
                               @Valid @ModelAttribute("products") Product_dto productDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "product/form";
        }
        
        productService.updateProduct(id, productDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully!");
        return "redirect:/products";
    }

    // Delete product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
        return "redirect:/products";
    }

    // Search by productName
    @GetMapping("/search")
    public String searchProducts(@RequestParam String productName, Model model) {
        model.addAttribute("products", productService.searchProductsByProductName(productName));
        model.addAttribute("searchTerm", productName);
        return "product/list";
    }
}

