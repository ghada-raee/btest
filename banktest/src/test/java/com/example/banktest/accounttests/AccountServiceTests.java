package com.example.banktest.accounttests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.example.banktest.accountpackage.*;
import com.example.banktest.currencypackage.ConvertJsonRoot;
import com.example.banktest.currencypackage.CurrencyService;
import com.example.banktest.customerpackage.*;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTests {
    @Mock
    CustomerRepository customerRepository;
    @Mock
    AccountRepository accountRepository;

    @Mock
    TransactionRepository transactionRepository;
    @InjectMocks
    AccountService accountService;

    @Mock
    CurrencyService currencyService;


    private Validator validator;

    Customer customer;
    Account account;
    AccountRequest accountRequest;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        //MockitoAnnotations.initMocks(this);
        customer = new Customer(
                "John",
                "Doe",
                "123456789012",
                LocalDate.of(1990, 5, 15),
                "123 Main St",
                "12345678",
                "john.doe@example.com"
        );
        customer.setNumOfAccounts(1);
        account = new Account(customer, AccountType.SAVING, Currency.getInstance("KWD"));
        account.setBalance(6.0);//KWD
        accountRequest = new AccountRequest(account.getAccountId(), customer.getId(), 3.0, 123456,
                "KWD", AccountType.SALARY, "KWD");
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        //Not sure how can I hide them here;
    }
    @Test
    public void createAccount_OneSalaryAccountAndMainScenario_ReturnsAccountResponse() throws CustomerException, AccountException {
        when(customerRepository.findUserById(customer.getId())).thenReturn(Optional.of(customer));
        when(accountRepository.existsByCustomerAndAccountType(customer,accountRequest.getAccountType())).thenReturn(false);
        when(accountRepository.existsByAccountId(accountRequest.getAccountId())).thenReturn(false);
        AccountResponse response = accountService.createAccount(accountRequest);
        assertNotNull(response.getAccountId());

    }
    @Test
    public void createAccount_TwoSalaryAccount_ReturnsException() throws CustomerException, AccountException {
        when(customerRepository.findUserById(customer.getId())).thenReturn(Optional.of(customer));
        when(accountRepository.existsByCustomerAndAccountType(customer,accountRequest.getAccountType())).thenReturn(true);
        assertThrows(AccountException.class, () -> accountService.createAccount(accountRequest));
    }
    @Test
    public void createAccount_NullFields_ReturnsException() throws CustomerException, AccountException {
        accountRequest.setAccountType(null);
        assertThrows(NullPointerException.class, () -> accountService.createAccount(accountRequest));
    }
    @Test
    public void createAccount_MoreThanTenAccs_ReturnsException() throws CustomerException, AccountException {
        customer.setNumOfAccounts(10);
        when(customerRepository.findUserById(customer.getId())).thenReturn(Optional.of(customer));
        when(accountRepository.existsByCustomerAndAccountType(customer,accountRequest.getAccountType())).thenReturn(false);
        assertThrows(AccountException.class, () -> accountService.createAccount(accountRequest));
    }
    @Test
    public void createAccount_CustomerDoesntexist_ReturnsException() throws CustomerException, AccountException {
        when(customerRepository.findUserById(customer.getId())).thenReturn(Optional.empty());
        assertThrows(CustomerException.class, () -> accountService.createAccount(accountRequest));
    }

    @Test
    public void listAccounts_ReturnsAccounts() throws CustomerException, AccountException {
        List<Account> accountsList = new ArrayList<>();
        accountsList.add(account);
        when(customerRepository.findUserById(customer.getId())).thenReturn(Optional.of(customer));
        when(accountRepository.findByCustomer(customer)).thenReturn(accountsList);
        List<AccountResponse> responses = accountService.listAccounts(accountRequest);
        assertNotNull(responses);
    }
    @Test
    public void listAccounts_CustomerDoesntexist_ReturnsException() throws CustomerException, AccountException {
        when(customerRepository.findUserById(customer.getId())).thenReturn(Optional.empty());
        assertThrows(CustomerException.class, () -> accountService.listAccounts(accountRequest));
    }
    @Test
    public void listAccounts_ZeroAccounts_ReturnsException() throws CustomerException, AccountException {
        customer.setNumOfAccounts(0);
        when(customerRepository.findUserById(customer.getId())).thenReturn(Optional.of(customer));
        assertThrows(AccountException.class, () -> accountService.listAccounts(accountRequest));
    }
    @Test
    public void getAccount_ReturnsAccount() throws AccountException {
        when(accountRepository.findAccountByAccountId(accountRequest.getAccountId())).thenReturn(Optional.of(account));//will always exist
        AccountResponse response = accountService.getAccount(accountRequest);
        assertNotNull(response.getAccountId());
    }
    @Test
    public void updateBalance_ReturnsTransactionResponse() throws AccountException {
        when(accountRepository.findAccountByAccountId(accountRequest.getAccountId())).thenReturn(Optional.of(account));//will always exist
        TransactionResponse response = accountService.updateBalance(accountRequest);
        assertNotNull(response.getTransactionId());
        assertEquals(9.0, response.getBalance());
    }

    @Test
    public void updateBalance_DeactivatedAccount_ReturnsException() throws AccountException {
        account.set_active(false);
        when(accountRepository.findAccountByAccountId(accountRequest.getAccountId())).thenReturn(Optional.of(account));//will always exist
        assertThrows(AccountException.class, () -> accountService.updateBalance(accountRequest));
    }

    @Test
    public void updateBalance_NullField_ReturnsException() throws AccountException {
        accountRequest.setTransactionCurrency(null);
        when(accountRepository.findAccountByAccountId(accountRequest.getAccountId())).thenReturn(Optional.of(account));//will always exist
        assertThrows(NullPointerException.class, () -> accountService.updateBalance(accountRequest));
    }
    @Test
    public void updateBalance_InvalidCurrency_ReturnsException() throws AccountException {
        accountRequest.setTransactionCurrency("KD");
        when(accountRepository.findAccountByAccountId(accountRequest.getAccountId())).thenReturn(Optional.of(account));//will always exist
        assertThrows(IllegalArgumentException.class, () -> accountService.updateBalance(accountRequest));
    }

    @Test
    public void updateBalance_ExceedsLimit_ReturnsException() throws AccountException {
        accountRequest.setAmount(10000);//KWD
        when(accountRepository.findAccountByAccountId(accountRequest.getAccountId())).thenReturn(Optional.of(account));//will always exist
        assertThrows(AccountException.class, () -> accountService.updateBalance(accountRequest));
    }
    @Test
    public void updateBalance_BelowBalance_ReturnsException() throws AccountException {
        accountRequest.setAmount(-10.0);//KWD
        when(accountRepository.findAccountByAccountId(accountRequest.getAccountId())).thenReturn(Optional.of(account));//will always exist
        assertThrows(AccountException.class, () -> accountService.updateBalance(accountRequest));
    }

    //Not sure what's the issue, it works perfectly in postman and currencyService.convert was used in updateBalance and it worked
    //with no issues. No clue why it's resulting in null here
    @Test
    public void changeCurrency_ReturnsMonoConvertJsonRoot() throws AccountException {
        when(accountRepository.findAccountByAccountId(accountRequest.getAccountId())).thenReturn(Optional.of(account));//will always exist
        Mono<ConvertJsonRoot> response = accountService.changeCurrency(accountRequest,"USD","2023-08-20");
        System.out.println("//////// "+response.block().getResult());
        assertNotNull(response.block().getResult());
    }

    //same test for deactivate
    @Test
    public void activateAccount_Success() throws AccountException {
        when(accountRepository.findAccountByAccountId(accountRequest.getAccountId())).thenReturn(Optional.of(account));
        accountService.activateAccount(accountRequest);
        // Verify that accountRepository.save was called with the updated account
        verify(accountRepository, times(1)).save(account);
    }

}
