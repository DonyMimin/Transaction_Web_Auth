package com.example.webcrud.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.webcrud.dto.Product_dto;
import com.example.webcrud.services.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class Product_controller {

    // Initialize variable
    private ProductService productService;

    // Add constructor
    @Autowired
    public Product_controller(ProductService productService) {
        this.productService = productService;
    }

    // GET method to get all products
    // endpoint: /api/products
    @GetMapping
    public ResponseEntity<List<Product_dto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // GET method to find product by id
    // endpoint: /api/products/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Product_dto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // POST method to create product
    // endpoint: /api/products
    @PostMapping
    public ResponseEntity<Product_dto> createProduct(@Valid @RequestBody Product_dto productDTO) {
        return new ResponseEntity<>(productService.createProduct(productDTO), HttpStatus.CREATED);
    }

    // PUT method to update Product by id
    // endpoint: /api/products/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Product_dto> updateProduct(@PathVariable Long id, @Valid @RequestBody Product_dto productDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, productDTO));
    }

    // DELETE method to delete product by id
    // endpoint: /api/products/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // GET method to search producy by producName
    // endpoint: /products/search?productName=productName
    @GetMapping("/search")
    public ResponseEntity<List<Product_dto>> searchProducts(@RequestParam String productName) {
        return ResponseEntity.ok(productService.searchProductsByProductName(productName));
    }
}