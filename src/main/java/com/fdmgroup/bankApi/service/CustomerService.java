package com.fdmgroup.bankApi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fdmgroup.bankApi.model.Address;
import com.fdmgroup.bankApi.model.Customer;
import com.fdmgroup.bankApi.repository.CustomerRepository;

/* Service layer for Customer-related business logic.
 * This class acts as an intermediary between the controller
 * and the repository. It contains all business rules and
 * ensures only valid operations are performed on Customer data.
 */
@Service
public class CustomerService {

	//Repository used to interact with the database
    private final CustomerRepository repository;

   // Constructor-based dependency injection
    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    // Creates and saves a new customer
    public Customer create(Customer customer) {
        if (customer != null && customer.getName() != null && customer.getAddress() != null) {
            return repository.save(customer); // persist customer in DB
        }
        return null; // return null if invalid
    }

    // Finds a customer by its unique ID
    public Customer findById(long id) {
        Optional<Customer> optCustomer = repository.findById(id);
        return optCustomer.orElse(null); // return null if not found
    }

    // Retrieves all customers from the database
    public List<Customer> findAll() {
        return repository.findAll();
    }

    // Updates only allowed fields of an existing customer (name, postal code, city and province)
    public Customer update(long id, Customer updated) {
        Optional<Customer> existingOptional = repository.findById(id);

        if (existingOptional.isEmpty()) {
            return null;
        }

        Customer existing = existingOptional.get();

        // Update fields
        existing.setName(updated.getName());
        
        Address existingAddress = existing.getAddress();

        existingAddress.setPostalCode(updated.getAddress().getPostalCode());
        existingAddress.setCity(updated.getAddress().getCity());
        existingAddress.setProvince(updated.getAddress().getProvince());

        return repository.save(existing);
    }

    // Delete a customer by ID
    public boolean delete(long id) {
        if(repository.existsById(id)){
    		repository.deleteById(id);
    		return true; // deletion successful
    	}
        return false; // customer not found
    }
}