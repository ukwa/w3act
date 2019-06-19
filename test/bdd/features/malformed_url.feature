Feature: Check if there is an existing URL is malformed
  As a user
  I want to be able to check if my url is of bad form
  So that I can view the results

Scenario: Checking if it's there my URL is malformed
  Given I have a malformed URL of "http://www.nicelife"
  When I check to see if it is malformed
  Then I should see a result of "no"
  # A good tip is to avoid putting anything about the outcome in the Given


  Scenario Outline: Checking if it's there my URL is malformed
    Given I have a malformed URL of "<input>"
  	When I check to see if it is malformed
  	Then I should see a result of "<output>"
    Examples:
      | input 						| output  	|
      | http://nicelife.co.uk   			| yes		|
      | https://www.nicelife.wales   			| yes		|
      | htt://www.nicelife   			| no		|
      | nicelife 					| no		|
      | http://www.nicelife   			| yes		|
      | https://www.bbc.co.uk/test1&query=1&terri=2   	| yes    	|
      | https://www.bbc.co.uk/test1&query=1&terri=2/   	| yes    	|
      | http://www.sample.com/xyz?abc=dkd&p=q&c=2   	| yes    	|
      | http://www.sample.com/xyz?abc=dkd&p=q&c=2/   	| yes    	|
      | https://www.bbc.co.uk/test1/   			| yes    	|
      | http://www.bbc.co.uk/test1			| yes    	|
      | htt://www.nicolaagain.co.uk			   | no    	|
      | https://mysticalmoments.shop/			| yes    	|
      | https://emshort.blog/				| yes    	|
      | https://worldrugbymuseum.blog/			| yes    	|
      | https://www.pissarro.art/			| yes    	|
      | https://lastposts.blog/				| yes    	|
      | https://webarchive.org.uk/en/ukwa/search?text=test&search_location=full_text&reset_filters=false&content_type=Web+Page				| yes    	|
      | https://webarchive.org.uk/en/ukwa/search?text=test/   	|yes    	|
      | http://example.com:3000/pathname?search=test   	| yes    	|
      | http://example.com:3000/pathname/?search=test#hash   	| yes    	|
      | http://example.com:3000   	| yes    	|