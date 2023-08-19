package com.example.banktest.customerpackage;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerResponse {

    @Schema(description = "First name", example = "John")
    private String firstname;

    @Schema(description = "Last name", example = "Doe")
    private String lastname;

    @Schema(description = "Email", example = "JohnDoe@gmail.com")
    private String email;

    @Pattern(regexp = "^[0-9]{7}$")//for swagger
    private String customer_id;

    @Pattern(regexp = "^[0-9]{8}$")// for swagger
    private String phone;

    @Schema(description = "Address", example = "123 Main Street, City")
    private String address;

    public CustomerResponse convert(Customer c) {
        this.setCustomer_id(c.getId());
        this.setEmail(c.getEmail());
        this.setFirstname(c.getFirstname());
        this.setLastname(c.getLastname());
        this.setAddress(c.getAddress());
        this.setPhone((c.getPhone()));

        return this;
    }


}
