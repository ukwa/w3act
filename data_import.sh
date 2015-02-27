#!/bin/bash

JAVA_HOME=/opt/java

# Check envars
if [ ! -d "$JAVA_HOME" ]; then
    echo "JAVA_HOME not defined or not directory. Exiting."
    exit 1
fi

${JAVA_HOME}/bin/java -cp "/opt/w3act/lib/*" -Dconfig.file=conf/prod.conf java uk.bl.db.DataImport
