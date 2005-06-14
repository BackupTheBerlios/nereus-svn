#!/bin/sh

# Startet den Server

# Benötigte Umgebungsvariablen (CONFIG_DIR, JAVA,SERVER_HOST, BASIS_PFAD,
# DIST_DIR) werden gesetzt
. ./setup.sh

# Datei in der die Rechte für den Server vergeben werden.
POLICY_CONF=server.policy

# die Parameter für die JVM
JVMPAR=" " 
JVMPAR="$JVMPAR -Djava.security.policy=$CONFIG_DIR/$POLICY_CONF"
JVMPAR="$JVMPAR -Djava.rmi.server.hostname=$SERVER_HOST"

# die Main-Klasse ist
# nereus.simulator.MASIMServer
# und wird als Main-Class Attribut im jar-Manifest definiert
MAIN="nereus.simulator.MASIMServer"

exec $JAVA $JVMPAR -cp $DIST_DIR/Server.jar:$DIST_DIR/BienenstockScenario.jar $MAIN $SERVER_HOST $BASIS_PFAD $CONFIGFILE_SERVER

