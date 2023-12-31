package com.example.banktest.accountpackage;


import com.example.banktest.customerpackage.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Random;



@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name="account")
public class Account {
    @Id
    @Column(name = "account_id", length = 10)
    private String accountId;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "account_type")
    private AccountType accountType;

    @Column(name = "currency")
    private Currency currency;

    @Column(name = "is_active")
    private boolean is_active;

    @Column(name = "date_opened")
    private LocalDate dateopened;

    //condtr needs only type and customer id, init balance to zero, generate id in const


    public Account(Customer customer, AccountType accountType, Currency currency) {
        this.customer = customer;
        this.accountType = accountType;
        this.currency = currency;
        balance = 0.0;
        is_active= true;
        dateopened = LocalDate.now();
        generateAccounId();
        /*
        String id = customer.getId() + "" + digits3;
        accountId = Long.parseLong(id);

         */
    }

    public void generateAccounId(){
        Random random = new Random();
        int digits3 = random.nextInt(900) + 100;
        accountId = customer.getId()  + digits3;
    }
}
