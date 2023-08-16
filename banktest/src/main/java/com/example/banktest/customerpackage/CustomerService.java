package com.example.banktest.customerpackage;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@NoArgsConstructor
public class CustomerService {

    @Autowired //so we don't have to initiate
    private CustomerRepository customerRepository;

    public Customer addCustomer(CustomerRequest customer) throws CustomerException {
        String civilId = customer.getCivilid();
        if(customerRepository.existsByCivilid(civilId))
            throw new CustomerException("Customer exists");
        LocalDate dob = LocalDate.of(customer.getYear(),customer.getMonth(), customer.getDay());
        Customer c = new Customer(customer.getFirstname().trim()
                ,customer.getLastname().trim(),
                civilId.trim()
                ,dob,
                customer.getAddress().trim(),
                customer.getPhone(),
                customer.getEmail().trim());
        //c.setId("5310948");
        boolean flag = customerRepository.existsById(c.getId());
        while(flag){
            c.generateCustomerId();
            if(!customerRepository.existsById(c.getId()))
                break;
        }
        customerRepository.save(c);
        return c;
    }


    public Customer getCustomer(CustomerRequest customer) throws CustomerException {
        String civilId = customer.getCivilid();
        Customer c = customerRepository.findUserByCivilid(civilId).orElseThrow(
                () -> new CustomerException("Customer doesn't exist"));
        return c;
    }

    public Customer editCustomer(CustomerRequest customer) throws CustomerException {
        Customer c = customerRepository.findUserByCivilid(customer.getCivilid()).orElseThrow(
                () -> new CustomerException("Customer doesn't exist")); //will always exist but this is just to handle the optional

        String address = customer.getAddress();
        String email = customer.getEmail();
        String phone = customer.getPhone();
        if(address!=null && !address.isEmpty())
            c.setAddress(address.trim());
        if(email!=null && !email.isEmpty())
            c.setEmail(email.trim());
        if(phone!=null && !phone.isEmpty())
            c.setPhone(phone.trim());

        customerRepository.save(c);
        return c;
    }
}
