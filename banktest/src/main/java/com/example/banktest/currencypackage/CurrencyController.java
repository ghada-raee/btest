package com.example.banktest.currencypackage;

import com.example.banktest.GenericResponse;
import com.example.banktest.accountpackage.Account;
import com.example.banktest.accountpackage.AccountRequest;
import com.example.banktest.accountpackage.AccountResponse;
import com.example.banktest.customerpackage.Customer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(
            summary = "to list all currencies",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Currency changed",
                            content = @Content(schema = @Schema(implementation = ListCurrencyJsonRoot.class)),
                            links = @Link(
                                    name = "More Info",
                                    operationId = "list currency",
                                    description = "Link to more information about the response:" +
                                            " https://apilayer.com/marketplace/currency_data-api?e=Sign+In&l=Success?e=Sign+In&l=Success#endpoints"
                            )),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(
                                    example = "{\n\"error\":\"Internal Server Error\",\n\"status\":500,\n\"timestamp\"" +
                                            ":2023-08-19T16:52:58.894+00:00" + ",\n\"path\": /api/v1/currency/listcurrency\n}"))),
            }
    )
    public ResponseEntity<Object> listingCurrency()  {
        Mono<ListCurrencyJsonRoot> result;

        try{
            result = currencyService.listCurrency();

        } catch(Exception e){
            logger.error(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.OK, null);
        }
        return GenericResponse.generateResponse("Successful", HttpStatus.OK, result.block());
    }

    @GetMapping("/change")
    @ResponseBody
    @Operation(
            summary = "to change margin & percentage of currency/ies",
            description = "Change endpoint, you may request the change (both margin and percentage) of one or more currencies," +
                    " relative to a Source Currency, within a specific time-frame (optional).",
            parameters = {
                    @Parameter(name="start_date",example = "2023-07-14", required = true),
                    @Parameter(name = "end_date", example = "2023-07-15",required = true),
                    @Parameter(name="currencies",description = "comma-seperated list of currencies",example = "EUR,KWD", required = false),
                    @Parameter(name = "source",description = "source currency", example = "USD",required = false)
            },
            responses = {
                    @ApiResponse(responseCode = "400", description = "Exception",
                            content = @Content(schema = @Schema(
                                    example = "{\"message\":\"Exception Message\",\"status\":400,\"data\":null}"))),
                    @ApiResponse(responseCode = "200", description = "Currency changed",
                            content = @Content(schema = @Schema(implementation = ChangeJsonRoot.class)),
                            links = @Link(
                                    name = "More Info",
                                    operationId = "change",
                                    description = "Link to more information about the response:" +
                                            " https://apilayer.com/marketplace/currency_data-api?e=Sign+In&l=Success?e=Sign+In&l=Success#endpoints"
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(
                                    example = "{\n\"error\":\"Internal Server Error\",\n\"status\":500,\n\"timestamp\"" +
                                            ":2023-08-19T16:52:58.894+00:00" + ",\n\"path\": /api/v1/customer/getcustomer\n}"))),
            }
    )
    public ResponseEntity<Object> change(
            @RequestParam(name = "start_date", required = true)  String startDate,
            @RequestParam(name = "end_date", required = true) String endDate,
            @RequestParam(name = "currencies", required = false) String currencies,
            @RequestParam(name = "source", required = false) String source
    ) {

        Mono<ChangeJsonRoot> result;
        try{
            result = currencyService.change(startDate,endDate,currencies,source);
        } catch(IllegalArgumentException e){
            logger.error(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
        return GenericResponse.generateResponse("Successful", HttpStatus.OK, result.block());

    }

    @GetMapping("/historical")
    @ResponseBody
    @Operation(
            summary = "Historical exchange rate data",
            description = "Historical exchange rate data for every past day all the way back to the year of 1999.",
            parameters = {
                    @Parameter(name="date",example = "2023-07-14", required = true),
                    @Parameter(name="currencies",description = "comma-seperated list of currencies",example = "EUR,KWD", required = false),
                    @Parameter(name = "source",description = "source currency", example = "USD",required = false)
            },
            responses = {
                    @ApiResponse(responseCode = "400", description = "Exception",
                            content = @Content(schema = @Schema(
                                    example = "{\"message\":\"Exception Message\",\"status\":400,\"data\":null}"))),
                    @ApiResponse(responseCode = "200", description = "Currency changed",
                            content = @Content(schema = @Schema(implementation = HistoricalExchangeRateRoot.class)),
                            links = @Link(
                                    name = "More Info",
                                    operationId = "historical",
                                    description = "Link to more information about the response:" +
                                            " https://apilayer.com/marketplace/currency_data-api?e=Sign+In&l=Success?e=Sign+In&l=Success#endpoints"
                            )),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(
                                    example = "{\n\"error\":\"Internal Server Error\",\n\"status\":500,\n\"timestamp\"" +
                                            ":2023-08-19T16:52:58.894+00:00" + ",\n\"path\": /api/v1/customer/getcustomer\n}"))),
            }
    )
    public ResponseEntity<Object> historical(
            @RequestParam(name = "date", required = true) String date,
            @RequestParam(name = "currencies", required = false) String currencies,
            @RequestParam(name = "source", required = false) String source
    ) {

        Mono<HistoricalExchangeRateRoot> result;
        try{
            result = currencyService.historical(date,currencies,source);
        } catch(IllegalArgumentException e){
            logger.error(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }

        return GenericResponse.generateResponse("Successful", HttpStatus.OK, result.block());
    }

    @GetMapping("/live")
    @ResponseBody
    @Operation(
            summary = "Real-time exchange rates.",
            parameters = {

                    @Parameter(name="currencies",description = "comma-seperated list of currencies",example = "EUR,KWD", required = false),
                    @Parameter(name = "source",description = "source currency", example = "USD",required = false)
            },
            responses = {
                    @ApiResponse(responseCode = "400", description = "Exception",
                            content = @Content(schema = @Schema(
                                    example = "{\"message\":\"Exception Message\",\"status\":400,\"data\":null}"))),
                    @ApiResponse(responseCode = "200", description = "Currency changed",
                            content = @Content(schema = @Schema(implementation = ExchangeRateJsonRoot.class)),
                            links = @Link(
                                    name = "More Info",
                                    operationId = "live",
                                    description = "Link to more information about the response:" +
                                            " https://apilayer.com/marketplace/currency_data-api?e=Sign+In&l=Success?e=Sign+In&l=Success#endpoints"
                            )),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(
                                    example = "{\n\"error\":\"Internal Server Error\",\n\"status\":500,\n\"timestamp\"" +
                                            ":2023-08-19T16:52:58.894+00:00" + ",\n\"path\": /api/v1/customer/getcustomer\n}"))),
            }
    )
    public ResponseEntity<Object> live(
            @RequestParam(name = "currencies", required = false) String currencies,
            @RequestParam(name = "source", required = false) String source
    ) {
        Mono<ExchangeRateJsonRoot> result;
        try {
           result = currencyService.live(currencies, source);
        }
        catch(IllegalArgumentException e){
            logger.error(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
        return GenericResponse.generateResponse("Successful", HttpStatus.OK, result.block());

    }


    @GetMapping("/timeframe")
    @ResponseBody
    @Operation(
            summary = "daily historical rates between two dates",
            description = "Timeframe endpoint lets you query the API for daily historical rates between two dates of your choice," +
                    " with a maximum time frame of 365 days.",
            parameters = {
                    @Parameter(name="start_date",example = "2023-07-14", required = true),
                    @Parameter(name = "end_date", example = "2023-07-15",required = true),
                    @Parameter(name="currencies",description = "comma-seperated list of currencies",example = "EUR,KWD", required = false),
                    @Parameter(name = "source",description = "source currency", example = "USD",required = false)
            },
            responses = {
                    @ApiResponse(responseCode = "400", description = "Exception",
                            content = @Content(schema = @Schema(
                                    example = "{\"message\":\"Exception Message\",\"status\":400,\"data\":null}"))),
                    @ApiResponse(responseCode = "200", description = "Currency changed",
                            content = @Content(schema = @Schema(implementation = TimeFrameJson.class)),
                            links = @Link(
                                    name = "More Info",
                                    operationId = "timeframe",
                                    description = "Link to more information about the response:" +
                                            " https://apilayer.com/marketplace/currency_data-api?e=Sign+In&l=Success?e=Sign+In&l=Success#endpoints"
                            )),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(
                                    example = "{\n\"error\":\"Internal Server Error\",\n\"status\":500,\n\"timestamp\"" +
                                            ":2023-08-19T16:52:58.894+00:00" + ",\n\"path\": /api/v1/customer/getcustomer\n}"))),
            }
    )
    public ResponseEntity<Object> timeframe(
            @RequestParam(name = "start_date", required = true) String startDate,
            @RequestParam(name = "end_date", required = true) String endDate,
            @RequestParam(name = "currencies", required = false) String currencies,
            @RequestParam(name = "source", required = false) String source
    ) {


        Mono<TimeFrameJson> result;
        try{
            result = currencyService.timeframe(startDate,endDate,currencies,source);
        } catch(IllegalArgumentException e){
            logger.error(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
        return GenericResponse.generateResponse("Successful", HttpStatus.OK, result.block());

    }



}
