package com.fdmgroup.bankApi.service;

import static org.mockito.ArgumentMatchers.any; 
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fdmgroup.bankApi.model.Address;
import com.fdmgroup.bankApi.model.Company;
import com.fdmgroup.bankApi.model.Customer;
import com.fdmgroup.bankApi.model.Person;
import com.fdmgroup.bankApi.repository.CustomerRepository;

class CustomerServiceTest {

    @Mock
    private CustomerRepository repository;

    private CustomerService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new CustomerService(repository);
    }

    @Test
    void shouldCreateCustomer() {
        Customer customer = new Person();
        customer.setName("Alice");

        Address address = new Address();
        address.setCity("London");
        address.setProvince("Greater London");
        address.setPostalCode("E1 6AN");

        customer.setAddress(address);

        when(repository.save(customer)).thenReturn(customer);

        Customer created = service.create(customer);

        assertNotNull(created);
        verify(repository).save(customer);
    }

    @Test
    void shouldReturnNullWhenCreatingNullCustomer() {
        Customer created = service.create(null);
        assertNull(created);
    }

    @Test
    void shouldReturnNullWhenCreatingCustomerWithNullName() {
        Customer customer = new Company(); // name is null
        Customer created = service.create(customer);
        assertNull(created);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldFindCustomerById() {
        Customer customer = new Person();
        customer.setName("Bob");

        when(repository.findById(1L)).thenReturn(Optional.of(customer));

        Customer found = service.findById(1L);

        assertNotNull(found);
        assertEquals("Bob", found.getName());
    }

    @Test
    void shouldReturnNullIfCustomerNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Customer found = service.findById(99L);

        assertNull(found);
    }

    @Test
    void shouldReturnAllCustomers() {
        List<Customer> customers = Arrays.asList(new Person(), new Company());
        when(repository.findAll()).thenReturn(customers);

        List<Customer> result = service.findAll();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void shouldUpdateExistingCustomer() {
        Company existing = new Company();
        existing.setName("Old Name");

        Address existingAddress = new Address("10 Street", "A1", "OldCity", "OldProvince");
        existing.setAddress(existingAddress);

        Company updated = new Company();
        updated.setName("FDM");

        Address updatedAddress = new Address("10 Street", "E1 6AN", "London", "Greater London");
        updated.setAddress(updatedAddress);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        Customer result = service.update(1L, updated);

        assertNotNull(result);
        assertEquals("FDM", result.getName());
        assertEquals("London", result.getAddress().getCity());

        verify(repository).save(existing);
    }

    @Test
    void shouldReturnNullWhenUpdatingNonExistingCustomer() {
        Customer updated = new Person();
        updated.setName("Dave");

        when(repository.existsById(99L)).thenReturn(false);

        Customer result = service.update(99L, updated);

        assertNull(result);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldDeleteExistingCustomer() {
        when(repository.existsById(1L)).thenReturn(true);

        boolean deleted = service.delete(1L);

        assertTrue(deleted);
        verify(repository).deleteById(1L);
    }

    @Test
    void shouldReturnFalseWhenDeletingNonExistingCustomer() {
        when(repository.existsById(99L)).thenReturn(false);

        boolean deleted = service.delete(99L);

        assertFalse(deleted);
        verify(repository, never()).deleteById(anyLong());
    }
}
