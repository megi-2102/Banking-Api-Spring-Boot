package com.fdmgroup.bankApi.model;

import jakarta.persistence.*;

//Represents an individual customer.
//Mapped using JPA inheritance with discriminator value "COMPANY".
@Entity
@DiscriminatorValue("COMPANY")
public class Company extends Customer{
	
	// Default constructor required by JPA
	public Company() {}
	
	// Constructor used to create a Company with name and address
    public Company(String name, Address address) {
        super(name, address);
    }
}
