package com.fdmgroup.bankApi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fdmgroup.bankApi.model.Account;

// Repository interface for Account entity
// This interface extends JpaRepository, which provides built-in CRUD operations and database interaction methods.
// <Account, Long> means:
// - Account → the entity type this repository manages
// - Long → the type of the entity's primary key (accountId)
public interface AccountRepository extends JpaRepository<Account, Long>{
	
    // Custom query to find all accounts whose customer's address matches a given city
    // :city is a named parameter that will be bound at runtime
	@Query("SELECT a FROM Account a WHERE a.customer.address.city = :city")
        List<Account> findAccountsByCustomerCity(@Param("city")String city);
}
