package com.example.banktest.customertests;

import com.example.banktest.BadRequestException;
import com.example.banktest.customerpackage.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTests {
    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    CustomerService customerService;

    private Validator validator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    @Test
    public void addCustomer_ValidCustomer_ReturnsCustomerResponse() throws CustomerException {
        CustomerRequest validCustomer = createValidCustomerRequest();
        when(customerRepository.existsByCivilid(validCustomer.getCivilid())).thenReturn(false);
        CustomerResponse response = customerService.addCustomer(validCustomer);
        assertNotNull(response.getCustomer_id());

    }
    @Test
    public void addCustomer_NullField_ReturnsException() {
        CustomerRequest InvalidCustomer = createValidCustomerRequest();
        InvalidCustomer.setEmail(null);
        assertThrows(NullPointerException.class, () -> customerService.addCustomer(InvalidCustomer));
    }
    @Test
    public void addCustomer_WrongFormatInput_ReturnsException() {
        CustomerRequest InvalidCustomer = createValidCustomerRequest();
        InvalidCustomer.setFirstname("John9"); //works with all the wrong format inputs too, with different messages as a response
        Set<ConstraintViolation<CustomerRequest>> violations = validator.validate(InvalidCustomer);
        assertFalse(violations.isEmpty());
        ConstraintViolation<CustomerRequest> violation = violations.iterator().next();
        assertEquals("First name must only contain letters and '-' if needed", violation.getMessage());
    }

    @Test
    public void addCustomer_NegativeDate_ReturnsException(){
        CustomerRequest InvalidCustomer = createValidCustomerRequest();
        InvalidCustomer.setMonth(-10);
        assertThrows(IllegalArgumentException.class, () -> customerService.addCustomer(InvalidCustomer));
    }
    @Test
    public void addCustomer_CustomerExists_ReturnsException(){
        CustomerRequest validCustomer = createValidCustomerRequest();
        when(customerRepository.existsByCivilid(validCustomer.getCivilid())).thenReturn(true);
        assertThrows(CustomerException.class, () -> customerService.addCustomer(validCustomer));
    }

    @Test
    public void addCustomer_IllegalAge_ReturnsException(){
        CustomerRequest InvalidCustomer = createValidCustomerRequest();
        InvalidCustomer.setYear(2015);
        when(customerRepository.existsByCivilid(InvalidCustomer.getCivilid())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> customerService.addCustomer(InvalidCustomer));
    }

    @Test
    public void getCustomer_ValidInput_ReturnsCustomerResponse() throws CustomerException {
        CustomerRequest validCustomer = createValidCustomerRequest();
        Customer customer = createValidCustomer();
        when(customerRepository.findUserByCivilid(validCustomer.getCivilid())).thenReturn(Optional.of(customer));
        CustomerResponse response = customerService.getCustomer(validCustomer);
        assertNotNull(response.getCustomer_id());
    }

    @Test
    public void getCustomer_NullCivilId_ReturnsException(){
        CustomerRequest InvalidCustomer = createValidCustomerRequest();
        InvalidCustomer.setCivilid(null);
        assertThrows(NullPointerException.class, () -> customerService.getCustomer(InvalidCustomer));
    }

    @Test
    public void getCustomer_WrongFormatCivilId_ReturnsException(){
        CustomerRequest InvalidCustomer = createValidCustomerRequest();
        InvalidCustomer.setCivilid("77554412341");
        Set<ConstraintViolation<CustomerRequest>> violations = validator.validate(InvalidCustomer);
        assertFalse(violations.isEmpty());
        ConstraintViolation<CustomerRequest> violation = violations.iterator().next();
        assertEquals("Civil ID must be a 12-digit string", violation.getMessage());

    }

    @Test
    public void getCustomer_CustomerDoesntExist_ReturnsException(){
        CustomerRequest validCustomer = createValidCustomerRequest();
        when(customerRepository.findUserByCivilid(validCustomer.getCivilid())).thenReturn(null);
        assertThrows(NullPointerException.class, () -> customerService.getCustomer(validCustomer));
    }

    @Test
    public void editCustomer_ValidEdits_ReturnsCustomerResponse() throws CustomerException {
        CustomerRequest validCustomer = createValidCustomerRequest();
        Customer customer = createValidCustomer();
        when(customerRepository.findUserByCivilid(validCustomer.getCivilid())).thenReturn(Optional.of(customer));
        CustomerResponse response = customerService.editCustomer(validCustomer);
        assertNotNull(response.getCustomer_id());
    }

    @Test
    public void editCustomer_NullCivilId_ReturnsException(){
        CustomerRequest InvalidCustomer = createValidCustomerRequest();
        InvalidCustomer.setCivilid("");
        assertThrows(CustomerException.class, () -> customerService.editCustomer(InvalidCustomer));
    }

    @Test
    public void editCustomer_CustomerDoesntExist_ReturnsException(){
        CustomerRequest validCustomer = createValidCustomerRequest();
        when(customerRepository.findUserByCivilid(validCustomer.getCivilid())).thenReturn(null);
        assertThrows(NullPointerException.class, () -> customerService.editCustomer(validCustomer));
    }

    private CustomerRequest createValidCustomerRequest() {
        CustomerRequest request = new CustomerRequest("John", "Doe", "123456781234", "12345678",
                "john.doe@example.com", "123 Main St", 20, 9, 1990);
        return request;
    }

    private Customer createValidCustomer() {
        LocalDate dob = LocalDate.of(1990, 8, 20); // Example date of birth
        Customer customer = new Customer("John", "Doe", "123456781234", dob,
                "123 Main St", "12345678", "john.doe@example.com");
        return customer;
    }

}
