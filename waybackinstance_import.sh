#!/bin/bash

PLAY_HOME=/opt/play/
JAVA_HOME=/opt/java

# Check envars
if [ -z "$PLAY_HOME" ];then
	echo "PLAY_HOME NEEDS TO BE SET"
	exit 1
fi

if [ ! -d "$JAVA_HOME" ]; then 
	echo "JAVA_HOME not defined or not directory. Exiting."
	exit 1  
fi      

# Collate jars
REPO=$PLAY_HOME
#CLASSPATH=$(find "${REPO}" -name *.jar -printf '%p:' | sed 's/:$//') 
CLASSPATH=$(find "${REPO}" -name '*.jar' | xargs echo | tr ' ' ':')

#CLASSPATH=${CLASSPATH}:./target/scala-2.10/classes:./conf:./target/scala-2.10/classes_managed:./target/scala-2.10/src_managed:.
CLASSPATH=${CLASSPATH}:/opt/w3act/target/scala-2.10/classes:/opt/w3act/conf:/opt/w3act/target/scala-2.10/classes_managed:/opt/w3act/target/scala-2.10/src_managed:.

export CLASSPATH
echo $CLASSPATH

${JAVA_HOME}/bin/java uk.bl.export.WaybackExport

