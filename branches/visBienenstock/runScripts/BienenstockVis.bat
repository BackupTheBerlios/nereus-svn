REM Startet die Visualisierung f�r das Bienenstock-Szenario.

REM Ben�tigte Umgebungsvariablen (IMAGE_PATH, CONFIG_DIR, DIST_DIR und
REM SERVER_HOST) werden gesetzt.
call setup.bat


REM Pfad zu Bildern des Szenarios Bienenstock
set SCENARIO_IMAGES=%IMAGE_DIR%\bienenstock\bilder\

REM Datei in der die Rechte f�r die Visualisierung vergeben werden.
set POLICY_CONF=client.policy

REM die Parameter f�r die JVM
set JVMPAR= -Djava.security.policy=%CONFIG_DIR%\%POLICY_CONF%

REM die Main-Klasse ist 
REM scenarios.bienenstock.visualisationgui.BienenstockVisualisierung
REM und wird als Main-Class Attribut im jar-Manifest definiert
REM MAIN="scenarios.bienenstock.visualisationgui.BienenstockVisualisierung"

echo "Als Parameter bitte Namen vom Spiel und Durchlaufnummer angeben"

%JAVA% %JVMPAR% -jar %DIST_DIR%\BienenstockVis.jar %SERVER_HOST% %SCENARIO_IMAGES% %1 %2
