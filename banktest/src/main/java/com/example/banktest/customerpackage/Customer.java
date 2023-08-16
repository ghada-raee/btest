package com.example.banktest.customerpackage;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.Random;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Customer {

        @Id
        //@GeneratedValue(generator = "customer_id")
        //@GenericGenerator(name = "customer_id", strategy = "com.example.banktest.customerpackage.CustomIdGenerator")
        @Column(name = "customer_id", nullable = false, length = 7)
        private String id;
        @Column(name = "first_name", nullable = false)
        private String firstname;

        @Column(name = "last_name", nullable = false)
        private String lastname;

        @Column(name = "civil_id", nullable = false, unique = true, length = 12)
        private String civilid;
        @Column(name = "date_of_birth", nullable = false)
        private Date dob;

        @Column(name = "address")
        private String address;

        @Column(name = "phone", nullable = false)
        private String phone;

        @Column(name = "email", nullable = false)
        private String email;

        @Column(name = "num_of_accounts", nullable = false)
        private int numOfAccounts;

        public Customer(String firstname, String lastname, String civilid, Date dob, String address, String phone, String email) {
                this.firstname = firstname;
                this.lastname = lastname;
                this.civilid = civilid;
                this.dob = dob;
                this.address = address;
                this.phone = phone;
                this.email = email;
                numOfAccounts=0;
                Random random = new Random();
                id = (1000000 + random.nextInt(9000000) ) +"";
        }
}
