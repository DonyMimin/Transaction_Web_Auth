package com.example.webcrud.controller;

import org.springframework.web.bind.annotation.*;

import com.example.webcrud.dto.Transaction_dto;
import com.example.webcrud.services.TransactionService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
public class Transaction_controller {

    // Initialize variable
    private TransactionService transactionService;

    // Add contructor
    @Autowired
    public Transaction_controller(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // GET method to get all transactions
    // endpoint: /api/transactions
    @GetMapping
    public ResponseEntity<List<Transaction_dto>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    // GET method to get transaction by id
    // endpoint: /api/transactions/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Transaction_dto> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    // POST method to create transaction
    // endpoint: /api/transactions
    @PostMapping
    public ResponseEntity<Transaction_dto> createTransaction(@Valid @RequestBody Transaction_dto transactionDTO) {
        return new ResponseEntity<>(transactionService.createTransaction(transactionDTO), HttpStatus.CREATED);
    }

    // PUT method to update transaction by id
    // endpoint: /api/transactions/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Transaction_dto> updateTransaction(@PathVariable Long id, @Valid @RequestBody Transaction_dto transactionDTO) {
        return ResponseEntity.ok(transactionService.updateTransaction(id, transactionDTO));
    }

    // DELETE method to delete transaction by id
    // endpoint: /api/transactions/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    // GET method to get transaction by productName
    // endpoint: /api/transactions/search?productName=productName
    @GetMapping("/search")
    public ResponseEntity<List<Transaction_dto>> searchTransactions(@RequestParam String productName) {
        return ResponseEntity.ok(transactionService.searchTransactionsByProductName(productName));
    }
}