package io.swagger.model;

import io.swagger.model.DTO.TransactionDTO;
import io.swagger.service.TransactionService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransactionsTest {

    private TransactionDTO transaction;
    private TransactionService transactionService;

    @Before
    public void SetUp() {
        transaction = new TransactionDTO();
        transactionService = new TransactionService();
    }

    @Test
    public void CreateTransactionShouldNotBeNull() {
        transaction.setAmount(new BigDecimal(1000));
        transaction.setPerformerIban("NL02INHO0000000002");
        transaction.setTargetIban("NL01INHO0000000001");
        assertNotNull(transaction);
    }

    @Test
    public void CreateTransaction_AmountIsNull() {
        transaction.setPerformerIban("NL02INHO0000000002");
        transaction.setTargetIban("NL01INHO0000000001");
        assertNull(transaction.getAmount());
    }

    @Test
    public void CreateTransaction_PerformerIsNull() {
        transaction.setAmount(new BigDecimal(1000));
        transaction.setTargetIban("NL01INHO0000000001");
        assertNull(transaction.getPerformerIban());
    }

    @Test
    public void CreateTransaction_TargetIsNull() {
        transaction.setAmount(new BigDecimal(1000));
        transaction.setPerformerIban("NL02INHO0000000002");
        assertNull(transaction.getTargetIban());
    }

    @Test
    public void PerformingTransaction_EmptyAuthContext() {
        transaction.setAmount(new BigDecimal(100));
        transaction.setPerformerIban("NL02INHO0000000002");
        transaction.setTargetIban("NL01INHO0000000001");

        Exception exception = assertThrows(NullPointerException.class,
                () -> transactionService.performTransaction(transaction));
    }
}
