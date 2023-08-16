package com.example.banktest.customerpackage;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {


    private String firstname, lastname, civilid, address, phone, email;
    private int day,month,year;
    private int numOfAccounts;
}
