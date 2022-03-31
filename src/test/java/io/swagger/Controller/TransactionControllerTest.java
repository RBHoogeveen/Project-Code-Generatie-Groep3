package io.swagger.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.DTO.TransactionDTO;
import io.swagger.model.Transaction;
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
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TransactionControllerTest {
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
  @DisplayName("Checks if the admin can get its transaction history")
  public void getTransactionHistoryShouldReturnOK() throws Exception {
    this.mockMvc.perform(get("/transactions")
        .header(HEADER_STRING, TOKEN_PREFIX + " " + this.xAuthTokenUser))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Checks if the user of role admin is able to POST a transaction")
  public void postingATransactionShouldReturnOK() throws Exception {
    TransactionDTO transaction = new TransactionDTO();
    transaction.setAmount(BigDecimal.valueOf(500));
    transaction.setPerformerIban("NL02INHO0000000002");
    transaction.setTargetIban("NL04INHO0000000004");

    this.mockMvc.perform(post("/transactions/transaction")
        .header(HEADER_STRING, TOKEN_PREFIX + " " + this.xAuthTokenAdmin)
        .content(asJsonString(transaction))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Checks if the user of role admin is able to POST a deposit")
  public void postingADepositShouldReturnOK() throws Exception {
    TransactionDTO deposit = new TransactionDTO();
    deposit.setAmount(BigDecimal.valueOf(1000));
    deposit.setPerformerIban("NL02INHO0000000002");
    deposit.setTargetIban("NL03INHO0000000003");

    this.mockMvc.perform(post("/transactions/deposit")
        .header(HEADER_STRING, TOKEN_PREFIX + " " + this.xAuthTokenAdmin)
        .content(asJsonString(deposit))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Checks if the user of role admin is able to POST a withdrawal")
  public void postingAWithdrawalShouldReturnOK() throws Exception {
    TransactionDTO withdrawal = new TransactionDTO();
    withdrawal.setAmount(BigDecimal.valueOf(750));
    withdrawal.setPerformerIban("NL03INHO0000000003");
    withdrawal.setTargetIban("NL02INHO0000000002");

    this.mockMvc.perform(post("/transactions/withdrawal")
        .header(HEADER_STRING, TOKEN_PREFIX + " " + this.xAuthTokenAdmin)
        .content(asJsonString(withdrawal))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }
}
