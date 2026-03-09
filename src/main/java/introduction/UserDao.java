package introduction;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.*;

public class UserDao implements IUserDao{
	
	private EntityManagerFactory emf;
	
	public UserDao(EntityManagerFactory emf)
	{
		this.emf = emf;
	}
	
	@Override
	public void persist(User user)
	{
		EntityManager em = emf.createEntityManager();
	    em.getTransaction().begin();   // start transaction
	    em.persist(user);              // make entity managed + persistent
	    em.getTransaction().commit();  // commit to DB
        em.close();
	}
	
	@Override
	public Optional<User> findByUsername(String username) {
	    EntityManager em = emf.createEntityManager();

	    try {
	        User user = em.createQuery(
	            "SELECT u FROM User u WHERE u.username = :username",
	            User.class)
	            .setParameter("username", username)
	            .getSingleResult();

	        return Optional.of(user);

	    } catch (NoResultException e) {
	        return Optional.empty();
	    } finally {
	        em.close();
	    }
	}
	
	@Override
	public Optional<User> findById(int id) {
	    EntityManager em = emf.createEntityManager();
	    User user = em.find(User.class, id);
	    em.close();
	    return Optional.ofNullable(user);
	}

	@Override
	public void deleteById(int id) {
	    EntityManager em = emf.createEntityManager();

	    em.getTransaction().begin();

	    User user = em.find(User.class, id);
	    if (user != null) {
	        em.remove(user);   // only works on managed entities
	    }

	    em.getTransaction().commit();
	    em.close();
	}

	@Override
	public void update(User user) {
	    EntityManager em = emf.createEntityManager();

	    em.getTransaction().begin();
	    em.merge(user);   // merges detached entity into persistence context
	    em.getTransaction().commit();

	    em.close();
	}

	@Override
	public List<User> findAll() {
	    EntityManager em = emf.createEntityManager();

	    List<User> users = em.createQuery(
	        "SELECT u FROM User u", User.class)
	        .getResultList();

	    em.close();
	    return users;
	}

}
