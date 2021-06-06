Feature: login test

  Scenario: Successfully log in and get a JWT token
    When I successfully log in
    Then I get http status 200 and a JWT token