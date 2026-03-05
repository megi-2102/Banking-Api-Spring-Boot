package com.fdmgroup.bankApi.model;

import jakarta.persistence.*;

// Entity representing a Checking Account.
// Uses JOINED inheritance
@Entity
@Table(name = "CHECKING_ACCOUNT")
public class CheckingAccount extends Account{
	
	// Next check number to be issued for this account
	@Column(name = "NEXT_CHECK_NUMBER", nullable = false)
	private int nextCheckNumber;
	
	// Default constructor required by JPA
    public CheckingAccount() {}
    
    // Constructor used to create a savings account with balance and interest rate
    public CheckingAccount(double balance, Customer customer, int nextCheckNumber) {
        super(balance, customer);
        this.nextCheckNumber = nextCheckNumber;
    }

    // Get the next check number 
	public int getNextCheckNumber() {
		return nextCheckNumber;
	}

	// Update the next check number
	public void setNextCheckNumber(int nextCheckNumber) {
		this.nextCheckNumber = nextCheckNumber;
	}
}
