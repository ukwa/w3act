#!/bin/bash

PLAY_HOME=/cygdrive/c/play-2.2.1

if [ -z "$PLAY_HOME" ];then
    echo "PLAY_HOME NEEDS TO BE SET"
else
	REPO=$PLAY_HOME
	
	#CLASSPATH=$(find "${REPO}" -name *.jar -printf '%p:' | sed 's/:$//') 
	CLASSPATH=$(find "${REPO}" -name '*.jar' | xargs echo | tr ' ' ':')

	CLASSPATH=target/scala-2.10/classes:target/scala-2.10/classes_managed:.

	export CLASSPATH

	echo $CLASSPATH
	
	java uk.bl.export.WaybackExport
fi
