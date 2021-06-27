Feature: Transaction tests

  Scenario: Retrieve transactions of TYPE_TRANSACTION by user User
    When I request transaction history of TYPE_TRANSACTION
    Then I get 1 transaction of TYPE_TRANSACTION

  Scenario: Post a new transaction
    When I post a new transaction
    Then I get status 201

  Scenario: Post a new deposit
    When I post a new deposit
    Then I get status 201

  Scenario: Post a new withdrawal
    When I post a new withdrawal
    Then I get status 201
