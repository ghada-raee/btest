package com.example.banktest.customerpackage;


import jakarta.persistence.*;
import lombok.*;
import org.intellij.lang.annotations.Pattern;

import java.util.ArrayList;
import java.util.Random;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Account {
    @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", length = 10)
    private String accountId;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "account_type")
    private AccountType accountType;

    //condtr needs only type and customer id, init balance to zero, generate id in const


    public Account(Customer customer, AccountType accountType) {
        this.customer = customer;
        this.accountType = accountType;
        balance = 0.0;
        Random random = new Random();
        int digits3 = random.nextInt(900) + 100;
        accountId = customer.getId()  + digits3;
        /*
        String id = customer.getId() + "" + digits3;
        accountId = Long.parseLong(id);

         */
    }
}
