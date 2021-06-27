package io.swagger.model;

public enum TransferType {
  TYPE_TRANSACTION,
  TYPE_WITHDRAW,
  TYPE_DEPOSIT;

  @Override
  public String toString() {
    return name();
  }
}


