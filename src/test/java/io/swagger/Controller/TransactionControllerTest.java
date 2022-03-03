package io.swagger.Controller;

import io.swagger.repository.UserRepository;
import io.swagger.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "Admin", password = "Admin", roles = "EMPLOYEE")
    public void getPerformedTransactionsShouldReturnBadRequest() throws Exception {
        this.mockMvc.perform(get("/transactions/history"))
                .andExpect(status().isBadRequest());
    }

    
}
