@echo off

setLocal ENABLEDELAYEDEXPANSION

SET PLAY_HOME=

IF "%PLAY_HOME%"=="" GOTO USAGE 

SET REPO=%PLAY_HOME%\repository

if defined CLASSPATH (set CLASSPATH=%CLASSPATH%;.) else (set CLASSPATH=.)
FOR /R %REPO% %%G IN (*.jar) DO set CLASSPATH=!CLASSPATH!;%%G
echo The Classpath definition is %CLASSPATH%

cd target\scala-2.10\classes

java uk.bl.db.DataImport

GOTO END

:USAGE
	echo PLAY_HOME is not set
:END
endlocal

