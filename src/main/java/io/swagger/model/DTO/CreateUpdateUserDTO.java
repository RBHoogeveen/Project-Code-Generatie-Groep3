package io.swagger.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.Role;
import io.swagger.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Schema(description = "Requestbody for creating or updating a user.")
@Validated

public class CreateUpdateUserDTO {
  @JsonProperty("username")
  private String username = null;

  @JsonProperty("firstname")
  private String firstname = null;

  @JsonProperty("lastname")
  private String lastname = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("phonenumber")
  private String phonenumber = null;

  @JsonProperty("password")
  private String password = null;

  @JsonProperty("dayLimit")
  private BigDecimal dayLimit = null;

  @JsonProperty("transactionLimit")
  private BigDecimal transactionLimit = null;

  @ElementCollection(fetch = FetchType.EAGER)
  private List<Role> roles;

  @JsonProperty("isActive")
  private Boolean isActive = null;

  private Boolean createSavingsAccount;
  private Boolean createCurrentAccount;

  public Boolean getCreateSavingsAccount() {
    return createSavingsAccount;
  }

  public void setCreateSavingsAccount(Boolean createSavingsAccount) {
    this.createSavingsAccount = createSavingsAccount;
  }

  public Boolean getCreateCurrentAccount() {
    return createCurrentAccount;
  }

  public void setCreateCurrentAccount(Boolean createCurrentAccount) {
    this.createCurrentAccount = createCurrentAccount;
  }

  public CreateUpdateUserDTO username(String username) {
    this.username = username;
    return this;
  }


  /**
   * Get username
   * @return username
   **/
  @ApiModelProperty(example = "test", value = "The username for this user")
    @NotNull

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public CreateUpdateUserDTO firstname(String firstname) {
    this.firstname = firstname;
    return this;
  }

  /**
   * Get firstname
   * @return firstname
   **/
  @ApiModelProperty(example = "John", value = "The users firstname")
    @NotNull

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public CreateUpdateUserDTO lastname(String lastname) {
    this.lastname = lastname;
    return this;
  }

  /**
   * Get lastname
   * @return lastname
   **/
  @ApiModelProperty(example = "Doe", value = "The users lastname")
    @NotNull

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public CreateUpdateUserDTO email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   **/
  @ApiModelProperty(example = "John@Doe.com", value = "The users email")
    @NotNull

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public CreateUpdateUserDTO phonenumber(String phonenumber) {
    this.phonenumber = phonenumber;
    return this;
  }

  /**
   * Get phonenumber
   * @return phonenumber
   **/
  @ApiModelProperty(example = "06 12345678", value = "The password for this user")
    @NotNull

  public String getPhonenumber() {
    return phonenumber;
  }

  public void setPhonenumber(String phonenumber) {
    this.phonenumber = phonenumber;
  }

  public CreateUpdateUserDTO password(String password) {
    this.password = password;
    return this;
  }

  /**
   * The password for this user
   * @return password
   **/
  @ApiModelProperty(example = "MySecretPassword", value = "The password for this user")
    @NotNull

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Get dayLimit
   * @return dayLimit
   **/
  @ApiModelProperty(example = "1000", value = "The users daily spending limit")
    @NotNull

  @Valid

  public BigDecimal getDayLimit() {
    return dayLimit;
  }

  public void setDayLimit(BigDecimal dayLimit) {
    this.dayLimit = dayLimit;
  }

  public CreateUpdateUserDTO transactionLimit(BigDecimal transactionLimit) {
    this.transactionLimit = transactionLimit;
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

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  /**
   * Get transactionLimit
   * @return transactionLimit
   **/
  @ApiModelProperty(example = "500", value = "Maximum amount that can be spend within one transaction")
    @NotNull
  @Valid

  public BigDecimal getTransactionLimit() {
    return transactionLimit;
  }

  public void setTransactionLimit(BigDecimal transactionLimit) {
    this.transactionLimit = transactionLimit;
  }

  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateUpdateUserDTO user = (CreateUpdateUserDTO) o;
    return Objects.equals(this.username, user.username) &&
        Objects.equals(this.firstname, user.firstname) &&
        Objects.equals(this.lastname, user.lastname) &&
        Objects.equals(this.email, user.email) &&
        Objects.equals(this.phonenumber, user.phonenumber) &&
        Objects.equals(this.password, user.password) &&
        Objects.equals(this.dayLimit, user.dayLimit) &&
        Objects.equals(this.isActive, user.isActive) &&
        Objects.equals(this.transactionLimit, user.transactionLimit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, firstname, lastname, email, phonenumber, password, dayLimit, isActive, transactionLimit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateUpdateUserDTO {\n");

    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    firstname: ").append(toIndentedString(firstname)).append("\n");
    sb.append("    lastname: ").append(toIndentedString(lastname)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    phonenumber: ").append(toIndentedString(phonenumber)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    dayLimit: ").append(toIndentedString(dayLimit)).append("\n");
    sb.append("    isActive: ").append(toIndentedString(isActive)).append("\n");
    sb.append("    transactionLimit: ").append(toIndentedString(transactionLimit)).append("\n");
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
