package com.example.webcrud.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productID;
    
    @Column(name = "nama_product", nullable = false)
    private String productName;
    
    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false) 
    private Integer stock;
}

