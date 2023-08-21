package com.example.banktest.customerpackage;

import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;

@Service
@NoArgsConstructor
public class CustomerService {

    @Autowired //so we don't have to initiate
    private CustomerRepository customerRepository;

    public CustomerResponse addCustomer(CustomerRequest customer) throws CustomerException {

        String firstname = customer.getFirstname();
        String lastname = customer.getLastname();
        String civilId = customer.getCivilid();
        int year = customer.getYear();
        int month = customer.getMonth();
        int day = customer.getDay();
        String address = customer.getAddress();
        String phone = customer.getPhone();
        String email = customer.getEmail();
        ArrayList<String> fields = new ArrayList<>(Arrays.asList(firstname,lastname,civilId,address,phone,email));
        isEmptyOrNull(fields);
        if(year<=0 || month<=0 || day<=0)
            throw new IllegalArgumentException("The numbers in date of birth must be positive numbers");
        //when trying to add a customer that already exists
        if(customerRepository.existsByCivilid(civilId))
            throw new CustomerException("Customer exists");

        LocalDate dob = LocalDate.of(customer.getYear(),customer.getMonth(), customer.getDay());
        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(dob, currentDate);
        //age resrtiction 16 & above and not more than 110 years old
        if (age.getYears() < 16 || age.getYears() > 110)
            throw new IllegalArgumentException("Age is not within legal age range");

        Customer c = new Customer(firstname.trim()
                ,lastname.trim(), civilId.trim() ,dob,
                address.trim(),phone.trim(),
                email.trim());

        boolean flag = customerRepository.existsById(c.getId());
        while(flag){
            c.generateCustomerId();
            if(!customerRepository.existsById(c.getId()))
                break;
        }
        customerRepository.save(c);
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.convert(c);
        return customerResponse;
    }


    public CustomerResponse getCustomer(CustomerRequest customer) throws CustomerException {
        String civilId = customer.getCivilid();
        isEmptyOrNull(civilId);
        Customer c = customerRepository.findUserByCivilid((civilId.trim())).orElseThrow(
                () -> new CustomerException("Customer doesn't exist")); //won't happen
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.convert(c);
        return customerResponse;

    }

    public CustomerResponse editCustomer(CustomerRequest customer) throws CustomerException {
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
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.convert(c);
        return customerResponse;

    }


    public static void isEmptyOrNull(ArrayList<String> fields) throws NullPointerException {
        for( String field : fields){
            if(field==null || field.isEmpty())
                throw new NullPointerException("Fill the the required fields please");
        }
    }
    public static void isEmptyOrNull(String field) throws NullPointerException {

            if(field==null || field.isEmpty())
                throw new NullPointerException("Fill the the required fields please");

    }
}
