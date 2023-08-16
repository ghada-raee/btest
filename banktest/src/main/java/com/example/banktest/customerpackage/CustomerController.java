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
    @PostMapping("/adduser")
    @ResponseBody
    public ResponseEntity<Object> add(
             @RequestBody CustomerRequest customer
    ) {

        Customer c;
        try {
            c = customerService.add(customer);
        } catch (Exception exception) {
            logger.warn(exception.getMessage());
            return GenericResponse.generateResponse(exception.getMessage(), HttpStatus.OK, null);
        }

        return GenericResponse.generateResponse(
                "Successfully created user: {user}".replace("{user}", c.getId()),
                HttpStatus.OK,
                "success"
        );
    }


}
