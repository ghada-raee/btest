package com.example.banktest.currencypackage;

import com.example.banktest.GenericResponse;
import com.example.banktest.accountpackage.Account;
import com.example.banktest.accountpackage.AccountRequest;
import com.example.banktest.customerpackage.Customer;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.intellij.lang.annotations.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/currency")
public class CurrencyController {


    private final CurrencyService currencyService;

    Logger logger = LoggerFactory.getLogger(CurrencyController.class);

    @GetMapping("/listcurrency")
    @ResponseBody
    public ResponseEntity<Object> listingCurrency()  {
        Mono<ListCurrencyJsonRoot> result;

        try{
            result = currencyService.listCurrency();

        } catch(CurrencyException e){
            logger.error(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.OK, null);
        }
        return GenericResponse.generateResponse("Successful", HttpStatus.OK, result.block());
    }

    @GetMapping("/change")
    @ResponseBody
    public ResponseEntity<Object> change(
            @RequestParam(name = "start_date", required = true) String startDate,
            @RequestParam(name = "end_date", required = true) String endDate,
            @RequestParam(name = "currencies", required = false) String currencies,
            @RequestParam(name = "source", required = false) String source
    ) {
        if (!isValidDateFormat(startDate) || !isValidDateFormat(endDate))
            return GenericResponse.generateResponse("Date format should be YYYY-MM-DD", HttpStatus.BAD_REQUEST, null);
        Mono<ChangeJsonRoot> result;
        try{
            result = currencyService.change(startDate,endDate,currencies,source);
        } catch(CurrencyException e){
            logger.error(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.OK, null);
        }
        return GenericResponse.generateResponse("Successful", HttpStatus.OK, result.block());

    }

    @GetMapping("/historical")
    @ResponseBody
    public ResponseEntity<Object> historical(
            @RequestParam(name = "date", required = true) String date,
            @RequestParam(name = "currencies", required = false) String currencies,
            @RequestParam(name = "source", required = false) String source
    ) {
        if (!isValidDateFormat(date))
            return GenericResponse.generateResponse("Date format should be YYYY-MM-DD", HttpStatus.BAD_REQUEST, null);
        Mono<HistoricalExchangeRateRoot> result;
        try{
            result = currencyService.historical(date,currencies,source);
        } catch(CurrencyException e){
            logger.error(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.OK, null);
        }
        return GenericResponse.generateResponse("Successful", HttpStatus.OK, result.block());
    }

    @GetMapping("/live")
    @ResponseBody
    public ResponseEntity<Object> live(
            @RequestParam(name = "currencies", required = false) String currencies,
            @RequestParam(name = "source", required = false) String source
    ) {

        Mono<ExchangeRateJsonRoot> result;

        try{
            result = currencyService.live(currencies, source);

        } catch(CurrencyException e){
            logger.error(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.OK, null);
        }
        return GenericResponse.generateResponse("Successful", HttpStatus.OK, result.block());

    }


    @GetMapping("/timeframe")
    @ResponseBody
    public ResponseEntity<Object> timeframe(
            @RequestParam(name = "start_date", required = true) String startDate,
            @RequestParam(name = "end_date", required = true) String endDate,
            @RequestParam(name = "currencies", required = false) String currencies,
            @RequestParam(name = "source", required = false) String source
    ) {
        if (!isValidDateFormat(startDate) || !isValidDateFormat(endDate))
            return GenericResponse.generateResponse("Date format should be YYYY-MM-DD", HttpStatus.BAD_REQUEST, null);

        Mono<TimeFrameJson> result;
        try{
            result = currencyService.timeframe2(startDate,endDate,currencies,source);
        } catch(WebClientResponseException e){
            logger.error(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.OK, null);
        }
        return GenericResponse.generateResponse("Successful", HttpStatus.OK, result.block());

    }

    private boolean isValidDateFormat(String date) {
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }
    private boolean isValidCurrencyCode(String currencyCode) {
        try {
            Currency.getInstance(currencyCode);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }


}
