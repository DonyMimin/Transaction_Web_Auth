package com.example.webcrud.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.webcrud.dto.Transaction_dto;
import com.example.webcrud.repository.Product_repository;
import com.example.webcrud.repository.Transaction_repository;
import com.example.webcrud.Entity.Product;
import com.example.webcrud.Entity.Transaction;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final Transaction_repository transactionRepository;
    private final Product_repository productRepository;

    public TransactionServiceImpl(
        Transaction_repository transactionRepository, 
        Product_repository productRepository
    )   {
        this.transactionRepository = transactionRepository;
        this.productRepository = productRepository;
    }

    // Get all transactions
    @Override
    public List<Transaction_dto> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get transaction by id
    @Override
    public Transaction_dto getTransactionById(Long transactionID) {
        Transaction transaction = transactionRepository.findById(transactionID)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + transactionID));
        return convertToDTO(transaction);
    }

    // Create transaction
    @Override
    @Transactional
    public Transaction_dto createTransaction(Transaction_dto transactionDTO) {

        // Initialize
        String productName = transactionDTO.getProductName().trim();
        String companyName = transactionDTO.getCompanyName();

        // Validate input
        if (productName == null || companyName == null) {
            throw new IllegalArgumentException("Product name and Company name are required");
        }

        // Get product data
        Product product = productRepository.findByProductName(productName)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + productName));

        // Check stock availability
        if (product.getStock() < transactionDTO.getQuantity()) {
            throw new IllegalArgumentException("Not enough stock available for " + product.getProductName());
        }

        // Calculate Total Price
        int totalPrice = product.getPrice() * transactionDTO.getQuantity();

        // Deduct Stock
        product.setStock(product.getStock() - transactionDTO.getQuantity());
        productRepository.save(product); // Save the updated stock

        // Create new transaction
        Transaction transaction = new Transaction();
        transaction.setProductName(product.getProductName());
        transaction.setCompanyName(transactionDTO.getCompanyName());
        transaction.setQuantity(transactionDTO.getQuantity());
        transaction.setTotalPrice(totalPrice);

        // Save transaction to DB
        transaction = transactionRepository.save(transaction);
        
        return convertToDTO(transaction);
    }

    // Update transaction
    @Override
    @Transactional
    public Transaction_dto updateTransaction(Long transactionID, Transaction_dto transactionDTO) {

        // Get transaction data
        Transaction existingTransaction = transactionRepository.findById(transactionID)
            .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + transactionID));
    
        // Preserve the original createdAt timestamp
        transactionDTO.setCreatedAt(existingTransaction.getCreatedAt());

        // Initialize product
        String productName = transactionDTO.getProductName().trim();
        // Get product data
        Product product = productRepository.findByProductName(productName)
            .orElseThrow(() -> new EntityNotFoundException("Product not found: " + productName));

        // Check stock availability
        if (product.getStock() < transactionDTO.getQuantity()) {
            throw new IllegalArgumentException("Not enough stock available for " + product.getProductName());
        }

        // Get the last quantity from the existing transaction
        Integer lastQty = existingTransaction.getQuantity();
        // Check stock availability for new quantity
        if (transactionDTO.getQuantity() > lastQty) {
            // If new quantity is greater, check if enough stock is available
            int additionalQuantity = transactionDTO.getQuantity() - lastQty;
            if (product.getStock() < additionalQuantity) {
                throw new IllegalArgumentException("Not enough stock available for " + product.getProductName());
            }
            // Reduce stock for additional quantity
            product.setStock(product.getStock() - additionalQuantity);
        } else if (transactionDTO.getQuantity() < lastQty) {
            // If new quantity is less, add back the difference to stock
            int returnedQuantity = lastQty - transactionDTO.getQuantity();
            product.setStock(product.getStock() + returnedQuantity);
        }
    
        // Map DTO to Entity, preserving the original ID
        Transaction updatedTransaction = convertToEntity(transactionDTO);
        updatedTransaction.setTransactionID(transactionID);
        // Save the updated transaction
        Transaction savedTransaction = transactionRepository.save(updatedTransaction);

        // Save product
        productRepository.save(product);
        
        return convertToDTO(savedTransaction);
    }

    // Delete transaction
    @Override
    @Transactional
    public void deleteTransaction(Long transactionID) {
        if (!transactionRepository.existsById(transactionID)) {
            throw new EntityNotFoundException("Transaction not found with id: " + transactionID);
        }

        Transaction transaction = transactionRepository.findById(transactionID)
            .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + transactionID));
        Product product = productRepository.findByProductName(transaction.getProductName())
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + transaction.getProductName()));

        // Restore Stock
        product.setStock(product.getStock() + transaction.getQuantity());
        productRepository.save(product); // Save the updated stock

        transactionRepository.deleteById(transactionID);
    }

    // Serach transaction by productName
    @Override
    public List<Transaction_dto> searchTransactionsByProductName(String productName) {
        return transactionRepository.findByProductNameContainingIgnoreCase(productName)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Helper methods for DTO conversion
    // Convert to DTO to make way safer and give a nessesary resp
    private Transaction_dto convertToDTO(Transaction transaction) {
        Transaction_dto dto = new Transaction_dto();
        dto.setTransactionID(transaction.getTransactionID());
        dto.setProductName(transaction.getProductName());
        dto.setCompanyName(transaction.getCompanyName());
        dto.setQuantity(transaction.getQuantity());
        dto.setTotalPrice(transaction.getTotalPrice());
        dto.setCreatedAt(transaction.getCreatedAt());
        return dto;
    }

    private Transaction convertToEntity(Transaction_dto dto) {
        Transaction transaction = new Transaction();
        transaction.setProductName(dto.getProductName());
        transaction.setCompanyName(dto.getCompanyName());
        transaction.setQuantity(dto.getQuantity());
        transaction.setTotalPrice(dto.getTotalPrice());
        transaction.setCreatedAt(dto.getCreatedAt());
        return transaction;
    }
}
