package com.fdmgroup.bankApi.controller;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.bankApi.model.Address;
import com.fdmgroup.bankApi.model.Person;
import com.fdmgroup.bankApi.model.Customer;
import com.fdmgroup.bankApi.service.CustomerService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Customer createCustomer() {
        Address address = new Address("12 Street", "Toronto", "Ontario", "M5V");
        Person person = new Person("John", address);
        return person;
    }

    @Test
    void testCreateCustomer() throws Exception {
        Customer customer = createCustomer();
        when(service.create(any(Customer.class))).thenReturn(customer);

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated());
    }
    
    @Test
    void testCreateCustomerConflict() throws Exception {
        Customer customer = createCustomer();
        when(service.create(any(Customer.class))).thenReturn(null);

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isConflict());
    }

    @Test
    void testFindCustomerById() throws Exception {
        Customer customer = createCustomer();
        when(service.findById(1)).thenReturn(customer);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void testFindCustomerNotFound() throws Exception {
        when(service.findById(1)).thenReturn(null);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindAllCustomers() throws Exception {
        Customer customer = createCustomer();
        when(service.findAll()).thenReturn(List.of(customer));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testFindAllCustomersEmpty() throws Exception {
        when(service.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Customer existing = createCustomer();
        Customer updated = createCustomer();

        when(service.findById(1)).thenReturn(existing);
        when(service.update(1, existing)).thenReturn(updated);

        mockMvc.perform(put("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateCustomerNotFound() throws Exception {
        Customer updated = createCustomer();
        when(service.findById(1)).thenReturn(null);

        mockMvc.perform(put("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateCustomerConflict() throws Exception {
        Customer existing = createCustomer();
        Customer updated = createCustomer();

        when(service.findById(1)).thenReturn(existing);
        when(service.update(1, existing)).thenReturn(null);

        mockMvc.perform(put("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isConflict());
    }

    @Test
    void testDeleteCustomer() throws Exception {
        when(service.delete(1)).thenReturn(true);

        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteCustomerNotFound() throws Exception {
        when(service.delete(1)).thenReturn(false);

        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNotFound());
    }
}