package io.swagger.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;

/**
 * Transaction
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-05-21T11:36:55.738Z")

@Entity
public class Transaction implements Transfer{
    @Id
    @GeneratedValue
    @JsonProperty("id")
    private Long id = null;

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

    public TransferType getTransferType() {
        return transferType;
    }

    public void setTransferType(TransferType transferType) {
        this.transferType = transferType;
    }

    //empty constructor for the spring annotations
    public Transaction() {
    }

    //TODO onetomany etc ff nachecken
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

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Transaction date(String date) {
        this.date = date;
        return this;
    }

    /**
     * Get date
     *
     * @return date
     **/
    @ApiModelProperty(value = "")

    @Override
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Transaction type(Boolean type) {
        this.type = type;
        return this;
    }

    /**
     * Get type
     *
     * @return type
     **/
    @ApiModelProperty(value = "")


    public Boolean isType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
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

    @Override
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
                Objects.equals(this.type, transaction.type) &&
                Objects.equals(this.userPerforming, transaction.userPerforming) &&
                Objects.equals(this.fromAccount, transaction.fromAccount) &&
                Objects.equals(this.toAccount, transaction.toAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, date, type, userPerforming, fromAccount, toAccount);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Transaction {\n");

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

