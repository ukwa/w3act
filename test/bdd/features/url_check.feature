Feature: Check if there is an existing URL in the Database
  As a user
  I want to be able to check if there is an existing URL
  So that I can view the results

Scenario: Checking if it's there is an existing URL
  Given I have a URL of "https://www.bbc.co.uk/test1&query=1&terri=2/"
  #Given I have a URL of "https://www.bbc.co.uk/test1/"
  When I check to see if it exists in the DB
  Then I should see a "yes"


  Scenario Outline: Checking if it's there is an existing URL
    Given I have a URL of "<input>"
  	When I check to see if it exists in the DB
  	Then I should see a "<output>"
    Examples:
      | input 											| output  	| 
      | https://www.bbc.co.uk/test1&query=1&terri=2/   	| yes		| 
      | https://england.shelter.org.uk   				| yes    	| 
      