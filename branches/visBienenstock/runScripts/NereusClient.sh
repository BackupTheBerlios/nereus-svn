#!/bin/sh

# Startet den Client

# Benötigte Umgebungsvariablen (CONFIG_DIR, JAVA, DIST_DIR, BASIS_PFAD,
# SERVER_HOST) werden gesetzt
. ./setup.sh

# Datei in der die Rechte für den Client vergeben werden.
POLICY_CONF=client.policy

# die Parameter für die JVM
JVMPAR=" " 
JVMPAR="$JVMPAR -Djava.security.policy=$CONFIG_DIR/$POLICY_CONF"
JVMPAR="$JVMPAR -Djava.rmi.server.hostname=$SERVER_HOST"

# die Main-Klasse ist
# nereus.client.MASIMClient
# und wird als Main-Class Attribut im jar-Manifest definiert
MAIN="nereus.registrationgui.MASIMClient"

exec $JAVA $JVMPAR -cp $DIST_DIR/Client.jar:$DIST_DIR/BienenstockVis.jar $MAIN $SERVER_HOST $BASIS_PFAD $CONFIGFILE_CLIENT $AGENT_PATH

