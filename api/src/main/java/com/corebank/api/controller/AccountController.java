package com.corebank.api.controller;

import com.corebank.api.model.Account;
import com.corebank.api.model.User;
import com.corebank.api.service.AccountService;
import com.corebank.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    /**
     * Get current user's accounts
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my-accounts")
    public List<Account> getMyAccounts() {
        String username = getCurrentUsername();
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return accountService.getAccountsByUser(user);
    }

    /**
     * Get all accounts (Admin only)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    /**
     * Get account by ID (only if user owns the account or is admin)
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        String username = getCurrentUsername();
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Optional<Account> accountOpt = accountService.getAccountById(id);
        if (accountOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Account account = accountOpt.get();
        // Check if user owns the account or is admin
        if (account.getUser().getId().equals(user.getId()) || isAdmin()) {
            return ResponseEntity.ok(account);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    /**
     * Create account for current user
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        String username = getCurrentUsername();
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        account.setUser(user);
        return accountService.createAccount(account);
    }

    /**
     * Update account (only if user owns the account or is admin)
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody Account account) {
        String username = getCurrentUsername();
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Optional<Account> existingAccountOpt = accountService.getAccountById(id);
        if (existingAccountOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Account existingAccount = existingAccountOpt.get();
        // Check if user owns the account or is admin
        if (existingAccount.getUser().getId().equals(user.getId()) || isAdmin()) {
            Account updatedAccount = accountService.updateAccount(id, account);
            return ResponseEntity.ok(updatedAccount);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    /**
     * Delete account (only if user owns the account or is admin)
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        String username = getCurrentUsername();
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return accountService.getAccountById(id)
                .map(account -> {
                    // Check if user owns the account or is admin
                    if (account.getUser().getId().equals(user.getId()) || isAdmin()) {
                        accountService.deleteAccount(id);
                        return ResponseEntity.ok("Account with ID " + id + " deleted successfully.");
                    } else {
                        return ResponseEntity.status(403).body("Access denied");
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Helper method to get current authenticated username
     */
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("User not authenticated");
        }
        return authentication.getName();
    }

    /**
     * Helper method to check if current user is admin
     */
    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }
}
