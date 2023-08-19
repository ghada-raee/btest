package com.example.banktest.customerpackage;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "First name", example = "John")
    private String firstname;

    @Pattern(regexp = "^[A-Za-z-]+$", message = "Last name must only contain letters and '-' if needed")
    @Schema(description = "Last name", example = "Doe")
    private String lastname;

    @Pattern(regexp = "^[0-9]{12}$", message = "Civil ID must be a 12-digit string")
    private String civilid;

    @Pattern(regexp = "^[0-9]{8}$", message = "Phone must be an 8-digit string")
    private String phone;
    @Email(message = "Email must be in a correct format")
    @Schema(description = "Email", example = "JohnDoe@gmail.com")
    private String email;

    @Schema(description = "Address", example = "123 Main Street, City")
    private String address;

    @Schema(description = "Day of birth", example = "18")
    private int day;
    @Schema(description = "Month of birth", example = "2")
    private int month;

    @Schema(description = "Year of birtyh", example = "1995")
    private int year;


}
