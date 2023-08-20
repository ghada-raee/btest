package com.example.banktest.customertests;

import com.example.banktest.accountpackage.Account;
import com.example.banktest.accountpackage.AccountType;
import com.example.banktest.customerpackage.Customer;
import com.example.banktest.customerpackage.CustomerRepository;
import com.example.banktest.customerpackage.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.r2dbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(CustomerRepository.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryTests {


    @Autowired
    private CustomerRepository customerRepository;

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
    }
    @AfterEach
    void teardown(){
        customerRepository.deleteAll();
    }
    @Test
    public void existsById_returnsTrue(){
        customerRepository.save(customer);
        //when
        boolean exists = customerRepository.existsById(customer.getId());
        //then
        assertTrue(exists);
    }
    @Test
    public void existsById_returnsFalse(){
        boolean exists = customerRepository.existsById("123456");
        assertFalse(exists);
    }

    @Test
    public void existsByCivilid_returnsTrue(){
        customerRepository.save(customer);
        boolean exists = customerRepository.existsByCivilid("123456789012");
        assertTrue(exists);
    }

    @Test
    public void existsByCivilid_returnsFalse(){
        boolean exists = customerRepository.existsByCivilid("123456789011");
        assertFalse(exists);
    }

    @Test
    public void findUserByCivilid_returnsCustomer(){
        customerRepository.save(customer);
        Customer retrievedCustomer = customerRepository.findUserByCivilid("123456789012").orElse(null);
        assertNotNull(retrievedCustomer, "Retrieved customer should not be null");

    }
    @Test
    public void findUserByCivilid_returnsNull(){
        Customer retrievedCustomer = customerRepository.findUserByCivilid("123456789012").orElse(null);
        assertNull(retrievedCustomer, "Retrieved customer should be null");

    }
    @Test
    public void findUserById_returnsCustomer(){
        customerRepository.save(customer);
        Customer retrievedCustomer = customerRepository.findUserById(customer.getId()).orElse(null);
        assertNotNull(retrievedCustomer, "Retrieved customer should not be null");

    }

    @Test
    public void findUserById_returnsNull(){
        Customer retrievedCustomer = customerRepository.findUserById("1234567").orElse(null);
        assertNull(retrievedCustomer, "Retrieved customer should not be null");
    }

}
