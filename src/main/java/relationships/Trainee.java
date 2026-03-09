package relationships;

import jakarta.persistence.*;

@Entity
@Table(name = "TRAINEE")
@NamedQuery(
		name = "Trainee.findByStream",
		query = "SELECT t from Trainee t WHERE t.stream.name = :streamName")
public class Trainee {
	@Id
	@Column(name = "TRAINEE_ID", nullable = false)
	@SequenceGenerator(name = "STR_TRN_GNTR", sequenceName = "TRN_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STR_TRN_GNTR")	
	private int id;
	
	@Column(name = "FIRST_NAME", nullable = false, updatable = false)
	private String firstName;
	
	@Column(name = "LAST_NAME", nullable = false, updatable = false)
	private String lastName;
	
	@ManyToOne
	@JoinColumn(name = "FK_STREAM_ID")
	private Stream stream;

	public Trainee() {}
	
	public Trainee(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public int getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Stream getStream() {
		return stream;
	}

	public void setStream(Stream stream) {
		this.stream = stream;
	}	
}
