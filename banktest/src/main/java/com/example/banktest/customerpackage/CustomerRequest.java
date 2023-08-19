package com.example.banktest.customerpackage;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Email;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@Data
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {



    @Pattern(regexp = "^[A-Za-z-]+$", message = "First name must only contain letters and '-' if needed")
    private String firstname;

    @Pattern(regexp = "^[A-Za-z-]+$", message = "Last name must only contain letters and '-' if needed")
    private String lastname;

    @Pattern(regexp = "^[0-9]{12}$", message = "Civil ID must be a 12-digit string")
    private String civilid;

    @Pattern(regexp = "^[0-9]{8}$", message = "Phone must be an 8-digit string")
    private String phone;
    @Email(message = "Email must be in a correct format")
    private String email;

    private String address;


    private int day;


    private int month;


    private int year;


}
