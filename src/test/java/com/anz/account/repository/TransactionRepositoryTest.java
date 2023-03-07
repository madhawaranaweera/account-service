package com.anz.account.repository;

import com.anz.account.entity.Transaction;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.NONE)
public class TransactionRepositoryTest {
    @Autowired
    TransactionRepository transactionRepository;

    private static Stream<Arguments> testFindAllByAccount() {
        return Stream.of(
                Arguments.of(111156711L, "123456", 5),
                Arguments.of(123309209L, "123456", 0)

        );
    }

    @ParameterizedTest()
    @MethodSource
    public void testFindAllByAccount(Long accountId, String userId, int expectedCount) {
        Page<Transaction> transactions = transactionRepository.findByAccountIdAndAccountUserId(accountId, userId, PageRequest.of(0, 50));

        assertThat(transactions).isNotNull();
        assertThat(transactions.getContent()
                .size()).isEqualTo(expectedCount);
    }
}
