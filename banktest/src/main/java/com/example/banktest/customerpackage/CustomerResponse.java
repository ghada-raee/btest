package com.example.banktest.customerpackage;


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

    private String firstname, lastname, email, customer_id,phone,address;

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
