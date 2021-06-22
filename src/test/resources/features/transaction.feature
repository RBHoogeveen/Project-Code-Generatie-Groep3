Feature: Transaction tests

  Scenario: Retrieve transactions of TYPE_TRANSACTION
    When I request transaction history of TYPE_TRANSACTION
    Then I get 2 transactions of TYPE_TRANSACTION
