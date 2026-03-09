package introduction;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {

    public static void main(String[] args) {

        // 1. Create EntityManagerFactory (reads persistence.xml)
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("jpabasic");

        // 2. Create DAO
        UserDao userDao = new UserDao(emf);

        // 3. Persist all users (Phase 4 requirement)
        userDao.persist(new User(
                "john.smith",
                "apple123",
                "John",
                "Smith"
        ));

        userDao.persist(new User(
                "jane.doe",
                "banana456",
                "Jane",
                "Doe"
        ));

        userDao.persist(new User(
                "joe.bloggs",
                "orange789",
                "Joe",
                "Bloggs"
        ));

        // 4. Find and print all users
        System.out.println("All users:");
        userDao.findAll().forEach(System.out::println);

        // 5. Find user with username "jane.doe" and update password
        userDao.findByUsername("jane.doe").ifPresent(user -> {
            user.setPassword("newPassword123");
            userDao.update(user);
        });

        // 6. Delete user with id 1
        userDao.deleteById(1);

        // 7. Close factory
        emf.close();
    }
}