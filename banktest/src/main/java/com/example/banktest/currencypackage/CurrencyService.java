package com.example.banktest.currencypackage;

import com.example.banktest.GenericResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Currency;

@Service
@NoArgsConstructor
public class CurrencyService {


    @Autowired
    private Environment environment;



    //
    public <T> Mono<T> makeRequest(String uri, Class<T> responseClass) {
        WebClient webClient = WebClient.builder().baseUrl(environment.getProperty("currency.api.baseUrl")).build();
        String apiKey = environment.getProperty("currency.api.key");
        return webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .header("apikey", apiKey)
                .retrieve()
                .onStatus(
                        status -> status.is5xxServerError(),
                        response -> {
                            HttpStatusCode httpStatus = response.statusCode();
                            return Mono.error(new CurrencyException("Server Error: " + httpStatus.value()));
                        }
                )
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> {
                            HttpStatusCode httpStatus = response.statusCode();
                            return Mono.error(new CurrencyException("Client Error: " + httpStatus.value()));
                        }
                )
                .bodyToMono(responseClass);
    }


    public Mono<ListCurrencyJsonRoot>  listCurrency() {
        return makeRequest("/list",ListCurrencyJsonRoot.class);
    }
    public Mono<ChangeJsonRoot> change(String startDate, String endDate, String currencies, String source) {
        if (!isValidDateFormat(startDate) || !isValidDateFormat(endDate))
            throw new IllegalArgumentException("\"Date format should be YYYY-MM-DD\"");
        isValidCurrencyCode(source);
        String url = "/change?start_date="+startDate+"&end_date="+endDate;
        if (currencies != null && !currencies.isEmpty())
            url = url + "&currencies="+ currencies;

        if (source != null && !source.isEmpty())
            url = url + "&source="+ source;
        Mono<ChangeJsonRoot> result= makeRequest(url,ChangeJsonRoot.class);
        if(!result.block().isSuccess())
            throw new IllegalArgumentException("Something went wrong. Check inputs format or try again");
        return result;
    }
    public Mono<HistoricalExchangeRateRoot> historical(String date, String currencies, String source){

        if (!isValidDateFormat(date))
            throw new IllegalArgumentException("Date format should be YYYY-MM-DD");
        String url = "/historical?date="+date;
        if (currencies != null && !currencies.isEmpty())
            url = url + "&currencies="+ currencies;

        if (source != null && !source.isEmpty())
            url = url + "&source="+ source;
        Mono<HistoricalExchangeRateRoot> result= makeRequest(url,HistoricalExchangeRateRoot.class);
        if(!result.block().isSuccess())
            throw new IllegalArgumentException("Something went wrong. Check inputs format or try again");
        return result;
    }

    public Mono<ExchangeRateJsonRoot> live(String currencies, String source) {
        String url2 = "/live?";
        boolean useUrl2 = false;
        if (currencies != null && !currencies.isEmpty()) {
            url2 = url2 + "&currencies=" + currencies;
            useUrl2 = true;
        }
        if (source != null && !source.isEmpty()) {
            url2 = url2 + "&source=" + source;
            useUrl2 = true;
        }
        Mono<ExchangeRateJsonRoot> result;
        if(useUrl2)
            result= makeRequest(url2,ExchangeRateJsonRoot.class);

        else
            result= makeRequest("/live",ExchangeRateJsonRoot.class);
        if(!result.block().isSuccess())
            throw new IllegalArgumentException("Something went wrong. Check inputs format or try again");
        return result;

    }
    public Mono<TimeFrameJson> timeframe(String startDate, String endDate, String currencies, String source) {
        if (!isValidDateFormat(startDate) || !isValidDateFormat(endDate))
            throw new IllegalArgumentException("Date format should be YYYY-MM-DD");
        String url = "/timeframe?start_date="+startDate+"&end_date="+endDate;
        if (currencies != null && !currencies.isEmpty())
            url = url + "&currencies="+ currencies;

        if (source != null && !source.isEmpty())
            url = url + "&source="+ source;
        Mono<TimeFrameJson> result= makeRequest(url,TimeFrameJson.class);
        if(!result.block().isSuccess())
            throw new IllegalArgumentException("Something went wrong. Check inputs format or try again");
        return result;
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
