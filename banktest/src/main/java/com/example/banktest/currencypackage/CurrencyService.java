package com.example.banktest.currencypackage;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@NoArgsConstructor
public class CurrencyService {



    public static <T> Mono<T> makeRequest(String uri, Class<T> responseClass) throws CurrencyException {
        WebClient webClient = WebClient.builder().baseUrl("https://api.apilayer.com/currency_data").build();
        String apiKey = "CGkHPEOzeayv5nEq44HZdPXV6bjRvK9s";
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
        /*
                HttpStatus status = ex.getStatusCode();
        String errorMessage = ex.getResponseBodyAsString();
         */
    }

    public <T> Mono<T> makeRequest2(String uri, Class<T> responseClass) {
        WebClient webClient = WebClient.builder().baseUrl("https://api.apilayer.com/currency_data").build();
        String apiKey = "CGkHPEOzeayv5nEq44HZdPXV6bjRvK9s";
        return webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .header("apikey", apiKey)
                .retrieve()
                .onStatus(
                        status -> status.is5xxServerError(),
                        response -> {
                            HttpStatusCode httpStatus = response.statusCode();
                            return Mono.error( new CurrencyException("Server Error: " + httpStatus.value()));
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
        /*
                HttpStatus status = ex.getStatusCode();
        String errorMessage = ex.getResponseBodyAsString();
         */
    }

    public Mono<ListCurrencyJsonRoot>  listCurrency() throws CurrencyException {
        return makeRequest("/list",ListCurrencyJsonRoot.class);
    }
    public Mono<ChangeJsonRoot> change(String startDate, String endDate, String currencies, String source) throws CurrencyException {

        String url = "/change?start_date="+startDate+"&end_date="+endDate;
        if (currencies != null && !currencies.isEmpty())
            url = url + "&currencies="+ currencies;

        if (source != null && !source.isEmpty())
            url = url + "&source="+ source;
        return makeRequest(url,ChangeJsonRoot.class);
    }
    public Mono<HistoricalExchangeRateRoot> historical(String date, String currencies, String source) throws CurrencyException {
        String url = "/historical?date="+date;
        if (currencies != null && !currencies.isEmpty())
            url = url + "&currencies="+ currencies;

        if (source != null && !source.isEmpty())
            url = url + "&source="+ source;
        return makeRequest(url,HistoricalExchangeRateRoot.class);
    }

    public Mono<ExchangeRateJsonRoot> live(String currencies, String source) throws CurrencyException {
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
        if(useUrl2)
            return makeRequest(url2,ExchangeRateJsonRoot.class);
        else
            return makeRequest("/live",ExchangeRateJsonRoot.class);

    }


    public Mono<TimeFrameJson> timeframe2(String startDate, String endDate, String currencies, String source) {
        String url = "/timeframe?start_date="+startDate+"&end_date="+endDate;
        if (currencies != null && !currencies.isEmpty())
            url = url + "&currencies="+ currencies;

        if (source != null && !source.isEmpty())
            url = url + "&source="+ source;
        return makeRequest2(url,TimeFrameJson.class);
    }
    public Mono<TimeFrameJson> timeframe(String startDate, String endDate, String currencies, String source) throws CurrencyException {

        String url = "/timeframe?start_date="+startDate+"&end_date="+endDate;
        if (currencies != null && !currencies.isEmpty())
            url = url + "&currencies="+ currencies;

        if (source != null && !source.isEmpty())
            url = url + "&source="+ source;
        return makeRequest(url,TimeFrameJson.class);
    }

}
