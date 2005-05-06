#!/bin/sh

# Startet den Server

# Benoetigte Umgebungsvariablen (CONFIG_DIR, JAVA,SERVER_HOST,
# BASIS_PFAD, DIST_DIR)
# werden gesetzt
. ./setup.sh

# Datei in der die Rechte fuer den Server vergeben werden.
POLICY_CONF=server.policy

# die Parameter fr die JVM
JVMPAR=" " 
JVMPAR="$JVMPAR -Djava.security.policy=$CONFIG_DIR/$POLICY_CONF"
JVMPAR="$JVMPAR -Djava.rmi.server.hostname=$SERVER_HOST"

# die Main-Klasse ist
# nereus.client.MASIMClient
# und wird als Main-Class Attribut im jar-Manifest definiert

exec $JAVA $JVMPAR -jar $DIST_DIR/Server.jar $SERVER_HOST $BASIS_PFAD

