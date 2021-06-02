package io.swagger.model.DTO;

public class ExceptionDTO {
  private String message;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public ExceptionDTO(String message) {
    this.message = message;
  }
}
