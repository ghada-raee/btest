package com.example.banktest.currencypackage;

import org.springframework.http.HttpStatus;

public class CurrencyException extends Throwable{

    public CurrencyException(String message) {
        super(message);

    }
}
