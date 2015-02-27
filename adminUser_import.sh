#!/bin/bash

PLAY_HOME=/opt/play/

if [ -z "$PLAY_HOME" ];then
    echo "PLAY_HOME NEEDS TO BE SET"
else
	REPO=$PLAY_HOME
	
	#CLASSPATH=$(find "${REPO}" -name *.jar -printf '%p:' | sed 's/:$//') 
	CLASSPATH=$(find "${REPO}" -name '*.jar' | xargs echo | tr ' ' ':')

	CLASSPATH=${CLASSPATH}:/opt/w3act/target/scala-2.10/classes:/opt/w3act/conf:/opt/w3act/target/scala-2.10/classes_managed:/opt/w3act/target/scala-2.10/src_managed:.
#	CLASSPATH=${CLASSPATH}:./target/scala-2.10/classes:./conf:./target/scala-2.10/classes_managed:./target/scala-2.10/src_managed:.

	export CLASSPATH

	echo $CLASSPATH
	
	#play compile

	echo "Creating account for $1 $2"
	
	java uk.bl.db.AdminUserImport $1 $2
fi
