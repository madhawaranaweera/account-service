package com.anz.repository;

import com.anz.account.entity.AccountEntity;
import com.anz.transaction.entity.TransactionEntity;
import com.anz.transaction.repository.TransactionRepository;
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

    private static Stream<Arguments> givenAccountId_findAllTransactionsByAccountId() {
        return Stream.of(
                Arguments.of(AccountEntity.builder()
                        .id(111156711L)
                        .build(), "123456", 5),
                Arguments.of(AccountEntity.builder()
                        .id(111156711L)
                        .build(), "123459", 0),
                Arguments.of(AccountEntity.builder()
                        .id(123309209L)
                        .build(), "123456", 0)
        );
    }

    @ParameterizedTest()
    @MethodSource("givenAccountId_findAllTransactionsByAccountId")
    void givenAccountId_findAllTransactionsByAccountId(AccountEntity account, String userId, int expectedCount) {
        Page<TransactionEntity> transactions = transactionRepository.findAllByAccountAndAccountUserId(account, userId, PageRequest.of(0, 50));

        assertThat(transactions).isNotNull();
        assertThat(transactions.getContent()
                .size()).isEqualTo(expectedCount);
    }
}
