package com.example.banktest.accountpackage;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionResponse {

    private long transactionId;
    private double balance,amount;
    private LocalDateTime time;
    private String transactionPartyId; //will make it string and id only for simplicity

    public TransactionResponse convert(Transaction t,double balance) {
        this.setBalance(balance);
        this.setTransactionId(t.getTransactionId());
        this.setAmount(t.getAmount());
        this.setTime(t.getTime());
        this.setTransactionPartyId(t.getTransactionPartyId());
        return this;
    }

}
