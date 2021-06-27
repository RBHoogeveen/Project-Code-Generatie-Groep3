package io.swagger.model;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

class UserTest {

  @Test
  @WithMockUser(username = "Bank", password = "Bank", roles = "EMPLOYEE")
  public void getUserByUsernameShouldReturnOk() {

  }
}