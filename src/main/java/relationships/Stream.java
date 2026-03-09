package relationships;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "STREAM")
@NamedQuery(
	    name = "Stream.findByStreamName",
	    query = "SELECT s FROM Stream s WHERE s.name = :streamName")
public class Stream {
	
	@Id
	
	@Column(name = "STREAM_ID", nullable = false)
	@SequenceGenerator(name = "STR_SEQ_GNTR", sequenceName = "STR_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STR_SEQ_GNTR")
	private int id;

	@Column(name = "STREAM_NAME", nullable = false, unique = true)
	private String name;
	
	@ManyToMany
	@JoinTable(name = "STREAM_MODULE",
	joinColumns = 
	@JoinColumn(name = "FK_STREAM_ID", nullable = false),
	inverseJoinColumns = 
	@JoinColumn(name = "FK_MODULE_ID", nullable = false)
	)
	private List<Module> modules;

	public Stream() {}
	
	public Stream(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Module> getModules() {
		return modules;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}
}
