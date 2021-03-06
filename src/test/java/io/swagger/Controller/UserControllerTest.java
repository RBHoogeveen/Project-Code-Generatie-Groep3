package io.swagger.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.DTO.CreateUpdateUserDTO;
import io.swagger.model.Role;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest {
  //information for jwt token
  static final String TOKEN_PREFIX = "Bearer";

  static final String HEADER_STRING = "Authorization";

  //Admin information
  private String xAuthTokenAdmin;

  //User information
  private String xAuthTokenUser;

  @Autowired
  private MockMvc mockMvc;

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Before
  @DisplayName("Checks if the user is able to login (with admin account and also user account)")
  public void loginUsers() throws Exception {
    //get user with customer access
    JSONObject admin = new JSONObject();
    admin.put("username", "Admin");
    admin.put("password", "Admin");

    MvcResult result = this.mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_JSON).content(admin.toString()))
        .andExpect(status().isOk())
        .andReturn();

    //Save token and user id for performing tests
    String content = result.getResponse().getContentAsString();
    this.xAuthTokenAdmin = content.substring(10, content.indexOf('}') - 1);
    JSONObject adminUser = new JSONObject(result.getResponse().getContentAsString());

    //Get user with employee access
    JSONObject user = new JSONObject();
    user.put("username", "User");
    user.put("password", "User");

    result = this.mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_JSON).content(user.toString()))
        .andExpect(status().isOk())
        .andReturn();

    String userContent = result.getResponse().getContentAsString();
    this.xAuthTokenUser = userContent.substring(10, userContent.indexOf('}') - 1);
    user = new JSONObject(result.getResponse().getContentAsString());
  }

  @Test
  @DisplayName("Checks if the admin can get the admin user")
  public void getAdminWithRoleAdminBySearchTermShouldReturnOk() throws Exception {
    this.mockMvc.perform(get("/users/Admin")
        .header(HEADER_STRING, TOKEN_PREFIX + " " + this.xAuthTokenAdmin))
        .andExpect(jsonPath("$[0].id").value(2))
        .andExpect(jsonPath("$[0].accounts[0].iban").value("NL02INHO0000000002"))
        .andExpect(jsonPath("$[0].accounts[1].iban").value("NL03INHO0000000003"))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Checks if a user can get the admin user")
  public void getAdminWithRoleUserBySearchTermShouldReturnError() throws Exception {
    this.mockMvc.perform(get("/users/Admin")
        .header(HEADER_STRING, TOKEN_PREFIX + " " + this.xAuthTokenUser))
        .andExpect(jsonPath("message").value("Access is denied"))
        .andExpect(status().is(400));
  }


  @Test
  @DisplayName("Checks if the admin can get all users")
  public void getUsersShouldReturnOk() throws Exception {
    this.mockMvc.perform(get("/users")
        .header(HEADER_STRING, TOKEN_PREFIX + " " + this.xAuthTokenAdmin))
        .andExpect(jsonPath("$", hasSize(5)))
        .andExpect(jsonPath("$[0].accounts[0].iban").value("NL01INHO0000000001"))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Checks if the user of role admin is able to CREATE a user")
  public void creatingAUserShouldReturnOK() throws Exception {
    CreateUpdateUserDTO createUser = new CreateUpdateUserDTO();
    createUser.setFirstname("John");
    createUser.setCreateCurrentAccount(false);
    createUser.setCreateSavingsAccount(false);
    createUser.setIsActive(true);
    createUser.setEmail("John@Doe.nl");
    createUser.setFirstname("John");
    createUser.setLastname("Doe");
    createUser.setDayLimit(BigDecimal.valueOf(54321));
    createUser.setPhonenumber("06-34343434");
    createUser.setRoles(Collections.singletonList(Role.ROLE_USER));
    createUser.setTransactionLimit(BigDecimal.valueOf(3000));
    createUser.setUsername("JohnDeer");
    createUser.setPassword("Doe");

    this.mockMvc.perform(post("/users/user")
        .header(HEADER_STRING, TOKEN_PREFIX + " " + this.xAuthTokenAdmin)
        .content(asJsonString(createUser))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("email").value("John@Doe.nl"))
        .andExpect(jsonPath("username").value("JohnDeer"))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Checks if the user of role admin is able to UPDATE a user")
  public void updateAUserShouldReturnOK() throws Exception {
    CreateUpdateUserDTO updateUser = new CreateUpdateUserDTO();
    updateUser.setFirstname("Admin");
    updateUser.setCreateCurrentAccount(false);
    updateUser.setCreateSavingsAccount(false);
    updateUser.setIsActive(true);
    updateUser.setEmail("Admin@Admin.nl");
    updateUser.setFirstname("Admin");
    updateUser.setLastname("Admin");
    updateUser.setDayLimit(BigDecimal.valueOf(638476));
    updateUser.setPhonenumber("06-2342356");
    updateUser.setRoles(Collections.singletonList(Role.ROLE_ADMIN));
    updateUser.setTransactionLimit(BigDecimal.valueOf(3423));
    updateUser.setUsername("Admin");
    updateUser.setPassword("Admin");

    this.mockMvc.perform(put("/users/Admin")
        .header(HEADER_STRING, TOKEN_PREFIX + " " + this.xAuthTokenAdmin)
        .content(asJsonString(updateUser))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("email").value("Admin@Admin.nl"))
        .andExpect(jsonPath("accounts[0].iban").value("NL02INHO0000000002"))
        .andExpect(jsonPath("accounts[1].iban").value("NL03INHO0000000003"))
        .andExpect(status().isOk());
  }
}