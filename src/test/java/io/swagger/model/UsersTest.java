package io.swagger.model;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UsersTest {

    private User User;

    @Before
    public void SetUp() {
        User = new User();
    }

    @Test
    public void createUserShouldNotBeNull() {
        assertNotNull(User);
    }

//    @Test
//    public void SettingAmountBelowZero_ThrowsException() {
//        Exception exception = assertThrows(IllegalArgumentException.class,
//                () -> User.setAmount(new BigDecimal(-1)));
//        assertEquals("Amount cannot be below zero", exception.getMessage());
//    }
}
