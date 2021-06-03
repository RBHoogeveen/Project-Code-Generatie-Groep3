package io.swagger.model.DTO;

import java.math.BigDecimal;

public class CreateUpdateAccountDTO {
  private Boolean type;
  private BigDecimal balance;
  private String username;
  private BigDecimal absoluteLimit;
  private Boolean isActive;

  public Boolean getType() {
    return type;
  }

  public void setType(Boolean type) {
    this.type = type;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public BigDecimal getAbsoluteLimit() {
    return absoluteLimit;
  }

  public void setAbsoluteLimit(BigDecimal absoluteLimit) {
    this.absoluteLimit = absoluteLimit;
  }

  public Boolean getActive() {
    return isActive;
  }

  public void setActive(Boolean active) {
    isActive = active;
  }
}
