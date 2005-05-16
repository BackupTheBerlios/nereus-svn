# Diese Datei dient zum Setzen der Umgebungsvariablen von Nereus
# in den anderen Scripten. Alle relativen Pfade beziehen sich auf das
# Verzeichnis runScripts.


# Der Pfad zum JDK. Wird standardmaesig als lokale Umgebungsvariable gesetzt.
# Kann hier aber auch explizit gesetzt werden.
# JAVA_HOME=/usr/java

# Der Pfad zur JVM.
if [ -n "$JAVA_HOME" ]; then
  JAVA="$JAVA_HOME/bin/java"
else
  JAVA="java"
fi

# Das Verzeichnis mit den policy-Dateien.
CONFIG_DIR="../config"

# Das Verzeichnis mit den jar-Dateien fuer die einzelnen Komponenten.
DIST_DIR="../dist"

# Das Verzeichnis in dem sich alle anderen Ordner (scenario, runSkripts, config)
# befinden.
BASIS_PFAD="../"

# Konfigurationsfile fuer den Client
CONFIGFILE_CLIENT="../config/clientconfig.xml"

# Konfigurationsfile fuer den Server
CONFIGFILE_SERVER="../config/serverconfig.xml"

# Verzeichnis in dem sich Szanrien befinden
SCENARIOS_DIR="../scenarios"

# Der Rechner, auf dem der Simulator von Nereus läuft.
SERVER_HOST="127.0.0.1"

# Der Port, unter dem der ClassFielServer lauft.
CLASS_SERVER_PORT="2003"

