package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.Account;
import io.swagger.model.User;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Deposit
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-05-21T11:36:55.738Z")

@Entity
public class Deposit {

  @Id
  @GeneratedValue
  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("amount")
  private BigDecimal amount = null;

  @JsonProperty("date")
  private String date = null;

  @JsonProperty("type")
  private Boolean type = null;

  @ManyToOne
  @JsonProperty("userPerforming")
  private User userPerforming = null;

  @ManyToOne
  @JsonProperty("fromAccount")
  private Account fromAccount = null;

  @ManyToOne
  @JsonProperty("toAccount")
  private Account toAccount = null;

  @JsonProperty("transferType")
  private TransferType transferType = null;

  //empty constructor for the spring annotations
  public Deposit() {
  }

  public TransferType getTransferType() {
    return transferType;
  }

  public void setTransferType(TransferType transferType) {
    this.transferType = transferType;
  }

  public Deposit id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(value = "")


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Deposit amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
  **/
  @ApiModelProperty(value = "")

  @Valid

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public Deposit date(String date) {
    this.date = date;
    return this;
  }

  /**
   * Get date
   * @return date
  **/
  @ApiModelProperty(value = "")


  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public Deposit type(Boolean type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
  **/
  @ApiModelProperty(value = "")


  public Boolean isType() {
    return type;
  }

  public void setType(Boolean type) {
    this.type = type;
  }

  public Deposit userPerforming(User userPerforming) {
    this.userPerforming = userPerforming;
    return this;
  }

  /**
   * Get userPerforming
   * @return userPerforming
  **/
  @ApiModelProperty(value = "")

  @Valid

  public User getUserPerforming() {
    return userPerforming;
  }

  public void setUserPerforming(User userPerforming) {
    this.userPerforming = userPerforming;
  }

  public Deposit fromAccount(Account fromAccount) {
    this.fromAccount = fromAccount;
    return this;
  }

  /**
   * Get fromAccount
   * @return fromAccount
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Account getFromAccount() {
    return fromAccount;
  }

  public void setFromAccount(Account fromAccount) {
    this.fromAccount = fromAccount;
  }

  public Deposit toAccount(Account toAccount) {
    this.toAccount = toAccount;
    return this;
  }

  /**
   * Get toAccount
   * @return toAccount
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Account getToAccount() {
    return toAccount;
  }

  public void setToAccount(Account toAccount) {
    this.toAccount = toAccount;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Deposit deposit = (Deposit) o;
    return Objects.equals(this.id, deposit.id) &&
        Objects.equals(this.amount, deposit.amount) &&
        Objects.equals(this.date, deposit.date) &&
        Objects.equals(this.type, deposit.type) &&
        Objects.equals(this.userPerforming, deposit.userPerforming) &&
        Objects.equals(this.fromAccount, deposit.fromAccount) &&
        Objects.equals(this.toAccount, deposit.toAccount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, amount, date, type, userPerforming, fromAccount, toAccount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Deposit {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    userPerforming: ").append(toIndentedString(userPerforming)).append("\n");
    sb.append("    fromAccount: ").append(toIndentedString(fromAccount)).append("\n");
    sb.append("    toAccount: ").append(toIndentedString(toAccount)).append("\n");
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

