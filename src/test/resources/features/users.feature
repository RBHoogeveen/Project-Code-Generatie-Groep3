Feature: User tests

# Login scenario
  Scenario: Login is status OK
    When i log in with username "Admin" and password "Admin"
    Then i get http code 200

  Scenario: Retrieve all users is status.OK
    When I retrieve all users
    Then I get http status 200

  Scenario: Retrieving all users returns list
    When i log in with username "Admin" and password "Admin"
    When I fetch all users
    Then I get http status 200

  Scenario: Retrieve users by searchTerm
    When I retrieve a user with searchTerm "Admin"
    Then I get http status 200

  Scenario: Create new user
    When i log in with username "Admin" and password "Admin"
    When I create a new user
    Then I get http status 200