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
