package com.example.banktest.accountpackage;

import com.example.banktest.GenericResponse;
import com.example.banktest.customerpackage.Customer;
import com.example.banktest.customerpackage.CustomerException;
import com.example.banktest.customerpackage.CustomerRequest;
import com.example.banktest.customerpackage.CustomerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/account")
public class AccountController {
    private final AccountService accountService;

    Logger logger = LoggerFactory.getLogger(Account.class);

    //create account
    @PostMapping("/createaccount")
    @ResponseBody
    public ResponseEntity<Object> createAccount(
            @RequestBody AccountRequest account
    ) {

        Account acc;
        try {
            acc = accountService.createAccount(account);
            logger.info("Account added");
        } catch (AccountException | CustomerException e) {
            logger.warn(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.OK, null);
        }
        return GenericResponse.generateResponse(
                "Successfully created account: {user}".replace("{user}", acc.getAccountId()),
                HttpStatus.OK,
                "success"
        );
    }

    //list accounts
    @GetMapping("/listaccounts")
    @ResponseBody
    public ResponseEntity<Object> listAccounts(
            @RequestBody AccountRequest account
    ) {

        List<Account> accs;
        try {
            accs = accountService.listAccounts(account);
            logger.info("Accounts listed");
        } catch (AccountException | CustomerException e) {
            logger.warn(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.OK, null);
        }
        return GenericResponse.generateResponse(
                "Successfully listed accounts",
                HttpStatus.OK,
                accs
        );
    }
    //account details
    @GetMapping("/getaccount")
    @ResponseBody
    public ResponseEntity<Object> getaccount(
            @RequestBody AccountRequest account
    ) {

        Account acc;
        //try {
            acc = accountService.getAccount(account);

       /* } catch (AccountException | CustomerException e) {
            logger.warn(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.OK, null);
        }*/
        return GenericResponse.generateResponse(
                "Successfully retrieved account",
                HttpStatus.OK,
                acc
        );
    }

    //update balance
    @PutMapping("/updatebalance")
    @ResponseBody
    public ResponseEntity<Object> updateBalance(
            @RequestBody AccountRequest account
    ) {

        Double balance;
        try {
            balance = accountService.updateBalance(account);
            logger.info("Balance changed");
        } catch (AccountException e) {
            logger.warn(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.OK, null);
        }
        return GenericResponse.generateResponse(
                "New Balance",
                HttpStatus.OK,
                balance
        );
    }

    //update currency
    //here it should change the amount of money too
    @PutMapping("/changeCurrency")
    @ResponseBody
    public ResponseEntity<Object> changeCurrency(
            @RequestBody AccountRequest account
    ) {

        Account acc;
            acc = accountService.changeCurrency(account);
            logger.info("Currency changed to "+acc.getCurrency().getCurrencyCode());

        return GenericResponse.generateResponse(
                "New Currency",
                HttpStatus.OK,
                acc
        );
    }

    //activate account
    @PutMapping("/activateAccount")
    @ResponseBody
    public ResponseEntity<Object> activateAccount(
            @RequestBody AccountRequest account
    ) {

        Account acc;
        acc = accountService.activateAccount(account);
        logger.info("Account activated");

        return GenericResponse.generateResponse(
                "Account activated",
                HttpStatus.OK,
                null
        );
    }
    @PutMapping("/deactivateAccount")
    @ResponseBody
    public ResponseEntity<Object> deactivateAccount(
            @RequestBody AccountRequest account
    ) {

        Account acc;
        acc = accountService.deactivateAccount(account);
        logger.info("Account deactivated");

        return GenericResponse.generateResponse(
                "Account deactivated",
                HttpStatus.OK,
                null
        );
    }

    //deactivate aacount
}
