package com.example.banktest.accountpackage;

import com.example.banktest.customerpackage.Customer;
import com.example.banktest.customerpackage.CustomerResponse;
import io.swagger.v3.oas.annotations.media.Schema;
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


    @Schema(description = "accountId", example = "3806175901")
    private String accountId;
    @Schema(description = "balance", example = "26.0")
    private Double balance;
    @Schema(description = "accountType", example = "SAVING")
    private AccountType accountType;
    @Schema(description = "currency", example = "KWD")
    private Currency currency;
    @Schema(description = "is_active", example = "true")
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
