package inheritance;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("CHECKING")
public class CheckingAccount extends BankAccount{

	public CheckingAccount() {}

	public CheckingAccount(double balance) {
		super(balance);
	}
	
	@Override
	public void withdraw(double amount)
	{
		balance -= amount;
	}

}
