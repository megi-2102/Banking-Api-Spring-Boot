package com.fdmgroup.bankApi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fdmgroup.bankApi.model.Account;
import com.fdmgroup.bankApi.model.Address;
import com.fdmgroup.bankApi.model.CheckingAccount;
import com.fdmgroup.bankApi.model.Company;
import com.fdmgroup.bankApi.model.Customer;
import com.fdmgroup.bankApi.model.Person;
import com.fdmgroup.bankApi.model.SavingsAccount;
import com.fdmgroup.bankApi.repository.AccountRepository;
import com.fdmgroup.bankApi.repository.CustomerRepository;

@Configuration
public class LoadDatabase {

    // Preload sample data when the application starts
    @Bean
    CommandLineRunner initDatabase(CustomerRepository customerRepo, AccountRepository accountRepo) {
        return args -> {
        	
            // Sample addresses
        	Address address1 = new Address("13 KENWORTH STREET", "T3R 3E3", "Toronto", "Ontario");
        	Address address2 = new Address("2 QUEEN STREET", "R43 6Y6", "Caledon", "Ontario");
        	
            // Sample customers
            Customer customer1 = new Company("FDM", address1);
            Customer customer2 = new Person("John", address2);

            // Save customers first so they get IDs
            customerRepo.save(customer1);
            customerRepo.save(customer2);

            // Sample accounts
            Account account1 = new SavingsAccount(2300.0, customer1, 4.5);
            Account account2 = new SavingsAccount(45000.0, customer2, 5.0);
            Account account3 = new CheckingAccount(120.0, customer1, 1);
            
            // Save accounts
            accountRepo.save(account1);
            accountRepo.save(account2);
            accountRepo.save(account3);
        };
    }
}