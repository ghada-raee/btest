package com.example.banktest.accountpackage;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;


@Entity(name="Transactions")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transaction {

    @Id
    @Column(name = "transaction_id", length = 15, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transactionId;
    @Column(name = "account_id", length = 10, nullable = false)
    private String accountId;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @Column(name = "transaction_party_id", nullable = false)
    private String transactionPartyId; //will make it string and id only for simplicity

    public Transaction(String accountId, double amount,String transactionPartyId){
        this.accountId = accountId;
        this.amount = amount;
        this.transactionPartyId = transactionPartyId;
        time = LocalDateTime.now();

    }




}
