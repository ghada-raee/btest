package com.example.banktest.customertests;


import com.example.banktest.GenericResponse;
import com.example.banktest.customerpackage.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/*
@WebMvcTest(controllers = CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)

 */
@WebMvcTest(controllers = CustomerController.class)
public class CustomerControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    CustomerRequest request;

    CustomerResponse customerResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        request= new CustomerRequest("John", "Doe", "123456781234", "12345678",
                "john.doe@example.com", "123 Main St", 20, 9, 1990);
        customerResponse = new CustomerResponse("John", "Doe", "JohnDoe@gmail.com", "1234567",
                "12345678", "123 Main Street, City"
        );

    }

    @Test
    public void addCustomer_Success() throws CustomerException, Exception {
        given(customerService.addCustomer(any(CustomerRequest.class))).willReturn(customerResponse);
        ResultActions response = mockMvc.perform(post("/api/v1/customer/addcustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully created user:"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(customerResponse));
    }
    //All other exceptions will be handled the same way with same result so no need to repeat the code
    @Test
    public void addCustomer_BadRequest_CustomerException() throws CustomerException, Exception {
        //we're telling the service to throw customer exception when called to see how the controller will respond
        given(customerService.addCustomer(any(CustomerRequest.class))).willThrow(new CustomerException("Customer exists"));
        ResultActions response = mockMvc.perform(post("/api/v1/customer/addcustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Customer exists"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());
    }

    @Test
    public void addCustomer_BadRequest_WrongFormat() throws CustomerException, Exception {
        String fname = request.getFirstname();
        request.setFirstname("Lily9");
        ResultActions response = mockMvc.perform(post("/api/v1/customer/addcustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        request.setFirstname(fname);
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname").value("First name must only contain letters and '-' if needed"));
    }

    @Test
    public void getCustomer_Success() throws CustomerException, Exception {
        given(customerService.getCustomer(any(CustomerRequest.class))).willReturn(customerResponse);
        ResultActions response = mockMvc.perform(get("/api/v1/customer/getcustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved customer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(customerResponse));
    }
    @Test
    public void getCustomer_NotFound() throws CustomerException, Exception {
        given(customerService.getCustomer(any(CustomerRequest.class))).willThrow(new CustomerException("Customer doesn't exist"));
        ResultActions response = mockMvc.perform(get("/api/v1/customer/getcustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Customer doesn't exist"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());
    }
    @Test
    public void getCustomer_BadRequest() throws CustomerException, Exception {
        given(customerService.getCustomer(any(CustomerRequest.class))).willThrow(new NullPointerException("Fill the the required fields please"));
        ResultActions response = mockMvc.perform(get("/api/v1/customer/getcustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Fill the the required fields please"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());
    }
    @Test
    public void editCustomer_Success() throws CustomerException, Exception {
        given(customerService.editCustomer(any(CustomerRequest.class))).willReturn(customerResponse);
        ResultActions response = mockMvc.perform(put("/api/v1/customer/editcustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully edited customer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(customerResponse));
    }
    @Test
    public void editCustomer_NotFound() throws CustomerException, Exception {
        given(customerService.editCustomer(any(CustomerRequest.class))).willThrow(new CustomerException("Customer doesn't exist"));
        ResultActions response = mockMvc.perform(put("/api/v1/customer/editcustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Customer doesn't exist"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());
    }






}
