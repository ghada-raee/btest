package com.example.banktest.customerpackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    /*
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Customer c WHERE c.id = ?1")
    boolean existsById(String Id);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Customer c WHERE c.civilId = ?1")
    boolean existsByCivilId(String civilId);

     */
    boolean existsById(String id);
    boolean existsByCivilid(String civilid);

    Optional<Customer> findUserByCivilid(String civilid);

}
