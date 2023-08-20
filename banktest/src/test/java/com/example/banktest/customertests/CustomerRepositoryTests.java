package com.example.banktest.customertests;

import com.example.banktest.customerpackage.Customer;
import com.example.banktest.customerpackage.CustomerRepository;
import com.example.banktest.customerpackage.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(CustomerRepository.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryTests {


    @Autowired
    private CustomerRepository customerRepository;


    @AfterEach
    void teardown(){
        customerRepository.deleteAll();
    }
    @Test
    public void existsById_returnsTrue(){
        //given
        Customer customer = new Customer(
                "John",
                "Doe",
                "123456789012", // Example civil ID
                LocalDate.of(1990, 5, 15), // Example date of birth
                "123 Main St",
                "12345678", // Example phone number
                "john.doe@example.com"
        );
        customerRepository.save(customer);
        //when
        boolean exists = customerRepository.existsById(customer.getId());
        //then
        assertTrue(exists);
    }
    @Test
    public void existsById_returnsFalse(){

        //when
        boolean exists = customerRepository.existsById("123456");
        //then
        assertFalse(exists);
    }

    @Test
    public void existsByCivilid_returnsTrue(){
        //given
        Customer customer = new Customer(
                "John",
                "Doe",
                "123456789012", // Example civil ID
                LocalDate.of(1990, 5, 15), // Example date of birth
                "123 Main St",
                "12345678", // Example phone number
                "john.doe@example.com"
        );
        customerRepository.save(customer);
        //when
        boolean exists = customerRepository.existsByCivilid("123456789012");
        //then
        assertTrue(exists);
    }

    @Test
    public void existsByCivilid_returnsFalse(){

        //when
        boolean exists = customerRepository.existsByCivilid("123456789011");
        //then
        assertFalse(exists);
    }

    @Test
    public void findUserByCivilid_returnsCustomer(){
        //given
        Customer customer = new Customer(
                "John",
                "Doe",
                "123456789012", // Example civil ID
                LocalDate.of(1990, 5, 15), // Example date of birth
                "123 Main St",
                "12345678", // Example phone number
                "john.doe@example.com"
        );
        customerRepository.save(customer);
        //when
        Customer retrievedCustomer = customerRepository.findUserByCivilid("123456789012").orElse(null);
        //then
        assertNotNull(retrievedCustomer, "Retrieved customer should not be null");

    }
    @Test
    public void findUserByCivilid_returnsNull(){
        //when
        Customer retrievedCustomer = customerRepository.findUserByCivilid("123456789012").orElse(null);
        //then
        assertNull(retrievedCustomer, "Retrieved customer should be null");

    }
    @Test
    public void findUserById_returnsCustomer(){
        //given
        Customer customer = new Customer(
                "John",
                "Doe",
                "123456789012", // Example civil ID
                LocalDate.of(1990, 5, 15), // Example date of birth
                "123 Main St",
                "12345678", // Example phone number
                "john.doe@example.com"
        );
        customerRepository.save(customer);
        //when
        Customer retrievedCustomer = customerRepository.findUserById(customer.getId()).orElse(null);
        //then
        assertNotNull(retrievedCustomer, "Retrieved customer should not be null");

    }

    @Test
    public void findUserById_returnsNull(){
        //when
        Customer retrievedCustomer = customerRepository.findUserById("1234567").orElse(null);
        //then
        assertNull(retrievedCustomer, "Retrieved customer should not be null");
    }

}
