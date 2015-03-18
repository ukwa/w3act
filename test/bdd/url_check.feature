Feature: Check if there is an existing URL in the Database
  As a user
  I want to be able to check if there is an existing URL
  So that I can view the results

Scenario: Checking if it's there is an existing URL
  Given I have a URL of "https://www.ukbiologycompetitions.org/"
  When I check to see if it exists in the DB
  Then I should see a "yes"

