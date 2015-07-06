1.0.3
-----
* Addition: Import and export of Collections via the API
* Addition: Consistency check for missing crawl permission data added
* Addition: Archivist role can now reset incorrect licence status
* Addition: Use a checkbox for whether or not to obey robots.txt 
* Change: Service logo and homepage showing WA livery
* Bug fix: Target amendments no longer cause the system to lose crawl permissions related to the target
* Bug fix: Licence, creation date and organisation information correctly reported in data exports
* Bug fix: Errant browser ‘back button’ behaviour now consistent
* Bug fix: Java code error when generating licence report fixed
* Bug fix: GeoIP database handles now cleared from server memory
* Bug fix: Additional seeds can now be added to existing targets
* Bug fix: ‘Archive This Now’ message made persistent across service restart
* Bug fix: Crawl start dates now required

1.0.2
-----
* Enhancement: Detection of duplicate URLs, particularly http and https version of same site
* Enhancement: Added testing of URLs
* Enhancement: External links now open in new browser window
* Enhancement: Postal address URL added to bulk importer
* Enhancement: HTTP update API implemented
* Enhancement: Mobile device layout improved
* Enhancement: Management of targets improved, target deletion possible but only as orphans
* Enhancement: Defunct accounts now markable as inactive/closed
* Enhancement: Validation of target records with no LD criteria met
* Enhancement: Bad URL entered now reports error message in UI
* Change: Search term no longer required on QA dashboard
* Change: Default date on target report
* Bug fix: Removed duplicate results in reports view
* Bug fix: Target end date now editable
* Bug fix: Corrected link to licence privacy statement
* Bug fix: Fixed editing of target schedule
* Bug fix: Target results export button
* Bug fix: Collections page shows Published status
* Bug fix: Licence form shows website URL
* Bug fix: Java crawl permission persistence error fixed
* Bug fix: Refused licence requests now displayed in refused list

1.0.1
-----
Enhancement/s:
* Creating new targets in bulk [#266](https://github.com/ukwa/w3act/issues/266)
* Presenting QA wayback content only to w3act authenticated users [#268](https://github.com/ukwa/w3act/issues/268)
* Added .wales and .cymru to TLD whitelist [#284](https://github.com/ukwa/w3act/issues/284)
* Improved presentation and visibility of links to Wayback and to the known Instances.

Bug fixes:
* Target exports occasionally produced Internal Server Error from postgres database [#282](https://github.com/ukwa/w3act/issues/282)
* Different browser behaviour when loading QA wayback content [#275](https://github.com/ukwa/w3act/issues/275)
* Wayback uses IA logo and 'home' link shows an error page [#277](https://github.com/ukwa/w3act/issues/277)
* Logging in using Chrome browser results in blank browser screen [#281](https://github.com/ukwa/w3act/issues/281)
* Removed unnecessary accounts and details from code and documentation [#279](https://github.com/ukwa/w3act/issues/279)
