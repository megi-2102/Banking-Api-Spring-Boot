package com.fdmgroup.bankApi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fdmgroup.bankApi.model.Account;
import com.fdmgroup.bankApi.model.CheckingAccount;
import com.fdmgroup.bankApi.model.Customer;
import com.fdmgroup.bankApi.model.Person;
import com.fdmgroup.bankApi.model.SavingsAccount;
import com.fdmgroup.bankApi.repository.AccountRepository;

class AccountServiceTest {

    @Mock
    private AccountRepository repository;

    private AccountService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new AccountService(repository);
    }

    @Test
    void shouldCreateAccount() {
        Account account = new CheckingAccount();
        when(repository.save(account)).thenReturn(account);

        Account created = service.create(account);

        assertNotNull(created);
        verify(repository).save(account);
    }
    
    @Test
    void shouldReturnNullWhenCreatingNullAccount() {
        Account created = service.create(null);
        assertNull(created);
    }

    @Test
    void shouldFindAccountById() {
        Account account = new CheckingAccount();
        when(repository.findById(1L)).thenReturn(Optional.of(account));

        Account found = service.findById(1L);

        assertNotNull(found);
    }

    @Test
    void shouldReturnNullIfAccountNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Account found = service.findById(99L);

        assertNull(found);
    }

    @Test
    void shouldReturnAllAccounts() {
        when(repository.findAll()).thenReturn(Arrays.asList(
                new CheckingAccount(),
                new CheckingAccount()
        ));
        assertEquals(2, service.findAll().size());
    }

    @Test
    void shouldUpdateBalance() {
        CheckingAccount existing = new CheckingAccount();
        existing.setBalance(100);

        CheckingAccount updated = new CheckingAccount();
        updated.setBalance(200);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        Account result = service.update(1L, updated);

        assertEquals(200, result.getBalance());
    }
    
    @Test
    void shouldUpdateInterestRateForSavingsAccount() {
        SavingsAccount existing = new SavingsAccount();
        existing.setBalance(1000);
        existing.setInterestRate(1.5);

        SavingsAccount updated = new SavingsAccount();
        updated.setBalance(1000);
        updated.setInterestRate(3.0);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        Account result = service.update(1L, updated);

        assertEquals(3.0, ((SavingsAccount) result).getInterestRate());
        verify(repository).save(existing);
    }
    
    @Test
    void shouldUpdateNextCheckNumber() {
        CheckingAccount existing = new CheckingAccount();
        existing.setNextCheckNumber(10);

        CheckingAccount updated = new CheckingAccount();
        updated.setNextCheckNumber(20);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        Account result = service.update(1L, updated);

        assertEquals(20, ((CheckingAccount) result).getNextCheckNumber());
    }
    
    @Test
    void shouldReturnNullWhenUpdatingNonExistingAccount() {
        CheckingAccount updated = new CheckingAccount();
        updated.setBalance(200);

        when(repository.findById(99L)).thenReturn(Optional.empty());

        Account result = service.update(99L, updated);
        assertNull(result);
    }
    
    @Test
    void shouldRemoveAccountFromCustomerWhenDeleting() {
        Customer customer = new Person();

        CheckingAccount account = new CheckingAccount();
        account.setCustomer(customer);

        customer.getAccounts().add(account);

        when(repository.findById(1L)).thenReturn(Optional.of(account));

        boolean result = service.delete(1L);

        assertTrue(result);
        assertFalse(customer.getAccounts().contains(account));
        verify(repository).delete(account);
    }
    
    @Test
    void shouldReturnFalseWhenDeletingNonExistingAccount() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        boolean result = service.delete(99L);
        
        assertFalse(result);
    }
    
    @Test
    void testFindAccountsByCustomerCity_returnsAccounts() {

        String city = "Toronto";

        SavingsAccount account = new SavingsAccount();
        account.setBalance(5000.0);

        List<Account> accounts = List.of(account);

        when(repository.findAccountsByCustomerCity(city))
                .thenReturn(accounts);

        List<Account> result = service.findAccountsByCustomerCity(city);

        assertEquals(1, result.size());
        assertEquals(5000.0, result.get(0).getBalance());

        verify(repository).findAccountsByCustomerCity(city);
    }
}
