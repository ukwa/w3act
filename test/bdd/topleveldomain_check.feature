Feature: Check top level domain
  As a user
  I want to be able to check if it's a top-level domain
  So that I can view the results

Scenario: Checking if it's a top level domain
  Given I have a Target domain of "http://www.ukbiologycompetitions.org/test.uk"
  When I check to see if it is a top-level domain
  Then I should see it is "no"
  
Scenario: Checking a Welsh top level domain passes
  Given I have a Welsh Target domain of "http://www.competitions.wales/test.uk"
  When I check to see if it passes a top-level domain
  Then I should see it has passed with a "yes"
  