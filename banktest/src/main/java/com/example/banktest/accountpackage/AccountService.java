package com.example.banktest.accountpackage;

import com.example.banktest.currencypackage.ConvertJsonRoot;
import com.example.banktest.currencypackage.CurrencyService;
import com.example.banktest.customerpackage.Customer;
import com.example.banktest.customerpackage.CustomerException;
import com.example.banktest.customerpackage.CustomerRepository;
import com.example.banktest.customerpackage.CustomerService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

@Service
@NoArgsConstructor
public class AccountService {

    @Autowired //so we don't have to initiate
    private CustomerRepository customerRepository;

    @Autowired //so we don't have to initiate
    private AccountRepository accountRepository;

    @Autowired
    CustomerService customerService;

    @Autowired
    CurrencyService currencyService;
    public AccountResponse createAccount(AccountRequest account) throws CustomerException, AccountException {
        AccountType accountType =account.getAccountType();
        if(accountType == null)
            throw new NullPointerException("Account type cannot be empty");
        customerService.isEmptyOrNull(account.getCurrency());

        String cid = account.getCustomer_id().trim();
        Customer customer = customerRepository.findUserById(cid).orElseThrow(
                () -> new CustomerException("Customer doesn't exist"));

        if(account.getAccountType() == AccountType.SALARY &&
                accountRepository.existsByCustomerAndAccountType(customer,account.getAccountType()))
            throw new AccountException("You cannot have more than 1 salary account");

        int numOfAcc = customer.getNumOfAccounts()+1;
        if(numOfAcc>10)
            throw new AccountException("You cannot have more than 10 accounts");

        Currency cu = Currency.getInstance(account.getCurrency().trim());
        Account account1 = new Account(customer,accountType,cu);
        customer.setNumOfAccounts(numOfAcc);
        boolean flag = accountRepository.existsByAccountId(account.getAccountId());
        while(flag){
            account1.generateAccounId();
            if(!accountRepository.existsByAccountId(account.getAccountId()))
                break;
        }
        accountRepository.save(account1);
        customerRepository.save(customer);
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.convert(account1);
        return accountResponse;

    }

    public List<AccountResponse> listAccounts(AccountRequest account) throws CustomerException, AccountException {
        String cid = account.getCustomer_id();
        Customer customer = customerRepository.findUserById(cid).orElseThrow(
                () -> new CustomerException("Customer doesn't exist"));
        if(customer.getNumOfAccounts()==0)
            throw new AccountException("you don't have any account");
        return accountRepository.findByCustomer(customer);

    }

    public AccountResponse getAccount(AccountRequest account) throws AccountException {
        Account acc = accountRepository.findAccountByAccountId(account.getAccountId()).orElseThrow(
                () -> new AccountException("Account doesn't exist"));
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.convert(acc);
        return accountResponse;
    }

    public AccountResponse updateBalance(AccountRequest account) throws AccountException {
        Account account1 = accountRepository.findAccountByAccountId(account.getAccountId()).orElseThrow(
                () -> new AccountException("Account doesn't exist"));
        if(!account1.is_active())
            throw new AccountException("You cannot update the balance since your account is deactivated");
       double amount = account1.getBalance() + account.getAmount();
       if(amount<0)
           throw new AccountException("You cannot deduct this amount of money");
       else
           account1.setBalance(amount);
        accountRepository.save(account1);
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.convert(account1);
        return accountResponse;
    }

    public Mono<ConvertJsonRoot> changeCurrency(AccountRequest account, String to, String date) throws AccountException {

        Account account1 = accountRepository.findAccountByAccountId(account.getAccountId()).orElseThrow(
                () -> new AccountException("Account doesn't exist"));
        String url = "/convert?to="+to+"&from="+account1.getCurrency().getCurrencyCode()+"&amount="+account1.getBalance();

        if(date != null && !date.isEmpty())
            url = url + "&date="+date;
        Mono<ConvertJsonRoot> result = currencyService.makeRequest(url,ConvertJsonRoot.class);
        Currency c = Currency.getInstance(to);
        double balance = result.block().getResult();

        account1.setCurrency(c);
        account1.setBalance(balance);
        accountRepository.save(account1);
        return result;
    }

    public void activateAccount(AccountRequest account) throws AccountException {
        Account account1 = accountRepository.findAccountByAccountId(account.getAccountId()).orElseThrow(
                () -> new AccountException("Account doesn't exist"));
        account1.set_active(true);
        accountRepository.save(account1);
    }
    public void deactivateAccount(AccountRequest account) throws AccountException {
        Account account1 = accountRepository.findAccountByAccountId(account.getAccountId()).orElseThrow(
                () -> new AccountException("Account doesn't exist"));
        account1.set_active(false);
        accountRepository.save(account1);

    }

}
