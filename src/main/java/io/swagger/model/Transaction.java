package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Transaction
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-05-21T11:36:55.738Z")

@Entity
public class Transaction {
  @Id
  @GeneratedValue
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("amount")
  private BigDecimal amount = null;

  @JsonProperty("date")
  private java.sql.Date date = null;

  @ManyToOne(fetch = FetchType.EAGER)
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
  public Transaction() {
  }

  public TransferType getTransferType() {
    return transferType;
  }

  public void setTransferType(TransferType transferType) {
    this.transferType = transferType;
  }

  public Transaction id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   *
   * @return id
   **/
  @ApiModelProperty(value = "")


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Transaction amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   *
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

  public Transaction date(java.sql.Date date) {
    this.date = date;
    return this;
  }

  /**
   * Get date
   *
   * @return date
   **/
  @ApiModelProperty(value = "")

  public java.sql.Date getDate() {
    return date;
  }

  public void setDate(java.sql.Date date) {
    this.date = date;
  }


  public Transaction userPerforming(User userPerforming) {
    this.userPerforming = userPerforming;
    return this;
  }

  /**
   * Get userPerforming
   *
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

  public Transaction fromAccount(Account fromAccount) {
    this.fromAccount = fromAccount;
    return this;
  }

  /**
   * Get fromAccount
   *
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

  public Transaction toAccount(Account toAccount) {
    this.toAccount = toAccount;
    return this;
  }

  /**
   * Get toAccount
   *
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
    Transaction transaction = (Transaction) o;
    return Objects.equals(this.id, transaction.id) &&
        Objects.equals(this.amount, transaction.amount) &&
        Objects.equals(this.date, transaction.date) &&
        Objects.equals(this.userPerforming, transaction.userPerforming) &&
        Objects.equals(this.fromAccount, transaction.fromAccount) &&
        Objects.equals(this.toAccount, transaction.toAccount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, amount, date, userPerforming, fromAccount, toAccount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Transaction {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
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

