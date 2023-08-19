package com.example.banktest.accountpackage;

import com.example.banktest.customerpackage.Customer;
import io.swagger.v3.oas.annotations.media.Schema;
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


    @Schema(description = "accountId", example = "3806175901")
    private String accountId;
    @Schema(description = "customer_id", example = "3806175")
    private String customer_id;
    @Schema(description = "amount", example = "3.0")
    private double amount;
    @Schema(description = "transactionPartyId", example = "123456")
    private long transactionPartyId;
    @Schema(description = "transactionCurrency", example = "KWD")
    private String transactionCurrency;

    @Schema(description = "accountType", example = "SAVING")
    private AccountType accountType;

    @Schema(description = "currency", example = "USD")
    private String currency; //assuming that it will be dropdown menu in the interface so the format is always correct




}
