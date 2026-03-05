package com.fdmgroup.bankApi.model;

import jakarta.persistence.*;

// Entity representing a Savings Account.
// Uses JOINED inheritance
@Entity
@Table(name = "SAVINGS_ACCOUNT")
public class SavingsAccount extends Account{
	
	// Interest rate specific to savings accounts
	@Column(name = "INTEREST_RATE", nullable = false)
	private double interestRate;
	
	// Default constructor required by JPA
    public SavingsAccount() {}
    
    // Constructor used to create a savings account with balance and interest rate
    public SavingsAccount(double balance, Customer customer, double interestRate) {
        super(balance, customer);
        this.interestRate = interestRate;
    }

    // Get the interest rate
	public double getInterestRate() {
		return interestRate;
	}

	// Update the interest rate
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}
}
