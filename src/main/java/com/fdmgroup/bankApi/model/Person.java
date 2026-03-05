package com.fdmgroup.bankApi.model;

import jakarta.persistence.*;

//Represents an individual customer.
//Mapped using JPA inheritance with discriminator value "PERSON".
@Entity
@DiscriminatorValue("PERSON")
public class Person extends Customer{
	
	// Default constructor required by JPA
	public Person() {}
	
	// Constructor used to create a Person with name and address
    public Person(String name, Address address) {
        super(name, address);
    }
}
