Feature: Unhappy Path Tests for the Reads Controller
  Scenario: Call the reads endpoint with an invalid account number
    When the client calls endpoint "/api/smart/reads/" with account number 999 and credentials "user", "password"
    Then response status code is 200
    And returned read response is valid with account number 999, 0 gas readings and 0 elec readings

  Scenario: Calls an endpoint that does not exist
    When the client calls endpoint "/api/smart/reads/blah" with credentials "user", "password"
    Then response status code is 400
    And response body contains "Bad Request"
    
  Scenario: Calls the reads endpoint with valid account number and valid body but fails validation on smaller reading
    Given the request body is
    """
{
  "meterReadings": [
    {
      "meterId": 1,
      "meterType": "ELEC",
      "reading": 10,
      "date": "2023-07-11"
    }
  ]
}
    """
    When the client calls endpoint "/api/smart/reads/1/meter-readings" with valid body and credentials "user", "password"
    Then response status code is 400
    And response body contains "is less than a previous reading"

  Scenario: Calls the reads endpoint with valid account number and valid body but fails validation on unknown meter type
    Given the request body is
    """
{
  "meterReadings": [
    {
      "meterId": 1,
      "meterType": "Blah",
      "reading": 10000,
      "date": "2023-07-11"
    }
  ]
}
    """
    When the client calls endpoint "/api/smart/reads/1/meter-readings" with valid body and credentials "user", "password"
    Then response status code is 400
    And response body contains "Bad Request"

  Scenario: Calls the reads endpoint with valid account number and valid body but fails validation on duplicate entry
    Given the request body is
        """
{
  "meterReadings": [
    {
      "meterId": 1,
      "meterType": "GAS",
      "reading": 10000,
      "date": "2023-07-11"
    },
    {
      "meterId": 1,
      "meterType": "GAS",
      "reading": 10000,
      "date": "2023-07-11"
    }
  ]
}
    """
    When the client calls endpoint "/api/smart/reads/1/meter-readings" with valid body and credentials "user", "password"
    Then response status code is 400
    And response body contains "is a duplicate"

  Scenario: Calls the reads endpoint with valid account number and valid body but wrong credentials
    Given the request body is
        """
        """
    When the client calls endpoint "/api/smart/reads/1/meter-readings" with valid body and credentials "user", "wrongpassword"
    Then response status code is 401