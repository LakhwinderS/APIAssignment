Feature: Booking API

  Scenario: Create a booking with valid data
    Given I have a valid booking payload
    When I send the POST request to create the booking
    Then the response status code should be 200
    And the response should contain the created booking details

  Scenario: Create a booking with missing first name
    Given I have a booking payload with missing first name
    When I send the POST request to create the booking
    Then the response status code should be 400
    And the response should contain an error message

  Scenario: Create a booking with incorrect data type for price
    Given I have a booking payload with incorrect data type for price
    When I send the POST request to create the booking
    Then the response status code should be 400
    And the response should contain an error message

  Scenario: Create a booking with invalid dates
    Given I have a booking payload with invalid check-in and check-out dates
    When I send the POST request to create the booking
    Then the response status code should be 400
    And the response should contain an error message

  Scenario: Retrieve the booking using GET request
    Given I have a valid booking ID
    When I send the GET request to retrieve the booking
    Then the response status code should be 200
    And the response should contain the correct booking details
