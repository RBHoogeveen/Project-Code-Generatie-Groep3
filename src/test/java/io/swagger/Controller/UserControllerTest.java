package io.swagger.Controller;

import io.swagger.model.Role;
import io.swagger.model.User;
import io.swagger.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    private User user;

    @BeforeEach
    public void setUp() {
        user.setIsActive(true);
        user.setEmail("User@user.nl");
        user.setFirstname("User");
        user.setLastname("User");
        user.setDayLimit(BigDecimal.valueOf(54321));
        user.setPhonenumber("06-34343434");
        user.setRoles(Collections.singletonList(Role.ROLE_USER));
        user.setTransactionLimit(BigDecimal.valueOf(3000));
        user.setUsername("User");
        user.setPassword("User");
        user.setDaySpent(BigDecimal.ZERO);
    }

    @Test
    public void BankUserShouldNotBeNull() throws Exception {
        given(userService.findAllUsers()).willReturn(Collections.singletonList(user));
        this.mockMvc.perform(get("/users")).andExpect(status().isOk());
    }

    @Test
    public void TestUserShouldNotBeNull() {
        assertNotNull(this.user);
    }
}
