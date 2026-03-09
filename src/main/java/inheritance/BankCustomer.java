package inheritance;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "BANK_CUSTOMER")
@SequenceGenerator(name = "bankCustSeqGen", sequenceName = "bankCustSeq")
public abstract class BankCustomer {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bankCustSeqGen")
	@Column(name = "CUSTOMER_ID")
	private int id;
	
	@Column(name = "ADDRESS", nullable = false)
	private String address;
	
	@OneToMany(mappedBy = "customer")
	private List<BankAccount> accounts;
	
	public BankCustomer() {}

	public BankCustomer(String address) {
		super();
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public List<BankAccount> getAccounts() {
		return accounts;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setAccounts(List<BankAccount> accounts) {
		this.accounts = accounts;
	}

	@Override
	public String toString() {
		return "BankCustomer [id=" + id + ", address=" + address + "]";
	}
}
