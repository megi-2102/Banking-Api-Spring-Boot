package introduction;
import java.util.List;
import java.util.Optional;

public interface IUserDao {
	
	void persist(User user);
	Optional<User> findById(int id);
	Optional<User> findByUsername(String username);
	void deleteById(int id);
	void update(User user);
	List<User> findAll();
	
	

}
