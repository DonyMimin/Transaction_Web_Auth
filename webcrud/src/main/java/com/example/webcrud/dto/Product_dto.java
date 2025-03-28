package com.example.webcrud.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product_dto {
    private Long productID;
    
    @NotBlank(message = "Product name is required")
    private String productName;
    
    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Integer price;
    
    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    private Integer stock;
}