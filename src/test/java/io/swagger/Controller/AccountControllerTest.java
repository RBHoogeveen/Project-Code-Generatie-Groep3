package io.swagger.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.DTO.CreateUpdateAccountDTO;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountControllerTest {

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
            .andExpect(jsonPath("token").exists())
            .andReturn();

    //Save token and user id for performing tests
    String content = result.getResponse().getContentAsString();
    this.xAuthTokenAdmin = content.substring(10, content.indexOf('}') - 1);
//    JSONObject adminUser = new JSONObject(result.getResponse().getContentAsString());

    //Get user with employee access
    JSONObject user = new JSONObject();
    user.put("username", "User");
    user.put("password", "User");

    result = this.mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON).content(user.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("token").exists())
            .andReturn();

    String userContent = result.getResponse().getContentAsString();
    this.xAuthTokenUser = userContent.substring(10, userContent.indexOf('}') - 1);
//    user = new JSONObject(result.getResponse().getContentAsString());
  }

  @Test
  @DisplayName("Checks if the admin can get the Admins accounts")
  public void getAccountsByUsernameShouldReturnOk() throws Exception {
    this.mockMvc.perform(get("/accounts/Admin")
            .header(HEADER_STRING, TOKEN_PREFIX + " " + this.xAuthTokenAdmin))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].iban").value("NL02INHO0000000002"))
            .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Checks if the user of role admin is able to CREATE an account")
  public void creatingAnAccountShouldReturnOK() throws Exception {
    CreateUpdateAccountDTO createAccount = new CreateUpdateAccountDTO();
    createAccount.setType(false);
    createAccount.setAbsoluteLimit(BigDecimal.ZERO);
    createAccount.setIsActive(true);
    createAccount.setBalance(BigDecimal.valueOf(1000));
    createAccount.setUsername("Test");

    this.mockMvc.perform(post("/accounts")
            .header(HEADER_STRING, TOKEN_PREFIX + " " + this.xAuthTokenAdmin)
            .content(asJsonString(createAccount))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("isActive").value("true"));
  }

  @Test
  @DisplayName("Checks if the user of role admin is able to UPDATE an account")
  public void updatingAnAccountAsAdminShouldReturnOK() throws Exception {
    CreateUpdateAccountDTO updateAccount = new CreateUpdateAccountDTO();
    updateAccount.setType(false);
    updateAccount.setAbsoluteLimit(BigDecimal.ZERO);
    updateAccount.setIsActive(false);
    updateAccount.setBalance(BigDecimal.valueOf(123));
    updateAccount.setUsername("Admin");

    this.mockMvc.perform(put("/accounts/NL02INHO0000000002")
            .header(HEADER_STRING, TOKEN_PREFIX + " " + this.xAuthTokenAdmin)
            .content(asJsonString(updateAccount))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("isActive").value("false"))
            .andExpect(jsonPath("iban").value("NL02INHO0000000002"))
            .andExpect(jsonPath("balance").value("123"))
            .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Checks if the user of role admin is able to UPDATE an account")
  public void updatingAnAccountAsUserShouldReturnError() throws Exception {
    CreateUpdateAccountDTO updateAccount = new CreateUpdateAccountDTO();
    updateAccount.setType(false);
    updateAccount.setAbsoluteLimit(BigDecimal.ZERO);
    updateAccount.setIsActive(false);
    updateAccount.setBalance(BigDecimal.valueOf(123));
    updateAccount.setUsername("User");

    this.mockMvc.perform(put("/accounts/NL02INHO0000000002")
            .header(HEADER_STRING, TOKEN_PREFIX + " " + this.xAuthTokenUser)
            .content(asJsonString(updateAccount))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("message").value("Access is denied"))
            .andExpect(status().is(400));
  }


}
