#!/bin/sh

# Startet die Visualisierung f�r das Bienenstock-Szenario.


# Ben�tigte Umgebungsvariablen (IMAGE_PATH, CONFIG_DIR, DIST_DIR und
# SERVER_HOST) werden gesetzt.
. ./setup.sh


# Pfad zu Bildern des Szenarios Bienenstock
SCENARIO_IMAGES="$IMAGE_DIR/bienenstock/bilder"

# Datei in der die Rechte f�r die Visualisierung vergeben werden.
POLICY_CONF=client.policy

# die Parameter f�r die JVM
JVMPAR=" " 
JVMPAR="$JVMPAR -Djava.security.policy=$CONFIG_DIR/$POLICY_CONF"

# die Main-Klasse ist 
# scenarios.bienenstock.visualisationgui.BienenstockVisualisierung
# und wird als Main-Class Attribut im jar-Manifest definiert
# MAIN="scenarios.bienenstock.visualisationgui.BienenstockVisualisierung"

echo "Als Parameter bitte Namen vom Spiel und Durchlaufnummer angeben"

exec $JAVA $JVMPAR -jar $DIST_DIR/BienenstockVis.jar $SERVER_HOST $SCENARIO_IMAGES $@

