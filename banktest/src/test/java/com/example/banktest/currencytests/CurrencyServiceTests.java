package com.example.banktest.currencytests;

import com.example.banktest.accountpackage.Account;
import com.example.banktest.accountpackage.AccountRequest;
import com.example.banktest.accountpackage.AccountResponse;
import com.example.banktest.accountpackage.AccountType;
import com.example.banktest.currencypackage.*;
import com.example.banktest.customerpackage.Customer;
import com.example.banktest.customerpackage.CustomerException;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTests {

    @Mock
    private Environment environment;

    @InjectMocks
    private CurrencyService currencyService;

    String start_date,end_date,date,currencies,source;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
                                                        //Not sure how can I hide them here
        when(environment.getProperty("currency.api.baseUrl")).thenReturn("https://api.apilayer.com/currency_data");
        when(environment.getProperty("currency.api.key")).thenReturn("CGkHPEOzeayv5nEq44HZdPXV6bjRvK9s");
        start_date = "2023-08-17";
        end_date = "2023-08-18";
        date = "2023-08-17";
        source= "USD";
        currencies = "EUR,KWD";
    }
    @Test
    public void testListCurrency_Success() {
        Mono<ListCurrencyJsonRoot> result = currencyService.listCurrency();
        assertTrue(result.block().isSuccess());

    }
    @Test
    public void change_Success() {
        Mono<ChangeJsonRoot> result = currencyService.change(start_date,end_date,currencies,source);
        assertTrue(result.block().isSuccess());
    }
    @Test
    public void change_SomethingWentWrong() {
        source ="US";
        assertThrows(IllegalArgumentException.class, () ->currencyService.change(start_date,end_date,currencies,source));
    }
    @Test
    public void historical_Success() {
        Mono<HistoricalExchangeRateRoot> result = currencyService.historical(date,currencies,source);
        assertTrue(result.block().isSuccess());
    }
    @Test
    public void historical_SomethingWentWrong() {
        source ="US";
        assertThrows(IllegalArgumentException.class, () ->currencyService.historical(date,currencies,source));
    }
    @Test
    public void live_Success() {
        Mono<ExchangeRateJsonRoot> result = currencyService.live(currencies,source);
        assertTrue(result.block().isSuccess());
    }
    @Test
    public void live_SomethingWentWrong() {
        source ="US";
        assertThrows(IllegalArgumentException.class, () ->currencyService.live(currencies,source));
    }
    @Test
    public void timeframe_Success() {
        Mono<TimeFrameJson> result = currencyService.timeframe(start_date,end_date,currencies,source);
        assertTrue(result.block().isSuccess());
    }

    @Test
    public void timeframe_SomethingWentWrong() {
        source ="US";
        assertThrows(IllegalArgumentException.class, () ->currencyService.timeframe(start_date,end_date,currencies,source));
    }
    @Test
    public void convert_Success() {
        Mono<ConvertJsonRoot> result = currencyService.convert("USD","KWD","10",date);
        assertTrue(result.block().isSuccess());
    }
    @Test
    public void convert_SomethingWentWrong() {
        assertThrows(IllegalArgumentException.class, () ->currencyService.convert("US","KWD","10",date));
    }
}
