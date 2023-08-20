package com.example.banktest.accounttests;

import com.example.banktest.accountpackage.Account;
import com.example.banktest.accountpackage.AccountRepository;
import com.example.banktest.accountpackage.AccountResponse;
import com.example.banktest.accountpackage.AccountType;
import com.example.banktest.customerpackage.Customer;
import com.example.banktest.customerpackage.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryTests {


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    Account account;
    Customer customer;

    @BeforeEach
    void setUp(){
        customer = new Customer(
                "John",
                "Doe",
                "123456789012",
                LocalDate.of(1990, 5, 15),
                "123 Main St",
                "12345678",
                "john.doe@example.com"
        );

        account = new Account(customer, AccountType.SAVING, Currency.getInstance("KWD"));
        customerRepository.save(customer);
    }
    @AfterEach
    void teardown(){
        customerRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    public void existsById_returnsTrue(){

        accountRepository.save(account);
        boolean exists = accountRepository.existsByAccountId(account.getAccountId());
        assertTrue(exists);
    }
    @Test
    public void existsById_returnsFalse(){
        boolean exists = accountRepository.existsByAccountId(account.getAccountId());
        assertFalse(exists);
    }
    @Test
    public void existsByCustomerAndAccountType_returnsTrue(){
        //customerRepository.save(customer);
        accountRepository.save(account);
        boolean exists = accountRepository.existsByCustomerAndAccountType(customer,account.getAccountType());
        assertTrue(exists);
    }
    @Test
    public void existsByCustomerAndAccountType_returnsFalse(){
        //customerRepository.save(customer);
        boolean exists = accountRepository.existsByCustomerAndAccountType(customer,account.getAccountType());
        assertFalse(exists);
    }

    @Test
    public void findAccountByAccountId_returnsAccount(){
        //customerRepository.save(customer);
        accountRepository.save(account);
        //when
        Account retrievedAccount = accountRepository.findAccountByAccountId(account.getAccountId()).orElse(null);
        //then
        assertNotNull(retrievedAccount, "Retrieved customer should not be null");

    }
    @Test
    public void findAccountByAccountId_returnsNull(){
        Account retrievedAccount = accountRepository.findAccountByAccountId(account.getAccountId()).orElse(null);
        //then
        assertNull(retrievedAccount, "Retrieved customer should be null");
    }
    @Test
    public void findByCustomer_returnsAccounts(){
        accountRepository.save(account);
        List<Account> retrievedAccount = accountRepository.findByCustomer(customer);
        assertNotNull(retrievedAccount, "Retrieved customer should not be null");

    }
    @Test
    public void findByCustomer_returnsNull(){
        List<Account> retrievedAccount = accountRepository.findByCustomer(customer);
        assertTrue(retrievedAccount.isEmpty());
    }

}
