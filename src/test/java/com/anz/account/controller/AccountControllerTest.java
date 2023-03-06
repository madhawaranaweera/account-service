package com.anz.account.controller;

import com.anz.account.AccountServiceApplication;
import com.anz.account.api.ApiError;
import com.anz.account.api.ViewAccountsResponse;
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

@SpringBootTest(classes = AccountServiceApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    private static final String BASE_URL = "http://localhost:";

    @LocalServerPort
    private int port;

    @ParameterizedTest()
    @MethodSource
    void testGetAccountsByUserId_success(String userId, int expectedResults) {
        StringBuilder url = new StringBuilder(BASE_URL).append(port).append("/wholesale/users/").append(userId).append("/accounts");

        ResponseEntity<ViewAccountsResponse> accountResponse = testRestTemplate.getForEntity(url.toString(), ViewAccountsResponse.class);
        assertEquals(HttpStatus.OK, accountResponse.getStatusCode());
        assertNotNull(accountResponse.getBody());
        assertNotNull(accountResponse.getBody().getAccounts());
        assertEquals(expectedResults,accountResponse.getBody().getAccounts().size());
    }

    private static Stream<Arguments> testGetAccountsByUserId_success() {
        return Stream.of(
                Arguments.of("123456", 11),
                Arguments.of("000000", 0)
        );
    }

    @ParameterizedTest()
    @MethodSource
    void testGetAccountsByUserId_throwException(String userId) {
        StringBuilder url = new StringBuilder(BASE_URL).append(port).append("/wholesale/users/").append(userId).append("/accounts");

        ResponseEntity<ApiError> apiError = testRestTemplate.getForEntity(url.toString(), ApiError.class);
        assertNotNull(apiError);
        assertEquals(HttpStatus.BAD_REQUEST,apiError.getStatusCode());
    }

    private static Stream<Arguments> testGetAccountsByUserId_throwException() {
        return Stream.of(
                Arguments.of("12345"),
                Arguments.of("12345123451234512345"),
                Arguments.of("123456@")

        );
    }

}
