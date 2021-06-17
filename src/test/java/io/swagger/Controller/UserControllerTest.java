package io.swagger.Controller;

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
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "Bank", password = "Bank", roles = "EMPLOYEE")
    public void getUserByUsernameShouldReturnOk() throws Exception {
        this.mockMvc.perform(get("/users/Bank"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "Admin", password = "Admin", roles = "Admin")
    public void getUsersShouldReturnOk() throws Exception {
        this.mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }
}
