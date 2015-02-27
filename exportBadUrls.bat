@echo off

setLocal ENABLEDELAYEDEXPANSION

SET PLAY_HOME=c:\play-2.2.1

IF "%PLAY_HOME%"=="" GOTO USAGE 

SET REPO=%PLAY_HOME%

mkdir tmp

if defined CLASSPATH (set CLASSPATH=%CLASSPATH%;.) else (set CLASSPATH=.)

rem FOR /R %REPO% %%G IN (*.jar) DO set CLASSPATH=!CLASSPATH!;%%G
FOR /R %REPO% %%G IN (*.jar) DO xcopy /s/y %%G tmp

SET CLASSPATH=%CLASSPATH%;tmp\*;target\scala-2.10\classes

echo The Classpath definition is %CLASSPATH%

cd target\scala-2.10\classes

java -cp %CLASSPATH% uk.bl.export.ExportMalformedUrls

GOTO END

:USAGE
	echo PLAY_HOME is not set
:END
endlocal

