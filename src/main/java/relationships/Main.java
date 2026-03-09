package relationships;

import java.util.Arrays;
import java.util.List;

import jakarta.persistence.*;

public class Main {

	public static void main(String[] args) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabasic");
		EntityManager em = emf.createEntityManager();
		System.out.println("Connected to h2 database");
		
		Stream stream1 = new Stream("Software Development");
		Stream stream2 = new Stream("BABI");
		
		Trainee trainee1 = new Trainee("John", "Doe");
		Trainee trainee2 = new Trainee("James", "Smith");
		Trainee trainee3 = new Trainee("Jane", "Doe");
		
		Module module1 = new Module("SQL");
		Module module2 = new Module("OOD");

		trainee1.setStream(stream1);
		trainee2.setStream(stream2);
		trainee3.setStream(stream1);
		
		stream1.setModules(Arrays.asList(module1,module2));
		stream2.setModules(Arrays.asList(module1));
		
		em.getTransaction().begin();

		em.persist(module1);
		em.persist(module2);

		em.persist(stream1);
		em.persist(stream2);

		em.persist(trainee1);
		em.persist(trainee2);
		em.persist(trainee3);

		em.getTransaction().commit();
		
		System.out.println("Data persisted successfully.\n");
		
		Stream babiStream = em.createNamedQuery(
				"Stream.findByStreamName", Stream.class)
				.setParameter("streamName", "BABI")
				.getSingleResult();

		System.out.println("Modules in BABI stream:");
		for (Module module : babiStream.getModules()) {
			System.out.println("- " + module.getName());
		}
		
		List<Trainee> softwareTrainees = em.createNamedQuery(
				"Trainee.findByStream", Trainee.class)
				.setParameter("streamName","Software Development")
				.getResultList();
		
		System.out.println("Trainees in Software Development stream are: ");
		for(Trainee t: softwareTrainees) {
			System.out.println("- "+t.getFirstName()+" "+t.getLastName());
		}

		em.close();
		emf.close();
	}

}
