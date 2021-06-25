Feature: User tests

  Scenario: Retrieve all users is status.OK
    When I retrieve all users
    Then I get http status 200

  Scenario: Retrieving all users returns list
    When I fetch all users
    Then I get a list of 3 users

  Scenario: Retrieve users by searchTerm
    When I retrieve a user with searchTerm "Admin"
    Then I get a dayLimit with 12345

  Scenario: Create new user
    When I create a new user
    Then I get http status 200