Feature: account tests

  Scenario: I get users accounts
    When I get accounts with "User"
    Then I get http status 200

  Scenario: I update an account
    When I put the account with iban "NL02INHO0000000002"
    Then I get http status 200

  Scenario: I create an account
    When I create an account
    Then I get http status 200