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

# Das Verzeichnis in dem sichder Ordner scenarios befindet.
BASIS_PFAD="../"

# Das Verzeichnis in dem die Klassen der Agenten befinden.
AGENT_PATH="../agents"

# Konfigurationsfile fuer den Client
CONFIGFILE_CLIENT="../config/clientconfig.xml"

# Konfigurationsfile fuer den Server
CONFIGFILE_SERVER="../config/serverconfig.xml"

# Verzeichnis in dem sich Szanrien befinden
SCENARIOS_DIR="../scenarios"

# Verzeichnis in dem sich die Verzeichnisse mit den Bildern der Szenrien
# befinden.
IMAGE_DIR="../scenariosconfig"

# Der Rechner, auf dem der Simulator von Nereus läuft, optional mit Port.
# SERVER_HOST="127.0.0.1:1099"
SERVER_HOST="127.0.0.1"

# Der Port, unter dem der ClassFielServer lauft.
CLASS_SERVER_PORT="2003"

