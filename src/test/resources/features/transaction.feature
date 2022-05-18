Feature: Transaction tests

  Scenario: Login and get transactions
    When i log in with username "Admin" and password "Admin"
    And get all transactions
    Then i get http code 404

  Scenario: Post a new transaction
    When i log in with username "Admin" and password "Admin"
    And I post a new transaction
    Then I get status 201

  Scenario: Post a new deposit
    When i log in with username "Admin" and password "Admin"
    And I post a new deposit
    Then I get status 201

  Scenario: Post a new withdrawal
    When i log in with username "Admin" and password "Admin"
    And I post a new withdrawal
    Then I get status 201
    And i get 1 transaction result
