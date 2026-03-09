package inheritance;

import jakarta.persistence.*;

@Entity
@Table(name = "COMPANY")
@PrimaryKeyJoinColumn(name = "CUSTOMER_ID")
@NamedQuery(name = "Company.findByName",
			query = "SELECT c FROM Company c WHERE c.name = :companyName"
			)
public class Company extends BankCustomer{
	
	@Column(name = "COMPANY_NAME", nullable = false, unique = true)
	private String name;
	@Column(name = "COMPANY_ABN", nullable = false, unique = true)
	private long abn;
	
	public Company() {}
	
	public Company(String address, String name, long abn) {
		super(address);
		this.name = name;
		this.abn = abn;
	}
	
	public String getName() {
		return name;
	}
	public long getAbn() {
		return abn;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setAbn(long abn) {
		this.abn = abn;
	}
}
