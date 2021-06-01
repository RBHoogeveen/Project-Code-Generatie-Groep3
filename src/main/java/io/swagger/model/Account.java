package io.swagger.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

import org.iban4j.Iban;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;

/**
 * Account
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-05-21T11:36:55.738Z")

@Entity
public class Account {
    @Id
    @GeneratedValue
    private Long accountId;

    @JsonProperty("iban")
    private String iban = null;

    @JsonProperty("type")
    private Boolean type = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("balance")
    private BigDecimal balance = null;

    @ManyToOne
    @JsonProperty("user")
    private User user = null;

    @OneToOne
    @JsonProperty("transaction")
    private Transaction transaction = null;

    @OneToOne
    @JsonProperty("deposit")
    private Deposit deposit = null;

    @OneToOne
    @JsonProperty("withdrawal")
    private Withdrawal withdrawal = null;

    @JsonProperty("absoluteLimit")
    private BigDecimal absoluteLimit = null;


    public Account() {
    }

    public Account iban(String iban) {
        this.iban = iban;
        return this;
    }

    //TODO check nog ff of deze method niet in een andere class moet, user?
    //method to perform a transaction
    public Transaction MakeTransaction(BigDecimal amount, Account receiverAccount, Account performerAccount) {
        //prepare the transaction
        this.transaction = new Transaction();
        transaction.setAmount(amount);
        String timeOfTransaction = convertNowToString();
        transaction.setDate(timeOfTransaction);
        transaction.setUserPerforming(performerAccount.getUser());
        transaction.setFromAccount(performerAccount);
        transaction.setToAccount(receiverAccount);
        return transaction;
    }

    //method to perform a deposit
    public Deposit MakeDeposit(BigDecimal amount, Account receiverAccount, Account performerAccount) {
        //prepare the withdrawal
        this.deposit = new Deposit();
        deposit.setAmount(amount);
        String timeOfDeposit = convertNowToString();
        deposit.setDate(timeOfDeposit);
        deposit.setUserPerforming(performerAccount.getUser());
        deposit.setFromAccount(performerAccount);
        deposit.setToAccount(receiverAccount);
        return deposit;
    }

    //method to perform a withdrawal
    public Withdrawal MakeWithdrawal(BigDecimal amount, Account receiverAccount, Account performerAccount) {
        //prepare the withdrawal
        this.withdrawal = new Withdrawal();
        withdrawal.setAmount(amount);
        String timeOfWithdrawel = convertNowToString();
        withdrawal.setDate(timeOfWithdrawel);
        withdrawal.setUserPerforming(performerAccount.getUser());
        withdrawal.setFromAccount(performerAccount);
        withdrawal.setToAccount(receiverAccount);
        return withdrawal;
    }

    //method to convert now to string
    public String convertNowToString() {
        String pattern = "dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        Date now = Calendar.getInstance().getTime();
        String timeOfTransaction = df.format(now);
        return timeOfTransaction;
    }

    public Boolean getType() {
        return type;
    }


    /**
     * Get iban
     *
     * @return iban
     **/
    @ApiModelProperty(value = "")


    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Account type(Boolean type) {
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

    public Account name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get name
     *
     * @return name
     **/
    @ApiModelProperty(value = "")


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Account balance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    /**
     * Get balance
     *
     * @return balance
     **/
    @ApiModelProperty(value = "")

    @Valid

    public BigDecimal getBalance() {
        return balance;
    }


  @ManyToOne
  @JsonProperty("user")
  private User user = null;

  public Account iban(String iban) {
    this.iban = iban;
    return this;
  }

  public void setBalance(BigDecimal balance) {
      this.balance = balance;
  }

  public Account user(User user) {
      this.user = user;
      return this;
  }

    /**
     * Get user
     *
     * @return user
     **/
    @ApiModelProperty(value = "")

    @Valid

  public String getIban() {
    return iban;
  }

  public void setIban(String iban) {
    this.iban = iban;
  }
  
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
    @ApiModelProperty(value = "")

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
                Objects.equals(this.name, account.name) &&
                Objects.equals(this.balance, account.balance) &&
                Objects.equals(this.user, account.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iban, type, name, balance, user);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Account {\n");

        sb.append("    iban: ").append(toIndentedString(iban)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
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

