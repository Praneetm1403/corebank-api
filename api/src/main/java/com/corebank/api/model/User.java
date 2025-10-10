package com.corebank.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "users")  // avoid reserved keyword conflicts
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Account> accounts;
// Paneer Mahendrakar
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

//    public List<Account> getAccounts() { return accounts; }
//    public void setAccounts(List<Account> accounts) { this.accounts = accounts; }
}
