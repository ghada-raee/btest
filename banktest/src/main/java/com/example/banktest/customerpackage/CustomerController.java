package com.example.banktest.customerpackage;


import com.example.banktest.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(
            summary = "to add a new customer to the database",
            responses = {

            @ApiResponse(responseCode = "400", description = "Exception",
                    content = @Content(schema = @Schema(
                            example = "{\"message\":\"Exception Message\",\"status\":400,\"data\":null}"))),
                    @ApiResponse(responseCode = "200", description = "Customer added successfully",
                            content = @Content(schema = @Schema(implementation = CustomerResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(
                                    example = "{\n\"error\":\"Internal Server Error\",\n\"status\":500,\n\"timestamp\"" +
                                            ":2023-08-19T16:52:58.894+00:00" + ",\n\"path\": /api/v1/customer/getcustomer\n}"))),
            }
    )
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
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }

        return GenericResponse.generateResponse(
                "Successfully created user:",
                HttpStatus.OK,
                c
        );
    }
    @GetMapping("/getcustomer")
    @ResponseBody
    @Operation(
            summary = "to get customer data using civilID \nyou only need CIVIL ID field",
            description = "YOU DON'T NEED ALL THE FIELDS, ONLY: civilID. Also, the request won't work in Swagger-UI " +
                    "since OAS 3.0 doesn't support the request body for GET methods",
            responses = {
                    @ApiResponse(responseCode = "400", description = "Exception",
                            content = @Content(schema = @Schema(
                                    example = "{\"message\":\"Exception Message\",\"status\":400,\"data\":null}"))),
                    @ApiResponse(responseCode = "404", description = "Exception",
                            content = @Content(schema = @Schema(
                                    example = "{\"message\":\"Exception Message\",\"status\":404,\"data\":null}"))),
                    @ApiResponse(responseCode = "200", description = "Customer retrieved successfully",
                            content = @Content(schema = @Schema(implementation = CustomerResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(
                                    example = "{\n\"error\":\"Internal Server Error\",\n\"status\":500,\n\"timestamp\"" +
                                            ":2023-08-19T16:52:58.894+00:00" + ",\n\"path\": /api/v1/customer/getcustomer\n}"))),
            }
    )
    public ResponseEntity<Object> getCustomer(
            @Valid
            @RequestBody CustomerRequest customer
    ) {
        CustomerResponse c;
        try {
            c = customerService.getCustomer(customer);
        } catch (CustomerException e) {
            logger.error(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
        catch (NullPointerException e) {
            logger.error(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }

        return GenericResponse.generateResponse(
                "Successfully retrieved customer",
                HttpStatus.OK,
                c
        );
    }

    @PutMapping("/editcustomer")
    @ResponseBody
    @Operation(
            summary = "to edit customer data: phone, address or email",
            description = "YOU DON'T NEED ALL THE FIELDS, ONLY: civilID. Optional fields: phone, address, email",
            responses = {
                    @ApiResponse(responseCode = "403", description = "Exception",
                            content = @Content(schema = @Schema(
                                    example = "{\"message\":\"Exception Message\",\"status\":403,\"data\":null}"))),
                    @ApiResponse(responseCode = "200", description = "Customer edited successfully",
                            content = @Content(schema = @Schema(implementation = CustomerResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(
                                    example = "{\n\"error\":\"Internal Server Error\",\n\"status\":500,\n\"timestamp\"" +
                                            ":2023-08-19T16:52:58.894+00:00" + ",\n\"path\": /api/v1/customer/getcustomer\n}"))),
            }
    )
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

