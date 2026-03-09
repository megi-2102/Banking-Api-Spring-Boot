package introduction;
import jakarta.persistence.*;

@Entity
@Table(name = "FDM_USER")
public class User {
	
	protected User() {}
	@Id
	@Column(name = "USER_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "USERNAME", nullable = false, unique = true)
	private String username;
	
	@Column(name = "PASSWORD", nullable = false)
	private String password;
	
	@Column(name = "FIRST_NAME", nullable = false, updatable = false )
	private String firstName;
	
	@Column(name = "LAST_NAME", nullable = false, updatable = false)
	private String lastName;

	public User(String username, String password, String firstName, String lastName) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	public int getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void updateDetails(User user)
	{
		System.out.println("Details of the user: "+user.getFirstName()
		+ ' '+user.getLastName()+" are:\nUsername: "+user.getUsername()
		+'\n'+"Password: "+user.getPassword());
	}
	
	@Override
	public String toString() {
	    return "User{" +
	            "id=" + id +
	            ", username='" + username + '\'' +
	            ", firstName='" + firstName + '\'' +
	            ", lastName='" + lastName + '\'' +
	            '}';
	}

}
