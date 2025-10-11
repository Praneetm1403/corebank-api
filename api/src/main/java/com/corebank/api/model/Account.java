package com.corebank.api.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "accounts")

public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK to users.id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"password", "email", "role"})
    private User user;


    // store monetary values in BigDecimal
    @NotNull
    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    public Account() {}

    public Account(User user, BigDecimal balance) {
        this.user = user;
        this.balance = balance;
    }

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}
