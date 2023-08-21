package com.example.banktest.currencytests;

import com.example.banktest.currencypackage.*;
import com.example.banktest.customerpackage.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = CurrencyController.class)
public class CurrencyControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    @Autowired
    private ObjectMapper objectMapper;

    String start_date,end_date,date,currencies,source;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        //Not sure how can I hide them here
        //when(environment.getProperty("currency.api.baseUrl")).thenReturn("https://api.apilayer.com/currency_data");
       // when(environment.getProperty("currency.api.key")).thenReturn("CGkHPEOzeayv5nEq44HZdPXV6bjRvK9s");
        start_date = "2023-08-17";
        end_date = "2023-08-18";
        date = "2023-08-17";
        source= "USD";
        currencies = "EUR,KWD";
    }
    @Test
    public void listcurrency_Success() throws CustomerException, Exception {
        Map<String, String> currencies = Map.of(
                "USD", "United States Dollar",
                "EUR", "Euro",
                "JPY", "Japanese Yen"
        );
        ListCurrencyJsonRoot listCurrencyJsonRoot = new ListCurrencyJsonRoot(currencies, true);
        Mono<ListCurrencyJsonRoot> monoListCurrencyJsonRoot = Mono.just(listCurrencyJsonRoot);

        given(currencyService.listCurrency()).willReturn(monoListCurrencyJsonRoot);
        ResultActions response = mockMvc.perform(get("/api/v1/currency/listcurrency")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString("")));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successful"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(monoListCurrencyJsonRoot.block()));
    }
    @Test
    public void change_Success() throws Exception {
        ChangeJsonRoot.Quotes quotes = new ChangeJsonRoot.Quotes(-0.1726, -13.4735, 1.108609, 1.281236);
        Map<String, ChangeJsonRoot.Quotes> quotesMap = Map.of("USDAUD", quotes);
        ChangeJsonRoot changeJsonRoot = new ChangeJsonRoot(true, end_date, quotesMap, source, start_date, true);
        Mono<ChangeJsonRoot> monoChangeJsonRoot = Mono.just(changeJsonRoot);
        given(currencyService.change(start_date,end_date,currencies,source)).willReturn(monoChangeJsonRoot);
        ResultActions response = mockMvc.perform(get("/api/v1/currency/change")
                .param("start_date", start_date)
                .param("end_date", end_date)
                .param("currencies", currencies)
                .param("source", source)
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successful"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(monoChangeJsonRoot.block()));
    }
    @Test
    public void change_BadRequest() throws Exception {
        given(currencyService.change(start_date,end_date,currencies,source)).willThrow(new IllegalArgumentException("Date format should be YYYY-MM-DD"));
        ResultActions response = mockMvc.perform(get("/api/v1/currency/change")
                .param("start_date", start_date)
                .param("end_date", end_date)
                .param("currencies", currencies)
                .param("source", source)
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Date format should be YYYY-MM-DD"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());
    }

    @Test
    public void historical_Success() throws Exception {
        Map<String, Double> quotes = new HashMap<>();
        quotes.put("USDAUD", 1.278342);
        quotes.put("USDEUR", 1.278342);
        quotes.put("USDGBP", 0.908019);
        quotes.put("USDPLN", 3.731504);
        HistoricalExchangeRateRoot exchangeRate = new HistoricalExchangeRateRoot(
                quotes, source, true, 1432400348, date, true);
        Mono<HistoricalExchangeRateRoot> monoHistoricalExchangeRateRoot = Mono.just(exchangeRate);
        given(currencyService.historical(date,currencies,source)).willReturn(monoHistoricalExchangeRateRoot);
        ResultActions response = mockMvc.perform(get("/api/v1/currency/historical")
                .param("date", date)
                .param("currencies", currencies)
                .param("source", source)
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successful"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(monoHistoricalExchangeRateRoot.block()));
    }
    @Test
    public void historical_BadRequest() throws Exception {
        given(currencyService.historical(date,currencies,source))
                .willThrow(new IllegalArgumentException("Date format should be YYYY-MM-DD"));
        ResultActions response = mockMvc.perform(get("/api/v1/currency/historical")
                .param("date", date)
                .param("currencies", currencies)
                .param("source", source)
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Date format should be YYYY-MM-DD"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());
    }

    @Test
    public void live_Success() throws Exception {
        Map<String, Double> quotes = new HashMap<>();
        quotes.put("USDAUD", 1.278342);
        quotes.put("USDEUR", 1.278342);
        quotes.put("USDGBP", 0.908019);
        quotes.put("USDPLN", 3.731504);
        ExchangeRateJsonRoot exchangeRate = new ExchangeRateJsonRoot(
                quotes, source, true, 1432400348);
        Mono<ExchangeRateJsonRoot> monoExchangeRateJsonRoot = Mono.just(exchangeRate);
        given(currencyService.live(currencies,source)).willReturn(monoExchangeRateJsonRoot);
        ResultActions response = mockMvc.perform(get("/api/v1/currency/live")
                .param("currencies", currencies)
                .param("source", source)
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successful"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(monoExchangeRateJsonRoot.block()));
    }
    @Test
    public void live_BadRequest() throws Exception {
        given(currencyService.live(currencies,source)).willThrow(new IllegalArgumentException("Something went wrong. Check inputs format or try again"));
        ResultActions response = mockMvc.perform(get("/api/v1/currency/live")
                .param("currencies", currencies)
                .param("source", source)
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Something went wrong. Check inputs format or try again"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());
    }

    @Test
    public void timeframe_Success() throws Exception {
        Map<String, Double> quotes1 = new HashMap<>();
        quotes1.put("USDEUR", 0.738541);
        quotes1.put("USDGBP", 0.668525);
        quotes1.put("USDUSD", 1.0);

        Map<String, Double> quotes2 = new HashMap<>();
        quotes2.put("USDEUR", 0.736145);
        quotes2.put("USDGBP", 0.668827);
        quotes2.put("USDUSD", 1.0);
        Map<String, Map<String, Double>> quotesMap = new HashMap<>();
        quotesMap.put("2010-03-01", quotes1);
        quotesMap.put("2010-03-02", quotes2);
        TimeFrameJson timeFrameJson = new TimeFrameJson(end_date,quotesMap,start_date
        ,true,source,true);
        Mono<TimeFrameJson> monoTimeFrameJson = Mono.just(timeFrameJson);
        given(currencyService.timeframe(start_date,end_date,currencies,source)).willReturn(monoTimeFrameJson);
        ResultActions response = mockMvc.perform(get("/api/v1/currency/timeframe")
                .param("start_date", start_date)
                .param("end_date", end_date)
                .param("currencies", currencies)
                .param("source", source)
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successful"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(monoTimeFrameJson.block()));
    }
    @Test
    public void timeframe_BadRequest() throws Exception {
        given(currencyService.timeframe(start_date,end_date,currencies,source)).willThrow(new IllegalArgumentException("Date format should be YYYY-MM-DD"));
        ResultActions response = mockMvc.perform(get("/api/v1/currency/timeframe")
                .param("start_date", start_date)
                .param("end_date", end_date)
                .param("currencies", currencies)
                .param("source", source)
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Date format should be YYYY-MM-DD"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());
    }


}

