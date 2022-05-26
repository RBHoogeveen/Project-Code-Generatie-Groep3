package io.swagger.repository;

import io.swagger.model.Account;
import io.swagger.model.Transaction;
import io.swagger.model.User;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TransactionRepositoryTests {
    private User performerUser;

    private Account performerAccount;
    private Account targetAccount;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private AccountRepository accountRepo;

    @Before
    public void SetUp() {
        performerAccount = new Account();
        performerAccount.setBalance(new BigDecimal("500.00"));
        performerAccount.setType(false);
        performerAccount.setIban("NL19INHO0123456789");
        accountRepo.save(performerAccount);

        targetAccount = new Account();
        targetAccount.setBalance(new BigDecimal("500.00"));
        targetAccount.setType(false);
        targetAccount.setIban("NL19INHO9876543210");
        accountRepo.save(targetAccount);
    }

    @Test
    public void PostTransaction_ShouldReturnTransaction() {
        // Prepare
        Transaction newTransaction = new Transaction();
        transactionRepo.save(newTransaction);

        // Then
        Transaction transaction = transactionRepo.getTransactionById(newTransaction.getId());

        // Assert
        assertThat(transaction).isEqualTo(newTransaction);
    }

    @Test
    public void UpdateBalance_ShouldReturnUpdatedBalance() {
        // Prepare
        Account account = new Account();
        account.setBalance(new BigDecimal("500.00"));
        account.setType(false);
        account.setIban("NL19INHO0123456789");
        accountRepo.save(account);
        BigDecimal newBalance = new BigDecimal("999.00");

        // Then
        transactionRepo.UpdateBalance(newBalance, "NL19INHO0123456789", false);
        Account performer = accountRepo.getAccountByIban("NL19INHO0123456789");

        // Assert
        assertThat(performer.getBalance()).isEqualTo(newBalance);
    }
}
