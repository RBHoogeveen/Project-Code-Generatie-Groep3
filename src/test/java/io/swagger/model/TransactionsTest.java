package io.swagger.model;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransactionsTest {

    private Transaction transaction;

    @Before
    public void SetUp() {
        transaction = new Transaction();
    }

    @Test
    public void createTransactionShouldNotBeNull() {
        assertNotNull(transaction);
    }

    @Test
    public void SettingAmountBelowZero_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> transaction.setAmount(new BigDecimal(-1)));
        assertEquals("Amount cannot be below zero", exception.getMessage());
    }
}
