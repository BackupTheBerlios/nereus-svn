@echo off

REM Diese Datei dient zum Setzen der Umgebungsvariablen von Nereus
REM in den anderen Scripten. Alle relativen Pfade beziehen sich auf das
REM Verzeichnis runScripts.


REM Der Pfad zum JDK. Wird standardmäßig als äußere Umgebungsvariable gesetzt.
REM Kann hier aber auch explizit gesetzt werden.
REM set JAVA_HOME=/usr/java

REM Der Pfad zur JVM (default java aus dem %PATH%)
set JAVA=java
if not "%JAVA_HOME%"=="" set JAVA=%JAVA_HOME%/bin/java

REM Das Verzeichnis mit den policy-, properties- und config-Dateien.
set CONFIG_DIR=..\config

REM Das Verzeichnis mit den jar-Dateien für die einzelnen Komponenten.
set DIST_DIR=..\dist

REM Das Verzeichnis in dem sich alle anderen Ordner (scenario, runSkripts, config)
REM befinden.
set BASIS_PFAD=..\

REM Konfigurationsfile fuer den Server
set CONFIGFILE_SERVER=..\config\serverconfig.xml

REM Konfigurationsfile fuer den Client
set CONFIGFILE_CLIENT=..\config\clientconfig.xml

REM Das Verzeichnis in dem sich scenario-Datein befinden
set SCENARIOS_DIR=..\scenarios

REM Der Rechner, auf dem der Simulator von Nereus läuft.
set SERVER_HOST=127.0.0.1
 

REM Der Port, unter dem der ClassFileServer läuft.
set CLASS_SERVER_PORT=2003

