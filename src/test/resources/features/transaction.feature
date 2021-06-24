Feature: Transaction tests

  Scenario: Retrieve transactions of TYPE_TRANSACTION
    Given That I am logged in as Bank
    When I request transaction history of TYPE_TRANSACTION
    Then I get 2 transactions of TYPE_TRANSACTION

  Scenario: Post a new transaction
    When I post a new transaction
    Then I get status 201

  Scenario: Post a new deposit
    When I post a new deposit
    Then I get status 201

  Scenario: Post a new withdrawal
    When I post a new withdrawal
    Then I get status 201
