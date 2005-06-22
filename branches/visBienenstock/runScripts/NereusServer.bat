REM Startet den Server

REM Benoetigte Umgebungsvariablen (CONFIG_DIR, JAVA,SERVER_HOST,
REM BASIS_PFAD, DIST_DIR)
REM werden gesetzt
call setup.bat

REM Datei in der die Rechte fuer den Server vergeben werden.
set POLICY_CONF=server.policy

REM Die Main-Klasse ist
REM MAIN=nereus.simulator.MASIMServer
set MAIN=nereus.simulator.MASIMServer

REM die Parameter für die JVM
set JVMPAR=-Xms200m -Xmx200m -Djava.security.policy=%CONFIG_DIR%\%POLICY_CONF%
set JVMPAR=%JVMPAR% -Djava.rmi.server.hostname=%SERVER_HOST%

%JAVA% %JVMPAR% -cp %DIST_DIR%\Server.jar;%DIST_DIR%\BienenstockScenario.jar %MAIN% %SERVER_HOST% %BASIS_PFAD% %CONFIGFILE_SERVER% %SCENARIOS_DIR%
