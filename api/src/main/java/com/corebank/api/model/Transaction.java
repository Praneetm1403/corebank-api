package com.corebank.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to account
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "user", "transactions"})
    private Account account;

    // Amount of money transferred
    @NotNull
    @Column(nullable = false)
    private BigDecimal amount;

    // Type: DEPOSIT or WITHDRAW
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    // Optional description
    private String description;

    // Auto-generated timestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    // Balance after the transaction
    @NotNull
    @Column(nullable = false)
    private BigDecimal balanceAfter;

    // Constructors
    public Transaction() {}

    public Transaction(Account account, BigDecimal amount, TransactionType type, String description, BigDecimal balanceAfter) {
        this.account = account;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public BigDecimal getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(BigDecimal balanceAfter) { this.balanceAfter = balanceAfter; }
}
