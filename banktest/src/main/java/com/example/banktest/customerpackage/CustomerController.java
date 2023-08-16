package com.example.banktest.customerpackage;


import com.example.banktest.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
             @RequestBody CustomerRequest customer
    ) {

        Customer c;
        try {
            c = customerService.addCustomer(customer);
            logger.info("Customer added");
        } catch (CustomerException e) {
            logger.warn(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.OK, null);
        }

        return GenericResponse.generateResponse(
                "Successfully created user: {user}".replace("{user}", c.getId()),
                HttpStatus.OK,
                "success"
        );
    }
    @GetMapping("/getcustomer")
    @ResponseBody
    public ResponseEntity<Object> getCustomer(
            @RequestBody CustomerRequest customer
    ) {
        Customer c;
        try {
            c = customerService.getCustomer(customer);
        } catch (CustomerException e) {
            logger.warn(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.OK, null);
        }

        return GenericResponse.generateResponse(
                "Successfully retrieved customer: {customer}".replace("{customer}", c.getId()),
                HttpStatus.OK,
                c
        );
    }

    @PutMapping("/editcustomer")
    @ResponseBody
    public ResponseEntity<Object> editCustomer(
            @RequestBody CustomerRequest customer
    ) {

        Customer c;
        try {
            c = customerService.editCustomer(customer);
        } catch (CustomerException e) {
            logger.warn(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.OK, null);
        }

        return GenericResponse.generateResponse(
                "Successfully edited customer: {customer}".replace("{customer}", c.getId()),
                HttpStatus.OK,
                c
        );
    }


}
