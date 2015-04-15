#!/bin/bash

JAVA_HOME=/usr

# Check envars
if [ ! -d "$JAVA_HOME" ]; then
    echo "JAVA_HOME not defined or not directory. Exiting."
    exit 1
fi

${JAVA_HOME}/bin/java -cp "./lib/*" -Dconfig.file=conf/application.conf uk.bl.export.UpdateFieldUrlDomains
