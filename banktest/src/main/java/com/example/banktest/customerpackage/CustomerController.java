package com.example.banktest.customerpackage;


import com.example.banktest.GenericResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;
    Logger logger = LoggerFactory.getLogger(Customer.class);
    //the add is to add users for testing, not part of requirments
    @PostMapping("/addcustomer")
    @ResponseBody
    public ResponseEntity<Object> addCustomer(
            @Valid
             @RequestBody CustomerRequest customer
    ) {

        CustomerResponse c;
        try {
            c = customerService.addCustomer(customer);
            logger.info("Customer added");
        } catch (CustomerException | NullPointerException | IllegalArgumentException e) {
            logger.error(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.OK, null);
        }

        return GenericResponse.generateResponse(
                "Successfully created user:",
                HttpStatus.OK,
                c
        );
    }
    @GetMapping("/getcustomer")
    @ResponseBody
    public ResponseEntity<Object> getCustomer(
            @Valid
            @RequestBody CustomerRequest customer
    ) {
        CustomerResponse c;
        try {
            c = customerService.getCustomer(customer);
        } catch (CustomerException | NullPointerException e) {
            logger.error(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.OK, null);
        }

        return GenericResponse.generateResponse(
                "Successfully retrieved customer",
                HttpStatus.OK,
                c
        );
    }

    @PutMapping("/editcustomer")
    @ResponseBody
    public ResponseEntity<Object> editCustomer(
            @Valid
            @RequestBody CustomerRequest customer
    ) {

        CustomerResponse c;
        try {
            c = customerService.editCustomer(customer);
        } catch (CustomerException e) {
            logger.error(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.OK, null);
        }

        return GenericResponse.generateResponse(
                "Successfully edited customer",
                HttpStatus.OK,
                c
        );
    }





}

