package io.swagger.model;

import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;

/**
 * User
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-05-21T11:36:55.738Z")

@Entity
public class User   {
  @Id
  @GeneratedValue
  @JsonProperty("id")
  private Integer id = null;

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

  @JsonProperty("daySpent")
  private BigDecimal daySpent = null;

  @JsonProperty("transactionLimit")
  private BigDecimal transactionLimit = null;

  @ElementCollection(fetch = FetchType.EAGER)
  private List<Role> roles;

  @JsonProperty("isactive")
  private Boolean isactive = null;
  
  public User() {
  }

  public User id(Integer id) {
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

  public User username(String username) {
    this.username = username;
    return this;
  }

  /**
   * Get username
   * @return username
  **/
  @ApiModelProperty(example = "test", value = "The username for this user")

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public User firstname(String firstname) {
    this.firstname = firstname;
    return this;
  }

  /**
   * Get firstname
   * @return firstname
  **/
  @ApiModelProperty(example = "John", value = "The users firstname")

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public User lastname(String lastname) {
    this.lastname = lastname;
    return this;
  }

  /**
   * Get lastname
   * @return lastname
  **/
  @ApiModelProperty(example = "Doe", value = "The users lastname")

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public User email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
  **/
  @ApiModelProperty(example = "John@Doe.com", value = "The users email")

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public User phonenumber(String phonenumber) {
    this.phonenumber = phonenumber;
    return this;
  }

  /**
   * Get phonenumber
   * @return phonenumber
  **/
  @ApiModelProperty(example = "06 12345678", value = "The password for this user")

  public String getPhonenumber() {
    return phonenumber;
  }

  public void setPhonenumber(String phonenumber) {
    this.phonenumber = phonenumber;
  }

  public User password(String password) {
    this.password = password;
    return this;
  }

  /**
   * The password for this user
   * @return password
  **/
  @ApiModelProperty(example = "MySecretPassword", value = "The password for this user")

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

  @Valid

  public BigDecimal getDayLimit() {
    return dayLimit;
  }

  public void setDayLimit(BigDecimal dayLimit) {
    this.dayLimit = dayLimit;
  }

  public User transactionLimit(BigDecimal transactionLimit) {
    this.transactionLimit = transactionLimit;
    return this;
  }

  /**
   * Get daySpent
   * @return daySpent
   **/
  @ApiModelProperty(value = "")

  @Valid

  public BigDecimal getDaySpent() {
    return daySpent;
  }

  public void setDaySpent(BigDecimal daySpent) {
    this.daySpent = daySpent;
  }

  /**
   * Get isActive
   * @return isActive
   **/
  @ApiModelProperty(example = "true", value = "True if the user is active, false if not")

  @Valid

  public Boolean getIsActive() {
    return isactive;
  }

  public void setIsActive(Boolean isActive) {
    this.isactive = isActive;
  }

  /**
   * Get transactionLimit
   * @return transactionLimit
  **/
  @ApiModelProperty(example = "500", value = "Maximum amount that can be spend within one transaction")

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
    User user = (User) o;
    return Objects.equals(this.id, user.id) &&
        Objects.equals(this.username, user.username) &&
        Objects.equals(this.firstname, user.firstname) &&
        Objects.equals(this.lastname, user.lastname) &&
        Objects.equals(this.email, user.email) &&
        Objects.equals(this.phonenumber, user.phonenumber) &&
        Objects.equals(this.password, user.password) &&
        Objects.equals(this.dayLimit, user.dayLimit) &&
        Objects.equals(this.isactive, user.isactive) &&
        Objects.equals(this.transactionLimit, user.transactionLimit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, firstname, lastname, email, phonenumber, password, dayLimit, isactive, transactionLimit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class User {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    firstname: ").append(toIndentedString(firstname)).append("\n");
    sb.append("    lastname: ").append(toIndentedString(lastname)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    phonenumber: ").append(toIndentedString(phonenumber)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    dayLimit: ").append(toIndentedString(dayLimit)).append("\n");
    sb.append("    isactive: ").append(toIndentedString(isactive)).append("\n");
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

