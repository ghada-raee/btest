package com.example.banktest.accountpackage;

import com.example.banktest.GenericResponse;
import com.example.banktest.currencypackage.ConvertJsonRoot;
import com.example.banktest.customerpackage.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/account")
public class AccountController {
    private final AccountService accountService;

    Logger logger = LoggerFactory.getLogger(AccountController.class);

    //create account
    @PostMapping("/createaccount")
    @ResponseBody
    @Operation(
            summary = "to create an account for customer",
            description = "Account types are: SALARY, SAVING, INVESTMENT. " +
                    "For the request, YOU ONLY NEED: accountId, currency, accountType",
            responses = {
                    @ApiResponse(responseCode = "403", description = "Exception",
                            content = @Content(schema = @Schema(
                                    example = "{\"message\":\"Exception Message\",\"status\":403,\"data\":null}"))),
                    @ApiResponse(responseCode = "200", description = "Account added",
                            content = @Content(schema = @Schema(implementation = AccountResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request or validation error",
                            content = @Content(schema = @Schema(
                                    example = "{\"message\":\"message\",\"status\":400,\"data\":null}"))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(
                                    example = "{\n\"error\":\"Internal Server Error\",\n\"status\":500,\n\"timestamp\"" +
                                            ":2023-08-19T16:52:58.894+00:00" + ",\n\"path\": /api/v1/customer/getcustomer\n}"))),

            }
    )
    public ResponseEntity<Object> createAccount(
            @RequestBody AccountRequest account
    ) {

        AccountResponse acc;
        try {
            acc = accountService.createAccount(account);
            logger.info("Account added");
        } catch (AccountException | NullPointerException e) {
            logger.error(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        } catch (CustomerException e) {
            logger.error(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
        return GenericResponse.generateResponse(
                "Successfully created account:",
                HttpStatus.OK,
                acc
        );
    }

    //list accounts
    @GetMapping("/listaccounts")
    @ResponseBody
    @Operation(
            summary = "list all customer's accounts",
            description = "YOU DON'T NEED TO USE ALL FIELDS. USE ONLY: customer_id.\n" +
                    "Also, the request won't work in Swagger-UI since OAS 3.0 doesn't support the request body for GET methods",
            responses = {
                    @ApiResponse(responseCode = "403", description = "Exception",
                            content = @Content(schema = @Schema(
                                    example = "{\"message\":\"Exception Message\",\"status\":403,\"data\":null}"))),
                    @ApiResponse(responseCode = "200", description = "Retrieved accounts",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AccountResponse.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(
                                    example = "{\n\"error\":\"Internal Server Error\",\n\"status\":500,\n\"timestamp\"" +
                                            ":2023-08-19T16:52:58.894+00:00" + ",\n\"path\": /api/v1/customer/getcustomer\n}"))),
            }
    )
    public ResponseEntity<Object> listAccounts(
            @RequestBody AccountRequest account
    ) {

        List<AccountResponse> accs;
        try {
            accs = accountService.listAccounts(account);
            logger.info("Accounts listed");
        } catch (AccountException | CustomerException e) {
            logger.error(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
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
    @Operation(
            summary = "get a customer's account details",
            description = "YOU DON'T NEED TO USE ALL FIELDS. USE ONLY: accountId.\n" +
                    "Also, the request won't work in Swagger-UI since OAS 3.0 doesn't support the request body for GET methods",
            responses = {
                    @ApiResponse(responseCode = "403", description = "Exception",
                            content = @Content(schema = @Schema(
                                    example = "{\"message\":\"Exception Message\",\"status\":403,\"data\":null}"))),
                    @ApiResponse(responseCode = "200", description = "retrieved account",
                            content = @Content(schema = @Schema(implementation = AccountResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(
                                    example = "{\n\"error\":\"Internal Server Error\",\n\"status\":500,\n\"timestamp\"" +
                                            ":2023-08-19T16:52:58.894+00:00" + ",\n\"path\": /api/v1/customer/getcustomer\n}"))),
            }
    )
    public ResponseEntity<Object> getaccount(
            @RequestBody AccountRequest account
    ) {

        AccountResponse acc;
        try {
            acc = accountService.getAccount(account);

        } catch (AccountException  e) {
            logger.error(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
        return GenericResponse.generateResponse(
                "Successfully retrieved account",
                HttpStatus.OK,
                acc
        );
    }


    //update balance
    @PutMapping("/updatebalance")
    @ResponseBody
    @Operation(
            summary = "to update the balance",
            description = "The balance could be updated by either deducting (negative amount) or adding to it.\n" +
                    "YOU DON'T NEED TO USE ALL FIELDS. USE ONLY: accountId, amount, transactionPartyId, transactionCurrency",
            responses = {

                    @ApiResponse(responseCode = "400", description = "Exception",
                            content = @Content(schema = @Schema(
                                    example = "{\"message\":\"Exception Message\",\"status\":400,\"data\":null}"))),
                    @ApiResponse(responseCode = "200", description = "Balance updated",
                            content = @Content(schema = @Schema(implementation = AccountResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(
                                    example = "{\n\"error\":\"Internal Server Error\",\n\"status\":500,\n\"timestamp\"" +
                                            ":2023-08-19T16:52:58.894+00:00" + ",\n\"path\": /api/v1/customer/getcustomer\n}"))),
            }
    )
    public ResponseEntity<Object> updateBalance(
            @RequestBody AccountRequest account
    ) {

        TransactionResponse balance;
        try {
            balance = accountService.updateBalance(account);
            logger.info("Balance changed");
        } catch (AccountException e) {
            logger.error(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
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
    @Operation(
            summary = "to change the account currency",
            description = "for the request body YOU DON'T NEED TO USE ALL FIELDS. USE ONLY: accountId." +
                    " & you need the required paramters too",
            parameters = {
                    @Parameter(name="to",description = "to what currency?",example = "USD", required = true),
                    @Parameter(name = "date", example = "2023-07-14",required = false)
            },
            responses = {
                    @ApiResponse(responseCode = "403", description = "Exception",
                            content = @Content(schema = @Schema(
                                    example = "{\"message\":\"Exception Message\",\"status\":403,\"data\":null}"))),
                    @ApiResponse(responseCode = "200", description = "Currency changed",
                            content = @Content(schema = @Schema(implementation = AccountResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(
                                    example = "{\n\"error\":\"Internal Server Error\",\n\"status\":500,\n\"timestamp\"" +
                                            ":2023-08-19T16:52:58.894+00:00" + ",\n\"path\": /api/v1/customer/getcustomer\n}"))),
            }
    )
    public ResponseEntity<Object> changeCurrency(
            @RequestBody AccountRequest account,
            @RequestParam(name = "to", required = true) String to,
            @RequestParam(name = "date", required = false) String date
    ) {
        Mono<ConvertJsonRoot> acc;
    try{

            acc = accountService.changeCurrency(account,to,date);
    } catch (AccountException e) {
        logger.error(e.getMessage());
        return GenericResponse.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
    }

        return GenericResponse.generateResponse(
                "New Currency",
                HttpStatus.OK,
                acc.block()
        );
    }

    //activate account
    @PutMapping("/activateAccount")
    @ResponseBody
    @Operation(
            summary = "to activate account",
            description = "for the request body YOU DON'T NEED TO USE ALL FIELDS. USE ONLY: accountId." ,
            responses = {
                    @ApiResponse(responseCode = "403", description = "Exception",
                            content = @Content(schema = @Schema(
                                    example = "{\"message\":\"Exception Message\",\"status\":403,\"data\":false}"))),
                    @ApiResponse(responseCode = "200", description = "Account activated",
                            content = @Content(schema = @Schema(type = "boolean"))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(
                                    example = "{\n\"error\":\"Internal Server Error\",\n\"status\":500,\n\"timestamp\"" +
                                            ":2023-08-19T16:52:58.894+00:00" + ",\n\"path\": /api/v1/customer/getcustomer\n}"))),
            }
    )
    public ResponseEntity<Object> activateAccount(
            @RequestBody AccountRequest account
    ) {

        try {
            accountService.activateAccount(account);
            logger.info("Account activated");
        } catch (AccountException e) {
            logger.error(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, false);
        }


        return GenericResponse.generateResponse(
                "Account activated",
                HttpStatus.OK,
                true
        );
    }
    @PutMapping("/deactivateAccount")
    @ResponseBody
    @Operation(
            summary = "to deactivate account",
            description = "for the request body YOU DON'T NEED TO USE ALL FIELDS. USE ONLY: accountId." ,
            responses = {

                    @ApiResponse(responseCode = "403", description = "Exception",
                            content = @Content(schema = @Schema(
                                    example = "{\"message\":\"Exception Message\",\"status\":403,\"data\":false}"))),
                    @ApiResponse(responseCode = "200", description = "Account deactivated",
                            content = @Content(schema = @Schema(type = "boolean"))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(
                                    example = "{\n\"error\":\"Internal Server Error\",\n\"status\":500,\n\"timestamp\"" +
                                            ":2023-08-19T16:52:58.894+00:00" + ",\n\"path\": /api/v1/customer/getcustomer\n}"))),
                    @ApiResponse(responseCode = "400", description = "Bad Request",
                            content = @Content(schema = @Schema(
                                    example = "{\n\"error\":\"Bad Request\",\n\"status\": 400,\n\"timestamp\"" +
                                            ":2023-08-19T16:52:58.894+00:00" + ",\n\"path\": /api/v1/customer/getcustomer\n}")))
            }
    )
    public ResponseEntity<Object> deactivateAccount(
            @RequestBody AccountRequest account
    ) {

        try{
            accountService.deactivateAccount(account);
            logger.info("Account deactivated");
        } catch (AccountException e) {
            logger.error(e.getMessage());
            return GenericResponse.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, false);
        }

        return GenericResponse.generateResponse(
                "Account deactivated",
                HttpStatus.OK,
                true
        );
    }

}
