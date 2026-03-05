package com.fdmgroup.bankApi.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fdmgroup.bankApi.exception.CustomerNotFoundException;
import com.fdmgroup.bankApi.model.Customer;
import com.fdmgroup.bankApi.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

// REST controller responsible for handling all customer-related API requests
// Base URL: /api/customers
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	// Service layer dependency used to perform business logic and database operations
    private final CustomerService service;

    //Constructor-based dependency injection.
    public CustomerController(CustomerService service) {
        this.service = service;
    }

    // Creates a new customer (Person or Company)
    // Returns HTTP 201 (Created) with a Location header if successful
    @Operation(
    	    summary = "Create a new customer",
    	    description = "Creates a new customer(Person or Company) and returns the location of the created resource"
    	)
    	@ApiResponses({
    	    @ApiResponse(
    	        responseCode = "201",
    	        description = "Customer created successfully"
    	    ),
    	    @ApiResponse(
    	        responseCode = "409",
    	        description = "Customer could not be created (conflict)"
    	    )
    	})
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Customer customer) {
    	// Delegate creation logic to the service layer
    	Customer createdCustomer = service.create(customer);
    	
    	// Build URI for the newly created resource
    	if(createdCustomer != null)
    	{
	        URI location = ServletUriComponentsBuilder
	                        .fromCurrentRequest()
	                        .path("/{id}")
	                        .buildAndExpand(createdCustomer.getCustomerId())
	                        .toUri();
	        
	        // Return 201 Created with Location header
	        return ResponseEntity.created(location).build();
    	}
    	// Return 409 Conflict with Location header
    	return ResponseEntity
    			.status(HttpStatus.CONFLICT)
    			.build();
    }

    // Retrieves a customer by their unique ID.
    @Operation(
    	    summary = "Find customer by ID",
    	    description = "Returns a customer if found, otherwise throws CustomerNotFoundException"
    	)
    	@ApiResponses({
    	    @ApiResponse(
    	        responseCode = "200",
    	        description = "Customer found"
    	        ),
    	    @ApiResponse(
    	        responseCode = "404",
    	        description = "Customer not found"
    	    )
    	})
    @GetMapping("/{id}")
    public ResponseEntity<Customer> findById(@PathVariable long id) {
    	// Fetch customer from service layer
    	Customer customer = service.findById(id);   
    	
    	// Fetch customer from service layer
    	if(customer != null) {
    		return ResponseEntity
    				.status(HttpStatus.OK)
    				.body(customer);
    	}
    	
    	// Otherwise throw custom exception
    	throw new CustomerNotFoundException("id - " + id);
    }

    // Retrieves all customers in the system.
    @Operation(
        summary = "Get all customers",
        description = "Returns a list of all customers in the system"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "List of customers returned successfully"
        ),
        @ApiResponse(
            responseCode = "204",
            description = "No customers found"
        )
    })
    @GetMapping
    public ResponseEntity<List<Customer>> findAll() {
    	// Retrieve all customers
    	List<Customer> customers = service.findAll();

    	// Return 204 No Content if the list is empty
        if (customers.isEmpty()) {
            return ResponseEntity.noContent().build(); // optional but clean
        }

        // Otherwise return the list with 200 OK
        return ResponseEntity.ok(customers);
    }

    // Updates allowed fields only of an existing customer (name, city, province, postal code)
    // Street number and ID cannot be modified
    @Operation(
    	    summary = "Update customer details",
    	    description = "Only name, city, province, and postalCode can be updated"
    	)
    	@ApiResponses({
    	    @ApiResponse(
    	        responseCode = "200",
    	        description = "Customer updated successfully"
    	    ),
    	    @ApiResponse(
    	        responseCode = "404",
    	        description = "Customer not found"
    	    ),
    	    @ApiResponse(
    	        responseCode = "409",
    	        description = "Update conflict"
    	    )
    	})
    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable long id, @RequestBody Customer updated) {
    	// Retrieve existing customer
    	Customer existing = service.findById(id);
    	
    	// If customer does not exist, throw exception
        if (existing == null) 
            throw new CustomerNotFoundException("id - " + id);
        if(existing != null) {
        	
        // Update only allowed fields
	        existing.setName(updated.getName());
	        existing.getAddress().setCity(updated.getAddress().getCity());
	        existing.getAddress().setProvince(updated.getAddress().getProvince());
	        existing.getAddress().setPostalCode(updated.getAddress().getPostalCode());
        }
        
        // Persist updated customer
        Customer updatedCustomer = service.update(id, existing);
    	if(updatedCustomer != null) {
    		
    		 // Return updated customer with HTTP 200
    		return ResponseEntity.ok(updatedCustomer);
    	} else {
    		return ResponseEntity.status(HttpStatus.CONFLICT).build();
    	}     
    }

    // Deletes a customer by ID
    @Operation(
    	    summary = "Delete customer by ID",
    	    description = "Deletes an existing customer"
    	)
    	@ApiResponses({
    	    @ApiResponse(
    	        responseCode = "200",
    	        description = "Customer deleted successfully"
    	    ),
    	    @ApiResponse(
    	        responseCode = "404",
    	        description = "Customer not found"
    	    )
    	})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable long id) {
        if (service.delete(id))
        {
    		return ResponseEntity
    				.status(HttpStatus.OK)
    				.build();
        }
        throw new CustomerNotFoundException("id - " + id);
    }
}