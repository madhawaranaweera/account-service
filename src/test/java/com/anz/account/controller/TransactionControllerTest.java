package com.anz.account.controller;

import com.anz.account.AccountServiceApplication;
import com.anz.account.api.TransactionResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = AccountServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionControllerTest {
    private static final String BASE_URL = "http://localhost:";
    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int port;

    private static Stream<Arguments> testGetTransactionsByAccountId_success() {
        return Stream.of(
                Arguments.of("123456", 111156711L, 5),
                Arguments.of("000000", 585309209L, 0)
        );
    }

    @ParameterizedTest()
    @MethodSource
    void testGetTransactionsByAccountId_success(String userId, Long accountId, int expectedResults) {
        StringBuilder url = new StringBuilder(BASE_URL).append(port)
                .append("/anz/wholesale/users/")
                .append(userId)
                .append("/accounts/")
                .append(accountId)
                .append("/transactions?page=0&size=50");

        ResponseEntity<TransactionResponse> transactionResponse = testRestTemplate.getForEntity(url.toString(), TransactionResponse.class);
        assertEquals(HttpStatus.OK, transactionResponse.getStatusCode());
        assertNotNull(transactionResponse.getBody());
        assertNotNull(transactionResponse.getBody()
                .getTransactions());
        assertEquals(expectedResults, transactionResponse.getBody()
                .getTransactions()
                .size());
    }
}
