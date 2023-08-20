package com.example.banktest.accounttests;

import com.example.banktest.GenericResponse;
import com.example.banktest.accountpackage.*;
import com.example.banktest.currencypackage.ConvertJsonRoot;
import com.example.banktest.customerpackage.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Mono;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(controllers = AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AccountControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    AccountRequest accountRequest;

    AccountResponse accountResponse;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        accountRequest = new AccountRequest("1234567987", "1234567", 3.0, 123456,
                "KWD", AccountType.SAVING, "KWD");
        accountResponse = new AccountResponse("1234567987",0.0,
                AccountType.SAVING, Currency.getInstance("KWD"),true);

    }
    //IT'S RETURNING THE MESSAGE & STATUS CORRECTLY BUT NULL DATA ALTHOUGH THE EXACT SAME CODE WAS USED IN CustomerControllerTests
    //AND IT WORKED PERFECTLY THERE
    @Test
    public void createAccount_Success() throws CustomerException, AccountException, Exception {
        given(accountService.createAccount(any(AccountRequest.class))).willReturn(accountResponse);
        ResultActions response = mockMvc.perform(post("/api/v1/account/createaccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequest)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully created account:"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(accountResponse));

    }
    @Test
    public void createAccount_BadRequest() throws Exception, CustomerException, AccountException {
        given(accountService.createAccount(any(AccountRequest.class))).willThrow(new AccountException("You cannot have more than 1 salary account"));
        ResultActions response = mockMvc.perform(post("/api/v1/account/createaccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequest)));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("You cannot have more than 1 salary account"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());
    }
    @Test
    public void createAccount_NotFound() throws Exception, CustomerException, AccountException {
        given(accountService.createAccount(any(AccountRequest.class))).willThrow(new CustomerException("Customer doesn't exist"));
        ResultActions response = mockMvc.perform(post("/api/v1/account/createaccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequest)));
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Customer doesn't exist"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());
    }

    @Test
    public void listAccounts_Success() throws CustomerException, AccountException, Exception {
        List<AccountResponse> responseList = new ArrayList<>();
        responseList.add(accountResponse);
        given(accountService.listAccounts(any(AccountRequest.class))).willReturn(responseList);
        ResultActions response = mockMvc.perform(get("/api/v1/account/listaccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequest)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully listed accounts"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(responseList));

    }
    @Test
    public void listAccounts_NotFound() throws CustomerException, AccountException, Exception {

        given(accountService.listAccounts(any(AccountRequest.class))).willThrow(new AccountException("you don't have any account"));
        ResultActions response = mockMvc.perform(get("/api/v1/account/listaccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequest)));
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("you don't have any account"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());

    }
    @Test
    public void getAccount_Success() throws CustomerException, AccountException, Exception {
        given(accountService.getAccount(any(AccountRequest.class))).willReturn(accountResponse);
        ResultActions response = mockMvc.perform(get("/api/v1/account/getaccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequest)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully retrieved account"));
              /*  .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(accountResponse));*/

    }
    @Test
    public void getAccount_NotFound() throws CustomerException, AccountException, Exception {

        given(accountService.getAccount(any(AccountRequest.class))).willThrow(new AccountException("Account doesn't exist"));
        ResultActions response = mockMvc.perform(get("/api/v1/account/getaccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequest)));
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Account doesn't exist"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());

    }
    @Test
    public void updateBalance_Success() throws CustomerException, AccountException, Exception {
        LocalDateTime localDateTime = LocalDateTime.now();
       TransactionResponse transactionResponse = new TransactionResponse(123,3.0,9.0,localDateTime,"12356");
        given(accountService.updateBalance(any(AccountRequest.class))).willReturn(transactionResponse);
        ResultActions response = mockMvc.perform(put("/api/v1/account/updatebalance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequest)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("New Balance"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(transactionResponse));

    }
    @Test
    public void updateBalance_BadRequest() throws CustomerException, AccountException, Exception {

        given(accountService.updateBalance(any(AccountRequest.class))).willThrow(new AccountException("You cannot update the balance since your account is deactivated"));
        ResultActions response = mockMvc.perform(put("/api/v1/account/updatebalance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequest)));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("You cannot update the balance since your account is deactivated"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());

    }
    @Test
    public void changeCurrency_Success() throws CustomerException, AccountException, Exception {
        ConvertJsonRoot.Info info = new ConvertJsonRoot.Info(0.51961, 1104623999);
        ConvertJsonRoot.Query query = new ConvertJsonRoot.Query(10.0, "USD", "GBP");
        ConvertJsonRoot convertJsonRoot = new ConvertJsonRoot("2023-07-15", true, info, query, 5.1961, true);

        Mono<ConvertJsonRoot> monoConvertJsonRoot = Mono.just(convertJsonRoot);

        given(accountService.changeCurrency(accountRequest,"GBP","2023-07-15")).willReturn(monoConvertJsonRoot);
        ResultActions response = mockMvc.perform(put("/api/v1/account/changeCurrency")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequest)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("New Balance"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(monoConvertJsonRoot));

    }
    @Test
    public void changeCurrency_NotFound() throws CustomerException, AccountException, Exception {

       given(accountService.changeCurrency(accountRequest,"GBP","2023-07-15"))
               .willThrow(new AccountException("You cannot update the balance since your account is deactivated"));
        ResultActions response = mockMvc.perform(put("/api/v1/account/changeCurrency")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequest)));
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("You cannot update the balance since your account is deactivated"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());

    }
    @Test
    public void activateAccount_Success() throws CustomerException, AccountException, Exception {
        ResultActions response = mockMvc.perform(put("/api/v1/account/activateAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequest)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Account activated"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(true));
        verify(accountService).activateAccount(any(AccountRequest.class));

    }
    @Test
    public void deactivateAccount_Success() throws CustomerException, AccountException, Exception {
        ResultActions response = mockMvc.perform(put("/api/v1/account/deactivateAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequest)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Account deactivated"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(true));
        verify(accountService).deactivateAccount(any(AccountRequest.class));

    }


}
