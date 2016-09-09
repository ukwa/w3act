Feature: Exporting Targets
  As a user with a list of Targets on View
  I want to be able to export them into a format
  So that I can view the results

Scenario: Exporting Targets to CSV Format
  Given I have a list of Target result on view
  When I choose the export function
  Then I should see headings with "nid" "title" "field_url" "author" "field_crawl_frequency" "created"
  And data with "1" "The Archival Acid Test" "http://acid.matkelly.com/" "Bob" "DAILY" "2012-02-01T00:00:00Z"
