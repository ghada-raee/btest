package com.example.banktest.accountpackage;

import com.example.banktest.currencypackage.ConvertJsonRoot;
import com.example.banktest.currencypackage.CurrencyService;
import com.example.banktest.customerpackage.Customer;
import com.example.banktest.customerpackage.CustomerException;
import com.example.banktest.customerpackage.CustomerRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
    CurrencyService currencyService;
    public Account createAccount(AccountRequest account) throws CustomerException, AccountException {
        String cid = account.getCustomer_id();
        Customer customer = customerRepository.findUserById(cid).orElseThrow(
                () -> new CustomerException("Customer doesn't exist"));
        if(account.getAccountType() == AccountType.SALARY &&
                accountRepository.existsByCustomerAndAccountType(customer,account.getAccountType()))
            throw new AccountException("You cannot have more than 1 salary account");
        int numOfAcc = customer.getNumOfAccounts()+1;
        if(numOfAcc>10)
            throw new AccountException("You cannot have more than 10 accounts");
        Currency cu = Currency.getInstance(account.getCurrency());
        Account account1 = new Account(customer,account.getAccountType(),cu);
        customer.setNumOfAccounts(numOfAcc);
        boolean flag = accountRepository.existsByAccountId(account.getAccountId());
        while(flag){
            account1.generateAccounId();
            if(!accountRepository.existsByAccountId(account.getAccountId()))
                break;
        }
        accountRepository.save(account1);
        customerRepository.save(customer);
        return account1;


    }

    public List<Account> listAccounts(AccountRequest account) throws CustomerException, AccountException {
        String cid = account.getCustomer_id();
        Customer customer = customerRepository.findUserById(cid).orElseThrow(
                () -> new CustomerException("Customer doesn't exist"));
        if(customer.getNumOfAccounts()==0)
            throw new AccountException("you don't have any account");
        return accountRepository.findByCustomer(customer);

    }

    public Account getAccount(AccountRequest account) {
        Account acc = accountRepository.findAccountByAccountId(account.getAccountId()).orElse(null);
        return acc;
    }

    public Double updateBalance(AccountRequest account) throws AccountException {
        Account account1 = accountRepository.findAccountByAccountId(account.getAccountId()).orElse(null);
        if(!account1.is_active())
            throw new AccountException("You cannot update the balance since your account is deactivated");
       double amount = account1.getBalance() + account.getAmount();
       if(amount<0)
           throw new AccountException("You cannot deduct this amount of money");
       else
           account1.setBalance(amount);
        accountRepository.save(account1);
        return account1.getBalance();
    }

    public Mono<ConvertJsonRoot> changeCurrency(AccountRequest account, String to, String date) {

        Account account1 = accountRepository.findAccountByAccountId(account.getAccountId()).orElse(null);
        String url = "/convert?to="+to+"&from="+account1.getCurrency().getCurrencyCode()+"&amount="+account1.getBalance();

        if(date != null && !date.isEmpty())
            url = url + "&date="+date;
        Mono<ConvertJsonRoot> result = currencyService.makeRequest2(url,ConvertJsonRoot.class);
        Currency c = Currency.getInstance(to);
        double balance = result.block().getResult();

        account1.setCurrency(c);
        account1.setBalance(balance);
        accountRepository.save(account1);
        return result;
    }

    public Account activateAccount(AccountRequest account) {
        Account account1 = accountRepository.findAccountByAccountId(account.getAccountId()).orElse(null);
        account1.set_active(true);
        accountRepository.save(account1);
        return account1;

    }
    public Account deactivateAccount(AccountRequest account) {
        Account account1 = accountRepository.findAccountByAccountId(account.getAccountId()).orElse(null);
        account1.set_active(false);
        accountRepository.save(account1);
        return account1;

    }

}
