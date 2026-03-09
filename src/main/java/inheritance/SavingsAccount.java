package inheritance;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("SAVINGS")
@NamedQuery(name = "SavingsAccount.findAll",
			query = "SELECT s FROM SavingsAccount s")
public class SavingsAccount extends BankAccount{

	public SavingsAccount() {}

	public SavingsAccount(double balance) {
		super(balance);
	}
	
	@Override
	public void withdraw(double amount)
	{
		balance -= amount;
	}
}
