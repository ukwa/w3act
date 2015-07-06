Feature: List only the Targets for Collection Category
  As a user
  I want to be able to get a list for a target based on a collection category
  So that I can view the results

Scenario: Get me only the targets for a collection category
  Given I have a collection category ID of 600
  When I choose to see to get the targets
  Then I should see a target with the title "BBC Target title"
  And should only be 1 target returned

