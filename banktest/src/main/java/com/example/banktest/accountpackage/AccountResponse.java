package com.example.banktest.accountpackage;

import com.example.banktest.customerpackage.Customer;
import com.example.banktest.customerpackage.CustomerResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Currency;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountResponse {

    private String accountId;
    private Double balance;
    private AccountType accountType;
    private Currency currency;
    private boolean is_active;

    public AccountResponse convert(Account a) {
        this.setAccountId(a.getAccountId());
        this.setBalance(a.getBalance());
        this.setAccountType(a.getAccountType());
        this.setCurrency(a.getCurrency());
        this.set_active(a.is_active());
        return this;
    }
}
