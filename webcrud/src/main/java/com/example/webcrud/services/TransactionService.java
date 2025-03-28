package com.example.webcrud.services;

import java.util.List;

import com.example.webcrud.dto.Transaction_dto;

public interface TransactionService {
    List<Transaction_dto> getAllTransactions();
    Transaction_dto getTransactionById(Long transactionID);
    Transaction_dto createTransaction(Transaction_dto transaction_dto);
    Transaction_dto updateTransaction(Long transactionID, Transaction_dto transaction_dto);
    void deleteTransaction(Long transactionID);
    List<Transaction_dto> searchTransactionsByProductName(String productName);
}