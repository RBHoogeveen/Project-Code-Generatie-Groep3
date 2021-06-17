package io.swagger.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @Test
    @WithMockUser(username = "Bank", password = "Bank", roles = "EMPLOYEE")
    public void getPerformedTransactionShouldReturnZero() {

    }
}
