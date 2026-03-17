package com.fdmgroup.bankApi.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fdmgroup.bankApi.exception.AccountNotFoundException;
import com.fdmgroup.bankApi.exception.CustomerNotFoundException;
import com.fdmgroup.bankApi.model.Account;
import com.fdmgroup.bankApi.model.CheckingAccount;
import com.fdmgroup.bankApi.model.Customer;
import com.fdmgroup.bankApi.model.SavingsAccount;
import com.fdmgroup.bankApi.service.AccountService;
import com.fdmgroup.bankApi.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

// REST controller responsible for handling all Account-related API requests.
// Base URL: /api/accounts
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final CustomerService customerService;

    // Constructor-based dependency injection for services
    public AccountController(AccountService accountService, CustomerService customerService) {
        this.accountService = accountService;
        this.customerService = customerService;
    }

    // Create a new account (Savings or Checking) for a specific customer
    @Operation(
        summary = "Create a new account",
        description = "Creates a new account (Savings or Checking) for a specific customer"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Account created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid account data"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PostMapping("/customer/{customerId}")
    public ResponseEntity<Void> create(@PathVariable long customerId, @RequestBody Account account) {
        Customer customer = customerService.findById(customerId);

        if (customer == null) {
            throw new CustomerNotFoundException("id - " + customerId);
        }

        // Link account to customer and maintain bidirectional relationship
        account.setCustomer(customer);
        customer.addAccount(account);

        Account createdAccount = accountService.create(account);
        
        if (createdAccount != null) {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdAccount.getAccountId())
                    .toUri();

            return ResponseEntity
                    .created(location)
                    .build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    // Retrieves an account by ID
    @Operation(
        summary = "Find account by ID",
        description = "Returns an account if found, otherwise throws AccountNotFoundException"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Account found"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Account> findById(@PathVariable long id) {
        Account account = accountService.findById(id);
        if (account != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(account);
        }
        throw new AccountNotFoundException("id - " + id);
    }

    // Retrieves all accounts
    @Operation(
        summary = "Get all accounts",
        description = "Returns a list of all accounts in the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of accounts returned successfully"),
        @ApiResponse(responseCode = "204", description = "No accounts found")
    })
    @GetMapping
    public ResponseEntity<List<Account>> findAll() {
        List<Account> accounts = accountService.findAll();
        if (accounts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(accounts);
    }

    // Updates allowed fields of an existing account (like balance)
    @Operation(
        summary = "Update account balance",
        description = "Updates the balance of an existing account"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Account updated successfully"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "409", description = "Update conflict")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Account> update(@PathVariable long id, @RequestBody Account updated) {

        Account existing = accountService.findById(id);

        if (existing == null) {
            throw new AccountNotFoundException("id - " + id);
        }

        // Update balance for all accounts
        existing.setBalance(updated.getBalance());

        // If the account is a SavingsAccount, update interest rate
        if (existing instanceof SavingsAccount && updated instanceof SavingsAccount) {
            ((SavingsAccount) existing).setInterestRate(
                    ((SavingsAccount) updated).getInterestRate());
        }

        // If the account is a CheckingAccount, update next check number
        if (existing instanceof CheckingAccount && updated instanceof CheckingAccount) {
            ((CheckingAccount) existing).setNextCheckNumber(
                    ((CheckingAccount) updated).getNextCheckNumber());
        }

        Account updatedAccount = accountService.update(id, existing);

        if (updatedAccount != null) {
            return ResponseEntity.ok(updatedAccount);
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    // Deletes an account by ID
    @Operation(
        summary = "Delete account by ID",
        description = "Deletes an existing account"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Account deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        Account existing = accountService.findById(id);
        if (existing == null) {
            throw new AccountNotFoundException("id - " + id);
        }
        accountService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    // Get accounts by customer city
    @Operation(
            summary = "Get accounts by customer city",
            description = "Returns all accounts belonging to customers from a specific city"
        )
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Accounts returned successfully"),
            @ApiResponse(responseCode = "204", description = "No accounts found for the city")
        })
    @GetMapping("/city/{city}")
    public ResponseEntity<List<Account>> getAccountsByCustomerCity(@PathVariable String city) {
        List<Account> accounts = accountService.findAccountsByCustomerCity(city);
        if (accounts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(accounts);
    }
}