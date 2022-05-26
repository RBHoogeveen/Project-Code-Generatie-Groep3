package io.swagger.model;

import io.swagger.model.DTO.TransactionDTO;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import io.swagger.service.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionsTest {

    private TransactionDTO transactionDTO;
    private Transaction transaction;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AccountRepository accountRepo;

    private User user;
    private User user1;

    @Before
    public void SetUp() {
        user = new User();
        user.setId(1101);
        user.setIsActive(true);
        userRepo.save(user);
        user1 = new User();
        user1.setId(1102);
        user1.setIsActive(true);
        userRepo.save(user1);

        transactionDTO = new TransactionDTO();
        transaction = new Transaction();
    }

    @Test
    public void CreateTransactionShouldNotBeNull() {
        transactionDTO.setAmount(new BigDecimal(1000));
        transactionDTO.setPerformerIban("NL02INHO0000000002");
        transactionDTO.setTargetIban("NL01INHO0000000001");
        assertNotNull(transactionDTO);
    }

    @Test
    public void CreateTransaction_AmountIsNull() {
        transactionDTO.setPerformerIban("NL02INHO0000000002");
        transactionDTO.setTargetIban("NL01INHO0000000001");
        assertNull(transactionDTO.getAmount());
    }

    @Test
    public void CreateTransaction_PerformerIsNull() {
        transactionDTO.setAmount(new BigDecimal(1000));
        transactionDTO.setTargetIban("NL01INHO0000000001");
        assertNull(transactionDTO.getPerformerIban());
    }

    @Test
    public void CreateTransaction_TargetIsNull() {
        transactionDTO.setAmount(new BigDecimal(1000));
        transactionDTO.setPerformerIban("NL02INHO0000000002");
        assertNull(transactionDTO.getTargetIban());
    }

    @Test
    public void PerformingTransaction_EmptyAuthContext() {
        transactionDTO.setAmount(new BigDecimal(100));
        transactionDTO.setPerformerIban("NL02INHO0000000002");
        transactionDTO.setTargetIban("NL01INHO0000000001");

        Exception exception = assertThrows(NullPointerException.class,
                () -> transactionService.performTransaction(transactionDTO));
    }

    @Test
    public void GetTransactionHistory_ShouldReturn0Transactions() {
        Account target = new Account();
        target.setIban("NL02INHO0000000002");
        Account performer = new Account();
        performer.setIban("NL02INHO0000000001");
        accountRepo.save(target);
        accountRepo.save(performer);

        List<Transaction> transactions = transactionService.getTransactionsByUser(1101);
        assertThat(transactions.size()).isEqualTo(0);
    }

    @Test
    public void GetTransactionHistory_ShouldReturn3Transaction() {

        List<Transaction> transactions = transactionService.getTransactionsByUser(2);
        assertThat(transactions.size()).isEqualTo(3);
    }
}