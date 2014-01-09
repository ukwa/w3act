# w3act

w3act is an annotation and curation tool for web archives

## How to install and use

### Requirements

To install you need:

* W3ACT sources 
* Play Framework
* PostgreSQL
* Java
* Maxmind GeoIP2 database

### Download

| Version    | Size     | Tool                                                    | Link                 |
|------------|----------|---------------------------------------------------------|----------------------|
| v1.0       | 221 KB   | W3ACT source code                                       |[download-w3act]      |
| v2.2.0     | 108.1 MB | Play Framework                                          |[download-play]       |
| v9.3.1     | 51.6 MB  | PostgreSQL database                                     |[download-db]         |
| v1.6.0_33  | 178 MB   | Java Developers Kit (e.g. JDK 6)                        |[download-java]       |
| v0.7.0     | 13.6 MB  | Maxmind GeoIP2 database                                 |[download-geoip]      |

### Install instructions

Please refer to the istallation instructions of associated tool.

## To use the application in production mode:

### Configuration details

The configuration file prod.conf for production should include necessary database entries for PostgreSQL or import them from application.conf:

db.default.driver=org.postgresql.Driver
# created database 'w3actprod' with user 'training' and password 'training'
db.default.url="postgres://training:training@127.0.0.1/w3actprod"

### Open terminal and execute the following command:

play clean stage

This command creates BAT file for Windows or SH file for Linux that can be started then. 

Note that if you want to use "play start" instead that could cause a problem with not killed PIDs if you close application. 
Also RUNNING_PID file will be created in root directory of the project that should be also removed then.

For the case you use application on Windows, in order to see processes you could use “tasklist” command. 
And for killing process with e.g. PID 1304 use “taskkill /pid 1304 /F” command.

### Execution steps for Linux
TBD

## To use the application in development mode:

### Open DOS window and run the following command:

play run

### Start browser and use URL: 

localhost:9000

## Testing

play test

## Documentation

Description of the domain object model and user flows can be find in [wiki]

Initial permissions and roles definition according to the requirements document is in initial-data.yml

### Develop

Build status is supported by Travis [build-status]

## Requirements

To build you require:

* Git client
* Java Developers Kit (e.g. JDK 6)
* Play Framework
* PostgreSQL database

For using the recommended IDE you require:

* Eclipse Kepler Service Release 1 with m2eclipse plugin [eclipse]

### Troubleshooting

**Getting SQL errors in browser**

To solve this problem adapt paths in clean up script according to your installation and execute it:

cleanup.bat

sometimes helps also:

play clean

or simply manually delete all "target" folders in your project

[download-w3act]: https://github.com/ukwa/w3act/archive/master.zip
[download-play]: http://www.playframework.com/download
[download-db]: http://www.postgresql.org/download/
[download-java]: http://www.oracle.com/technetwork/java/javase/downloads/index.html
[download-geoip]: http://dev.maxmind.com/geoip/geoip2/downloadable/#MaxMind_APIs
[build-status]: https://travis-ci.org/ukwa/w3act)](https://travis-ci.org/ukwa/w3act
[wiki]: https://github.com/ukwa/w3act/wiki
[eclipse]: http://eclipse.org/eclipse
