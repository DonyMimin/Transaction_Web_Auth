package com.example.webcrud.services;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.webcrud.dto.Product_dto;
import com.example.webcrud.repository.Product_repository;

import jakarta.persistence.EntityNotFoundException;

import com.example.webcrud.Entity.Product;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    // Initialize varibale
    private final Product_repository productRepository;

    // Add constructor
    public ProductServiceImpl(Product_repository productRepository) {
        this.productRepository = productRepository;
    }

    // Get all products
    @Override
    public List<Product_dto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get product by id
    @Override
    public Product_dto getProductById(Long productID) {
        Product product = productRepository.findById(productID)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productID));
        return convertToDTO(product);
    }

    // Create product
    @Override
    @Transactional
    public Product_dto createProduct(Product_dto productDTO) {
        Product product = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    // Update product
    @Override
    @Transactional
    public Product_dto updateProduct(Long companyID, Product_dto productDTO) {
        Product existingProduct = productRepository.findById(companyID)
              .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + companyID));

        // Update fields
        existingProduct.setProductName(productDTO.getProductName());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setStock(productDTO.getStock());

        Product updatedProduct = productRepository.save(existingProduct);
        return convertToDTO(updatedProduct);
    }

    // Delete product
    @Override
    @Transactional
    public void deleteProduct(Long productID) {
        if (!productRepository.existsById(productID)) {
            throw new EntityNotFoundException("Product not found with id: " + productID);
        }
        productRepository.deleteById(productID);
    }

    // Search product by productName
    @Override
    public List<Product_dto> searchProductsByProductName(String productName) {
        return productRepository.findByProductNameContainingIgnoreCase(productName)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    // Helper methods for DTO conversion
    // Convert to DTO to make way safer and give a nessesary resp
    private Product_dto convertToDTO(Product product) {
        Product_dto productDTO = new Product_dto();
        BeanUtils.copyProperties(product, productDTO);
        return productDTO;
    }

    private Product convertToEntity(Product_dto productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        return product;
    }
}