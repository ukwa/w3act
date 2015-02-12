#!/bin/bash

PLAY_HOME=/cygdrive/c/play-2.2.1/play

if [ -z "$PLAY_HOME" ];then
    echo "PLAY_HOME NEEDS TO BE SET"
else
	REPO=$PLAY_HOME
	
	#CLASSPATH=$(find "${REPO}" -name *.jar -printf '%p:' | sed 's/:$//') 
	CLASSPATH=$(find "${REPO}" -name '*.jar' | xargs echo | tr ' ' ':')

	CLASSPATH=${CLASSPATH}:./target/scala-2.10/classes:./conf;./target/scala-2.10/classes_managed:./target/scala-2.10/src_managed:.

	export CLASSPATH

	echo $CLASSPATH
	
	java uk.bl.export.ExportMalformedUrls
fi
