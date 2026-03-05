package com.fdmgroup.bankApi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fdmgroup.bankApi.model.Account;
import com.fdmgroup.bankApi.model.CheckingAccount;
import com.fdmgroup.bankApi.model.Customer;
import com.fdmgroup.bankApi.model.SavingsAccount;
import com.fdmgroup.bankApi.repository.AccountRepository;

/*
 * Service layer for Account-related business logic.
 * Acts as an intermediary between the controller
 * and the repository. Contains business rules and
 * ensures valid operations on Account data.
 */
@Service
public class AccountService {

    // Repository used to interact with the database.
    private final AccountRepository repository;

    // Constructor-based dependency injection.
    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    // Creates and saves a new account (Savings or Checking)
    public Account create(Account account) {
        if (account != null) {
            return repository.save(account); // persist account in DB
        }
        return null;
    }

    // Finds an account by its unique ID
    public Account findById(long id) {
        Optional<Account> optAccount = repository.findById(id);
        return optAccount.orElse(null); // return null if not found
    }

    // Retrieves all accounts from the database
    public List<Account> findAll() {
        return repository.findAll();
    }

    // Persists updates to an account (fields are modified in controller)
    public Account update(long id, Account updated) {
        Optional<Account> existingOptional = repository.findById(id);

        if (existingOptional.isEmpty()) {
            return null;
        }

        Account existing = existingOptional.get();

        // update common fields
        existing.setBalance(updated.getBalance());

        // checking account specific
        if (existing instanceof CheckingAccount && updated instanceof CheckingAccount) {
            ((CheckingAccount) existing)
                    .setNextCheckNumber(((CheckingAccount) updated).getNextCheckNumber());
        }

        // savings account specific
        if (existing instanceof SavingsAccount && updated instanceof SavingsAccount) {
            ((SavingsAccount) existing)
                    .setInterestRate(((SavingsAccount) updated).getInterestRate());
        }
        return repository.save(existing);
    }

    // Deletes an account by ID
    public boolean delete(long id) {
        Optional<Account> existingOptional = repository.findById(id);

        if (existingOptional.isEmpty()) {
            return false;
        }

        Account account = existingOptional.get();
        Customer customer = account.getCustomer();

        if (customer != null) {
            customer.getAccounts().remove(account); // break relationship
        }

        repository.delete(account);
        return true;
    }
    
    // Finds all accounts where the customer's city matches the provided city
    public List<Account> findAccountsByCustomerCity(String city) {
        return repository.findAccountsByCustomerCity(city);
    }
}