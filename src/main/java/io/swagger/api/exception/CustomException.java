package io.swagger.api.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends Throwable {
  public CustomException(String expired_or_invalid_jwt_token, HttpStatus internalServerError) {
  }
}
