package com.fdmgroup.bankApi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fdmgroup.bankApi.model.Account;
import com.fdmgroup.bankApi.model.CheckingAccount;
import com.fdmgroup.bankApi.model.Customer;
import com.fdmgroup.bankApi.model.SavingsAccount;
import com.fdmgroup.bankApi.service.AccountService;
import com.fdmgroup.bankApi.service.CustomerService;

import org.springframework.http.MediaType;

@WebMvcTest(AccountController.class)
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private CustomerService customerService;

    @Test
    void shouldReturnAllAccounts() throws Exception {

        when(accountService.findAll())
                .thenReturn(Arrays.asList(new CheckingAccount(), new CheckingAccount()));

        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
    
    @Test
    void shouldReturnNoContentIfNoAccounts() throws Exception {
        when(accountService.findAll()).thenReturn(Arrays.asList());
        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isNoContent()); 
        verify(accountService).findAll();
    }

    @Test
    void shouldReturnAccountById() throws Exception {

        CheckingAccount account = new CheckingAccount();
        account.setBalance(500);

        when(accountService.findById(1L)).thenReturn(account);

        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(500));
    }

    @Test
    void shouldReturn404WhenAccountNotFound() throws Exception {

        when(accountService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/accounts/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateAccountForCustomer_success() throws Exception {

        long customerId = 1L;

        Customer customer = mock(Customer.class);

        Account createdAccount = mock(Account.class);
        when(createdAccount.getAccountId()).thenReturn(10L);

        when(customerService.findById(customerId)).thenReturn(customer);
        when(accountService.create(any(Account.class))).thenReturn(createdAccount);

        String json = """
            {
                "type": "savings",
                "balance": 5000,
                "interestRate": 0.05
            }
            """;

        mockMvc.perform(post("/api/accounts/customer/{customerId}", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }
    
    @Test
    void testCreateAccountForCustomer_customerNotFound() throws Exception {

        long customerId = 99L;

        when(customerService.findById(customerId)).thenReturn(null);

        String json = """
            {
                "type": "checking",
                "balance": 2000,
                "nextCheckNumber": 100
            }
            """;

        mockMvc.perform(post("/api/accounts/customer/{customerId}", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testCreateAccountForCustomer_conflict() throws Exception {

        long customerId = 1L;

        Customer customer = mock(Customer.class);

        when(customerService.findById(customerId)).thenReturn(customer);
        when(accountService.create(any(Account.class))).thenReturn(null);

        String json = """
            {
                "type": "savings",
                "balance": 3000,
                "interestRate": 0.03
            }
            """;

        mockMvc.perform(post("/api/accounts/customer/{customerId}", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isConflict());
    }
    
    @Test
    void shouldUpdateAccount_success() throws Exception {

        CheckingAccount existing = new CheckingAccount();
        existing.setBalance(100);

        when(accountService.findById(1L)).thenReturn(existing);
        when(accountService.update(eq(1L), any())).thenReturn(existing);

        mockMvc.perform(put("/api/accounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "type": "checking",
                          "balance": 300,
                          "nextCheckNumber": 1001
                        }
                        """))
                .andExpect(status().isOk());
    }
    
    @Test
    void testUpdateAccount_conflict() throws Exception {

        long id = 1L;

        SavingsAccount existing = new SavingsAccount();
        existing.setBalance(1000.0);

        when(accountService.findById(id)).thenReturn(existing);
        when(accountService.update(eq(id), any(Account.class)))
                .thenReturn(null);

        String json = """
            {
                "type": "savings",
                "balance": 3000,
                "interestRate": 0.05
            }
            """;

        mockMvc.perform(put("/api/accounts/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isConflict());

        verify(accountService).update(eq(id), any(Account.class));
    }
    
    @Test
    void testUpdateAccount_notFound() throws Exception {

        long id = 99L;

        when(accountService.findById(id)).thenReturn(null);

        String json = """
            {
                "type": "savings",
                "balance": 4000,
                "interestRate": 0.05
            }
            """;

        mockMvc.perform(put("/api/accounts/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());

        verify(accountService).findById(id);
    }

    @Test
    void shouldDeleteAccount_success() throws Exception {

        CheckingAccount existing = new CheckingAccount();
        when(accountService.findById(1L)).thenReturn(existing);

        mockMvc.perform(delete("/api/accounts/1"))
                .andExpect(status().isOk());
    }
    
    @Test
    void testDeleteAccount_notFound() throws Exception {

        long id = 99L;

        when(accountService.findById(id)).thenReturn(null);

        mockMvc.perform(delete("/api/accounts/{id}", id))
                .andExpect(status().isNotFound());

        verify(accountService).findById(id);
    }
    
    @Test
    void testGetAccountsByCity() throws Exception {

        String city = "Toronto";

        SavingsAccount account = new SavingsAccount();
        account.setBalance(5000.0);

        List<Account> accounts = List.of(account);

        when(accountService.findAccountsByCustomerCity(city))
                .thenReturn(accounts);

        mockMvc.perform(get("/api/accounts/city/{city}", city))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].balance").value(5000.0));
    }
    
    @Test
    void shouldReturnNoContentIfNoAccountsInCity() throws Exception {
        String city = "Nowhere";
        when(accountService.findAccountsByCustomerCity(city)).thenReturn(Arrays.asList());
        mockMvc.perform(get("/api/accounts/city/{city}", city))
                .andExpect(status().isNoContent()); 
        verify(accountService).findAccountsByCustomerCity(city);
    }
}
