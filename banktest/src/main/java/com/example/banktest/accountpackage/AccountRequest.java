package com.example.banktest.accountpackage;

import com.example.banktest.customerpackage.Customer;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Currency;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {

    private String accountId, customer_id;
    private double amount;

    private long transactionPartyId;

    private String transactionCurrency;
    private AccountType accountType;
    private String currency; //assuming that it will be dropdown menu in the interface so the format is always correct




}
