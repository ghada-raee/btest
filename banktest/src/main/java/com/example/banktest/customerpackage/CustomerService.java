package com.example.banktest.customerpackage;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class CustomerService {

    @Autowired //so we don't have to initiate
    private CustomerRepository customerRepository;

    public Customer add(CustomerRequest customer){
        Customer c = new Customer(customer.getFirstname(),customer.getLastname(),
                customer.getCivilid(),customer.getDob(), customer.getAddress(), customer.getPhone(),
                customer.getEmail());
        customerRepository.save(c);
        return c;
    }

}
