package inheritance;
import jakarta.persistence.*;

@Entity
@Table(name = "BANK_ACCOUNT")
@NamedQuery(name = "BankAccount.findAll",
			query = "SELECT b FROM BankAccount b"
			)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
	    name = "ACCOUNT_TYPE",
	    discriminatorType = DiscriminatorType.STRING,
	    columnDefinition = "VARCHAR(20) NOT NULL"
	)
public abstract class BankAccount {
	
	@Id
	@SequenceGenerator(name = "bankAccSeqGen", sequenceName = "bankAccSeq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bankAccSeqGen")
	@Column(name = "ACCOUNT_ID", nullable = false)
	private int id;
	
	@Column(name = "BALANCE", nullable = false)
	protected double balance;
	
	@ManyToOne
	@JoinColumn(name = "FK_CUSTOMER_ID")
	private BankCustomer customer;
	
	public BankAccount() {}
	
	public BankAccount(double balance)
	{
		this.balance = balance;
	}
	
	public abstract void withdraw(double amount);
	
	public void deposit(double amount)
	{
		balance = balance + amount;
		System.out.println("The balance is now: "+ getBalance());
	}

	public int getId() {
		return id;
	}

	public double getBalance() {
		return balance;
	}

	public BankCustomer getCustomer() {
		return customer;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void setCustomer(BankCustomer customer) {
		this.customer = customer;
	}
}
