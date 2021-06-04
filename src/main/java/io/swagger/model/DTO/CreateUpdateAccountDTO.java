package io.swagger.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.Account;
import io.swagger.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@Schema(description = "Requestbody for creating or updating an account.")
@Validated

public class CreateUpdateAccountDTO {
  @JsonProperty("type")
  private Boolean type = null;

  @JsonProperty("balance")
  private BigDecimal balance = null;

  @JsonProperty("absoluteLimit")
  private BigDecimal absoluteLimit = null;

  @JsonProperty("isActive")
  private Boolean isActive = null;

  @JsonProperty("username")
  private String username = null;

  /**
   * Get type
   *
   * @return type
   **/
  @ApiModelProperty(example = "true", value = "true for savings, false for current")


  public Boolean getType() {
    return type;
  }

  public void setType(Boolean type) {
    this.type = type;
  }

  public CreateUpdateAccountDTO type(Boolean type) {
    this.type = type;
    return this;
  }

  /**
   * Get balance
   *
   * @return balance
   **/
  @ApiModelProperty(example = "€100.000,00", value = "The account balance")

  @Valid

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public CreateUpdateAccountDTO user(String username) {
    this.username = username;
    return this;
  }


  /**
   * Get isActive
   * @return isActive
   **/
  @ApiModelProperty(example = "true", value = "True if the user is active, false if not")
  @NotNull

  @Valid

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isactive) {
    this.isActive = isactive;
  }

  /**
   * Get user
   *
   * @return user
   **/
  @ApiModelProperty(value = "")

  @Valid


  public String getUsername() {
    return username;
  }

  public void setUsername(User user) {
    this.username = username;
  }

  /**
   * Get absoluteLimit
   * @return absoluteLimit
   **/
  @ApiModelProperty(example = "5000", value = "The users absolute spending limit")

  @Valid

  public BigDecimal getAbsoluteLimit() {
    return absoluteLimit;
  }

  public void setAbsoluteLimit(BigDecimal absoluteLimit) {
    this.absoluteLimit = absoluteLimit;
  }
  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateUpdateAccountDTO account = (CreateUpdateAccountDTO) o;
    return Objects.equals(this.type, account.type) &&
        Objects.equals(this.balance, account.balance) &&
        Objects.equals(this.absoluteLimit, account.absoluteLimit) &&
        Objects.equals(this.isActive, account.isActive) &&
        Objects.equals(this.username, account.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, balance, isActive, absoluteLimit, username);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Account {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
    sb.append("    isactive: ").append(toIndentedString(isActive)).append("\n");
    sb.append("    absoluteLimit: ").append(toIndentedString(absoluteLimit)).append("\n");
    sb.append("    user: ").append(toIndentedString(username)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
