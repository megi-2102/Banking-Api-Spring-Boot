package inheritance;

import jakarta.persistence.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("jpabasic");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        Person p1 = new Person("123 Fake Street", "John", "Smith");

        Company c1 = new Company("456 Real Street", "Google", 123456789);

        em.persist(p1);
        em.persist(c1);

        SavingsAccount sa1 = new SavingsAccount(1234.50);
        sa1.setCustomer(p1);
        
        CheckingAccount ca1 = new CheckingAccount(-250.00);
        ca1.setCustomer(c1);
        
        CheckingAccount ca2 = new CheckingAccount(-525.25);
        ca1.setCustomer(p1);
        
        SavingsAccount sa2 = new SavingsAccount(10500.00);
        sa2.setCustomer(c1);
        
        em.persist(sa1);
        em.persist(ca1);
        em.persist(sa2);
        em.persist(ca2);

        em.getTransaction().commit();

        // ---- PHASE 4 ----

        // 1. Find all bank accounts
        List<BankAccount> accounts =
                em.createNamedQuery("BankAccount.findAll", BankAccount.class)
                  .getResultList();
        
        for(BankAccount b: accounts)
        	{
        		System.out.println("Account ID: " + b.getId()  + " Balance: " + b.getBalance());
        	}
                   

        // 2. Find Google
        Company google =
                em.createNamedQuery("Company.findByName", Company.class)
                  .setParameter("companyName", "Google")
                  .getSingleResult();

        System.out.println("Company: " + google.getName()
                + " ABN: " + google.getAbn());

        // 3. Deposit $100 into all savings accounts
        em.getTransaction().begin();

        List<SavingsAccount> savings =
                em.createNamedQuery("SavingsAccount.findAll", SavingsAccount.class)
                  .getResultList();

        savings.forEach(s -> s.deposit(100));

        em.getTransaction().commit();

        em.close();
        emf.close();
    }
}