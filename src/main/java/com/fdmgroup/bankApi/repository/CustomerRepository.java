package com.fdmgroup.bankApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdmgroup.bankApi.model.Customer;

// Repository interface for Customer entity
// This interface extends JpaRepository, which provides built-in CRUD operations and database interaction methods.
// <Customer, Long> means:
// - Customer → the entity type this repository manages
// - Long → the type of the entity's primary key (customerId)
public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
}
