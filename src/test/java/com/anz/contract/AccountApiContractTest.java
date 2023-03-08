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
public class AccountApiContractTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenValidUserId_callGetAccountsApi_returnHttpOk() throws Exception {
        mockMvc.perform(get("/users/{user-id}/accounts", "123456")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void giveninValidUserId_callGetAccountsApi_returnHttpBadRequest() throws Exception {
        mockMvc.perform(get("/users/{user-id}/accounts", "12378@")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenNotAvailableUserId_callGetAccountsApi_returnHttpNotFound() throws Exception {
        mockMvc.perform(get("/users/{user-id}/accounts", "123789")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenValidUserIdUserId_callGetAccountsApi_inValidAccountName_returnHttpInternalServerError() throws Exception {
        mockMvc.perform(get("/users/{user-id}/accounts", "999999")
                        .contentType("application/json"))
                .andExpect(status().isInternalServerError());
    }
}
