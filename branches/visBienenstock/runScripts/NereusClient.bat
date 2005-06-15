call setup.bat

REM Datei in der die Rechte für den Client vergeben werden.
set POLICY_CONF=client.policy

REM die Parameter für die JVM
set JVMPAR= -Djava.security.policy=%CONFIG_DIR%\%POLICY_CONF%
set JVMPAR=%JVMPAR% -Djava.rmi.server.hostname=%SERVER_HOST%

REM die Main-Klasse ist
REM nereus.client.MASIMClient
REM und wird als Main-Class Attribut im jar-Manifest definiert
set MAIN=nereus.registrationgui.MASIMClient

%JAVA% %JVMPAR% -cp %DIST_DIR%\Client.jar;%DIST_DIR%\BienenstockVis.jar %MAIN% %SERVER_HOST% %BASIS_PFAD% %CONFIGFILE_CLIENT% 
