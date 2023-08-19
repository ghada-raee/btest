package com.example.banktest.customerpackage;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.Date;
import java.util.Random;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Customer {

        @Id
        @Column(name = "customer_id", nullable = false, length = 7)
        private String id;
        @Column(name = "first_name", nullable = false)

        private String firstname;


        @Column(name = "last_name", nullable = false)
        private String lastname;

        @Column(name = "civil_id", nullable = false, unique = true, length = 12)
        private String civilid;
        @Column(name = "date_of_birth", nullable = false)
        private LocalDate dob;

        @Column(name = "address", nullable = false)
        private String address;

        @Column(name = "phone", nullable = false, length = 8)
        private String phone;

        @Column(name = "email", nullable = false)
        private String email;

        @Column(name = "num_of_accounts", nullable = false)
        private int numOfAccounts;

        @Column(name = "date_joined", nullable = false)
        private LocalDate datejoined;

        public Customer(String firstname, String lastname, String civilid, LocalDate dob,
                        String address, String phone, String email) {
                this.firstname = firstname;
                this.lastname = lastname;
                this.civilid = civilid;
                this.dob = dob;
                this.address = address;
                this.phone = phone;
                this.email = email;
                numOfAccounts=0;
                datejoined = LocalDate.now();
                generateCustomerId();
        }

        public void generateCustomerId(){
                Random random = new Random();
                id = (1000000 + random.nextInt(9000000) ) +"";
        }
}
