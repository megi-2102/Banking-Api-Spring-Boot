package relationships;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "MODULE")
public class Module {
	
	@Id
	@Column(name = "MODULE_ID", nullable = false)
	@SequenceGenerator(name = "STR_MOD_GNTR", sequenceName = "MOD_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STR_MOD_GNTR")	
	private int module;
	
	@Column(name = "MODULE_NAME", nullable = false, unique = true)
	private String name;
	
	@ManyToMany(mappedBy = "modules")
	private List<Stream> streams;

	public Module() {}
	
	public Module(String name) {
		this.name = name;
	}

	public int getModule() {
		return module;
	}

	public String getName() {
		return name;
	}

	public List<Stream> getStreams() {
		return streams;
	}

}
