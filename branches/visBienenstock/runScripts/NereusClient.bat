call setup.bat

set MAIN=nereus.registrationgui.MASIMClient
set POLICY_CONF=client.policy

REM die Parameter für die JVM

set JVMPAR= -Djava.security.policy=%CONFIG_DIR%\%POLICY_CONF%
set JVMPAR=%JVMPAR% -Djava.rmi.server.hostname=%SERVER_HOST%

%JAVA% %JVMPAR% -cp %DIST_DIR%\Client.jar;%SCENARIOS_DIR%\Scenarios.jar %MAIN% %SERVER_HOST% %BASIS_PFAD% %CONFIGFILE_CLIENT%