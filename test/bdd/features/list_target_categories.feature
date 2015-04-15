Feature: List only the Collection Categories for a Target
  As a user
  I want to be able to list only the collection categories for a target
  So that I can view the results

Scenario: Get me only the collection categories
  Given I have a target ID of 1
  When I choose to see the collection categories
  Then I should see a collection category with the name "UK General Election 2015"
  And should only be 1 category returned

