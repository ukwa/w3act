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
* Whois gem
* Maven

### Download

| Version    | Size     | Tool                                                    | Link                 |
|------------|----------|---------------------------------------------------------|----------------------|
| v1.0       | 221 KB   | W3ACT source code                                       |[download-w3act]      |
| v2.2.0     | 108.1 MB | Play Framework                                          |[download-play]       |
| v9.3.1     | 51.6 MB  | PostgreSQL database                                     |[download-db]         |
| v1.6.0_33  | 178 MB   | Java Developers Kit (e.g. JDK 6)                        |[download-java]       |
| v0.7.0     | 13.6 MB  | Maxmind GeoIP2 database                                 |[download-geoip]      |
| v1.7.9     | 12.9 MB  | Whois mapping between domain and country                |[download-whois]      |
| v3.1.1     | 2.8 MB   | Maven tool                                              |[download-maven]      |

### Install instructions

Please refer to the installation instructions of associated tool.

#### Whois
In order to install Whois lookup functionality:

Download JRuby JARs from [download-whois].
Extract ZIP and in folder jruby-1.7.9 execute:

gem install whois

in order to download whois gem for JRuby.

Then copy JRuby JARs to the "lib" folder of the project. We need jruby.jar and jruby-complete-1.7.9.jar.
Download [ukwa-whois] maven project. Compile it using command

mvn clean install

Create JAR package

mvn package

Copy generated project to the "lib" folder of the project. We will get a jruby-whois-3.4.2.2-SNAPSHOT.jar

## To use the application in production mode:

### Configuration details

The configuration file prod.conf for production should include necessary database entries for PostgreSQL or import them from application.conf:

#### For H2
db.default.driver=org.h2.Driver
db.default.url="jdbc:h2:mem:play;DB_CLOSE_DELAY=-1"

#### For PostgreSQL
db.default.driver=org.postgresql.Driver

To create database 'w3actprod' with user 'training'
db.default.url="postgres://training:(password)@127.0.0.1/w3actprod"

In order to add and activate Travis CI application profile please add a new configuration file: conf/travis-ci.conf
This file overrides the default application.conf database (PostgreSQL) with the H2 one.
Then edit .travis.yml to pass the new config to play, i.e. change this line:
script: play-${PLAY_VERSION}/play test
to this
script: play-${PLAY_VERSION}/play -Dconfig.file=conf/travis-ci.conf test

### Open terminal and execute the following command:

play clean stage

This command creates BAT file for Windows or SH file for Linux that can be started then. 

Note that if you want to use "play start" instead that could cause a problem with not killed PIDs if you close application. 
Also RUNNING_PID file will be created in root directory of the project that should be also removed then.

For the case you use application on Windows, in order to see processes you could use “tasklist” command. 
And for killing process with e.g. PID 1304 use “taskkill /pid 1304 /F” command.

### Execution steps for Linux
[RHEL installation] wiki describes exact commands with comments for deployment on Linux. 

## To use the application in development mode:

### Open DOS window and run the following command:

play run

### Start browser and use URL: 

localhost:9000/actdev/

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

In order to setup Java project with W3ACT sources use command:

play eclipse

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
[download-whois]: http://www.jruby.org/files/downloads/1.7.9/index.html
[RHEL installation]: https://github.com/ukwa/w3act/wiki/Installation-instructions
[ukwa-whois]: https://github.com/ukwa/jruby-whois/blob/master/src/main/java/uk/bl/wa/whois/JRubyWhois.java
[download-maven]: http://maven.apache.org/download.cgi

Help with submodules for W3ACT Source

```
$ git submodule init
$ git config -l
$ git submodule update

```

Using the API from Curl
-----------------------

Login, then download via the API:

    $ curl -c cookie.jar -i --data "email=user@example.org&password=PASS" https://localhost:9000/act/login
    $ curl -o 42.json -b cookie.jar https://localhost:9000/act/api/targets/42


