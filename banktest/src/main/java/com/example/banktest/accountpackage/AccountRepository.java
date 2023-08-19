package com.example.banktest.accountpackage;

import com.example.banktest.customerpackage.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findAccountByAccountId(String aid);

    boolean existsByCustomerAndAccountType(Customer customer, AccountType accountType);

    boolean existsByAccountId(String accountid);

    List<AccountResponse> findByCustomer(Customer customer);
}
