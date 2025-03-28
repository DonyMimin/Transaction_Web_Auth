package com.example.webcrud.services;

import com.example.webcrud.dto.Product_dto;

import java.util.List;

public interface ProductService {
    List<Product_dto> getAllProducts();
    Product_dto getProductById(Long productID);
    Product_dto createProduct(Product_dto product_dto);
    Product_dto updateProduct(Long productID, Product_dto product_dto);
    void deleteProduct(Long productID);
    List<Product_dto> searchProductsByProductName(String productName);
} 