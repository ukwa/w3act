Feature: Check if there is an existing URL is malformed
  As a user
  I want to be able to check if my url is of bad form
  So that I can view the results

Scenario: Checking if it's there my URL is malformed
  Given I have a malformed URL of "http://www.nicelife"
  When I check to see if it is malformed
  Then I should see a result of "yes"


  Scenario Outline: Checking if it's there my URL is malformed
    Given I have a malformed URL of "<input>"
  	When I check to see if it is malformed
  	Then I should see a result of "<output>"
    Examples:
      | input 							| output  	| 
      | http://nicelife.co.uk   		| yes		| 
      | https://www.nicelife.wales   	| yes		| 
      | htt://www.nicelife   			| no		| 
      | nicelife   						| no		| 
      | http://nicelife					| no		|
      | http://www.nicelife   			| no		| 
      