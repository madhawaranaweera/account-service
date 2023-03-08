package com.anz.contract;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionApiContractTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenValidAccountIdAndValidUserId_callGetTransactionsApi_returnHttpOk() throws Exception {
        mockMvc.perform(get("/users/{user-id}/accounts/{account-id}/transactions", "123456", 111156711L)
                        .param("page", "0")
                        .param("size", "50")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void givenInValidAccountIdAndInValidUserId_callGetAccountApi_returnHttpBadRequest() throws Exception {
        mockMvc.perform(get("/users/{user-id}/accounts/{account-id}/transactions", "12345@", 111156711L)
                        .param("page", "0")
                        .param("size", "50")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenNotAvailableAccountIdAndValidUserId_callGetAccountApi_returnHttpNotFound() throws Exception {
        mockMvc.perform(get("/users/{user-id}/accounts/{account-id}/transactions", "123456", 211156711L)
                        .param("page", "0")
                        .param("size", "50")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenValidAccountIdAndValidUserId_callGetAccountApi_inValidAccountName_returnHttpInternalServerError() throws Exception {
        mockMvc.perform(get("/users/{user-id}/accounts/{account-id}/transactions", "999999", 111156722)
                        .param("page", "0")
                        .param("size", "50")
                        .contentType("application/json"))
                .andExpect(status().isInternalServerError());
    }
}
