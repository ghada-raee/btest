package com.example.banktest.customerpackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    boolean existsById(String id);
    boolean existsByCivilid(String civilid);

    Optional<Customer> findUserByCivilid(String civilid);

    Optional<Customer> findUserById(String id);

}
