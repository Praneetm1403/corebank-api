package com.corebank.api.service;

import com.corebank.api.model.*;
import com.corebank.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public Transaction createTransaction(Long accountId, Transaction transaction) {
        // Fetch the account fresh from DB
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Ensure balance is never null
        BigDecimal currentBalance = account.getBalance() != null ? account.getBalance() : BigDecimal.ZERO;
        BigDecimal newBalance;

        // Compute new balance based on transaction type
        if (transaction.getType() == TransactionType.DEPOSIT) {
            newBalance = currentBalance.add(transaction.getAmount());
        } else if (transaction.getType() == TransactionType.WITHDRAW) {
            if (currentBalance.compareTo(transaction.getAmount()) < 0) {
                throw new RuntimeException("Insufficient balance");
            }
            newBalance = currentBalance.subtract(transaction.getAmount());
        } else {
            throw new RuntimeException("Invalid transaction type");
        }

        // Update account balance
        account.setBalance(newBalance);
        accountRepository.save(account);

        // Create and save transaction
        Transaction savedTransaction = new Transaction(
                account,
                transaction.getAmount(),
                transaction.getType(),
                transaction.getDescription(),
                newBalance
        );

        return transactionRepository.save(savedTransaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public List<Transaction> getTransactionsByAccount(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
