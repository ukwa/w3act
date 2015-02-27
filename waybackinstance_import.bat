@echo off

setLocal ENABLEDELAYEDEXPANSION

SET PLAY_HOME=c:\play-2.2.1

IF "%PLAY_HOME%"=="" GOTO USAGE 

java -cp ..\lib\* uk.bl.export.WaybackExport

GOTO END

:USAGE
	echo PLAY_HOME is not set
:END
endlocal

