# w3act

w3act is an annotation and curation tool for web archives

## How to install and use

### Requirements

To install you need:

* W3ACT sources 
* Play Framework
* PostgreSQL
* Java

### Download

| Version    | Size     | Tool                                                    | Link                 |
|------------|----------|---------------------------------------------------------|----------------------|
| v1.0       | 221 KB   | W3ACT source code                                       |[download-w3act]      |
| v2.2.0     | 108.1 MB | Play Framework                                          |[download-play]       |
| v9.3.1     | 51.6 MB  | PostgreSQL database                                     |[download-db]         |
| v1.6.0_33  | 178 MB   | Java Developers Kit (e.g. JDK 6)                        |[download-java]       |

### Install instructions

Please refer to the istallation instructions of associated tool.

## To use the tool:

### Open DOS window and run the following command:

play run

### Start browser and use URL: 

localhost:9000

## Testing

play test

## Documentation

Description of the domain object model and user flows can be find in [wiki]

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

[download-w3act]: https://github.com/ukwa/w3act/archive/master.zip
[download-play]: http://www.playframework.com/download
[download-db]: http://www.postgresql.org/download/
[download-java]: http://www.oracle.com/technetwork/java/javase/downloads/index.html
[build-status]: https://travis-ci.org/ukwa/w3act)](https://travis-ci.org/ukwa/w3act
[wiki]: https://github.com/ukwa/w3act/wiki
[eclipse]: http://eclipse.org/eclipse
