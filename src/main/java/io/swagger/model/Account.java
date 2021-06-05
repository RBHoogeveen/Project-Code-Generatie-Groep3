package io.swagger.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Account
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-05-21T11:36:55.738Z")

@Entity
public class Account {
    @Id
    @JsonProperty("iban")
    private String iban = null;

    @JsonProperty("type")
    private Boolean type = null;

    @JsonProperty("balance")
    private BigDecimal balance = null;

    @ManyToOne
    @JsonProperty("user")
    private User user = null;

    @JsonProperty("absoluteLimit")
    private BigDecimal absoluteLimit = null;

    @JsonProperty("isactive")
    private Boolean isactive = null;

    public Account() {
    }

    public Account iban(String iban) {
        this.iban = iban;
        return this;
    }

    public Boolean getType() {
        return type;
    }


    /**
     * Get iban
     *
     * @return iban
     **/
    @ApiModelProperty(example = "NLxxINHO0xxxxxxxxx", value = "The accounts IBAN, serves as ID")


    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Account Iban(String iban) {
        this.iban = iban;
        return this;
    }

    /**
     * Get type
     *
     * @return type
     **/
    @ApiModelProperty(example = "true", value = "true for savings, false for current")


    public Boolean isType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public Account type(Boolean type) {
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

  public Account user(User user) {
      this.user = user;
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
        return isactive;
    }

    public void setIsActive(Boolean isactive) {
        this.isactive = isactive;
    }

    /**
     * Get user
     *
     * @return user
     **/
    @ApiModelProperty(value = "")

    @Valid

  
  public User getUser() {
      return user;
  }

  public void setUser(User user) {
      this.user = user;
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
        Account account = (Account) o;
        return Objects.equals(this.iban, account.iban) &&
                Objects.equals(this.type, account.type) &&
                Objects.equals(this.balance, account.balance) &&
                Objects.equals(this.isactive, account.isactive) &&
                Objects.equals(this.user, account.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iban, type, balance, isactive, user);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Account {\n");
        sb.append("    iban: ").append(toIndentedString(iban)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
        sb.append("    isactive: ").append(toIndentedString(isactive)).append("\n");
        sb.append("    user: ").append(toIndentedString(user)).append("\n");
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

