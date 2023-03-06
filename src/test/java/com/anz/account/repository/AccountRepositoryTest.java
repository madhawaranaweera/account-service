package com.anz.account.repository;

import com.anz.account.entity.Account;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AccountRepositoryTest {
    @Autowired
    AccountRepository accountRepository;

    @ParameterizedTest()
    @MethodSource
    public void testFindAllByUserId(String userId, int expectedCount) {
        List<Account> accounts = accountRepository.findAllByUserId(userId);

        assertThat(accounts).isNotNull();
        assertThat(accounts.size()).isEqualTo(expectedCount);
    }

    private static Stream<Arguments> testFindAllByUserId() {
        return Stream.of(
                Arguments.of("123456", 11),
                Arguments.of("000000", 0)

        );
    }
}
