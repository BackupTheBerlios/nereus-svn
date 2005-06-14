#!/bin/sh

# Startet den ClassFileServer, damit der Client die Agenten-Klassen
# an den Server uebergeben kann.

# Benötigte Umgebungsvariablen (CONFIG_DIR, JAVA, DIST_DIR,BASIS_PFAD,
# CLASS_SERVER_PORT) werden gesetzt
. ./setup.sh


# die Main-Klasse ist 
# nereus.webserver.ClassFileServer
# und wird als Main-Class Attribut im jar-Manifest definiert
MAIN="nereus.webserver.ClassFileServer"

exec $JAVA -cp $DIST_DIR/Client.jar $MAIN $CLASS_SERVER_PORT $AGENT_PATH

