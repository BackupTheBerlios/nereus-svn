call setup.bat 
set MAIN=nereus.webserver.ClassFileServer 
%JAVA% %JVMPAR% -cp %DIST_DIR%\Client.jar %MAIN% %CLASS_SERVER_PORT% %BASIS_PFAD%
