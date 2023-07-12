Feature: Happy Path Tests for the Reads Controller
  Scenario: Call the reads endpoint with a valid account number
    When the client calls endpoint "/api/smart/reads/" with account number 1 and credentials "user", "password"
    Then response status code is 200
    And returned read response is valid with account number 1, 5 gas readings and 3 elec readings

  Scenario: Call the reads endpoint with another valid account number
    When the client calls endpoint "/api/smart/reads/" with account number 2 and credentials "user", "password"
    Then response status code is 200
    And returned read response is valid with account number 2, 3 gas readings and 7 elec readings

  Scenario: Call the meter readings endpoint with a valid account number and valid body of Solus Elec
    Given the request body is
    """
{
  "meterReadings": [
    {
      "meterId": 1,
      "meterType": "ELEC",
      "reading": 10000,
      "date": "2023-07-11"
    }
  ]
}
"""
    When the client calls endpoint "/api/smart/reads/1/meter-readings" with valid body and credentials "user", "password"
    Then response status code is 201

  Scenario: Call the meter readings endpoint with a valid account number and valid body of Solus Gas
    Given the request body is
    """
{
  "meterReadings": [
    {
      "meterId": 2,
      "meterType": "GAS",
      "reading": 10000,
      "date": "2023-07-11"
    }
  ]
}
"""
    When the client calls endpoint "/api/smart/reads/1/meter-readings" with valid body and credentials "user", "password"
    Then response status code is 201

  Scenario: Call the meter readings endpoint with a valid account number and valid body of Multi-Meter
    Given the request body is
    """
{
  "meterReadings": [
    {
      "meterId": 1,
      "meterType": "ELEC",
      "reading": 10000,
      "date": "2023-07-11"
    },
    {
      "meterId": 2,
      "meterType": "GAS",
      "reading": 10000,
      "date": "2023-07-11"
    }
  ]
}
"""
    When the client calls endpoint "/api/smart/reads/1/meter-readings" with valid body and credentials "user", "password"
    Then response status code is 201

  Scenario: Call the meter readings endpoint with a valid account number and valid body of Multi-Meter and Multi Readings
    Given the request body is
    """
{
  "meterReadings": [
    {
      "meterId": 1,
      "meterType": "ELEC",
      "reading": 10000,
      "date": "2023-07-11"
    },
    {
      "meterId": 1,
      "meterType": "ELEC",
      "reading": 100000,
      "date": "2023-07-12"
    },
    {
      "meterId": 2,
      "meterType": "GAS",
      "reading": 10000,
      "date": "2023-07-11"
    },
    {
      "meterId": 2,
      "meterType": "GAS",
      "reading": 100000,
      "date": "2023-07-12"
    },
    {
      "meterId": 2,
      "meterType": "GAS",
      "reading": 1000000,
      "date": "2023-07-13"
    }
  ]
}
"""
    When the client calls endpoint "/api/smart/reads/1/meter-readings" with valid body and credentials "user", "password"
    Then response status code is 201