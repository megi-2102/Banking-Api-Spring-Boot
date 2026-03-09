package inheritance;

import jakarta.persistence.*;

@Entity
@PrimaryKeyJoinColumn(name = "CUSTOMER_ID")
@Table(name = "PERSON")
public class Person extends BankCustomer{

	@Column(name = "FIRST_NAME", nullable = false)
	private String firstName;
	@Column(name = "LAST_NAME", nullable = false)
	private String lastName;
	
	public Person() {}
	
	public Person(String address, String firstName, String lastName) {
		super(address);
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
