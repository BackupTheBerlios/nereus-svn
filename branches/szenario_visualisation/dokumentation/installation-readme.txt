Installation MASIM:

Server:
1. Kopie des Verzeichnisses source/server auf die Festplatte des Serverrechners
2. Kopie der Verzeichnisse runscripts/, config/ und libs/ auf die Festplatte
   des Servers
3. Anpassen der Datei runscripts/MASIMServer.sh
   - Ersetzen von "localhost" durch den Hostname des Servers
4. Anpassen der Permissions der Dartei MASIMServer.sh
5. Installation beendet.

Client:
1. Kopie der Verzeichnisse source/client, runscripts/, libs/ und config/auf die
   Festplatte des Clientrechners
2. Anpassen der Datei runscripts/MASIMClient.sh, es müssen folgende Daten 
   angepaßt werden:
   - Ersetzen von "localhost" durch den Hostname des Servers
3. Anpassen der Permissions der Dateien MASIMClient, clientWebserver   
4. Installation beendet.

Einfuegen von Szenarios:
Informationen zum einfügen von Szenarion entnehmen sie bitte der INSTALL.txt
unter source/scenarios/<Name des Szenarios>/INSTALL.txt

Zum starten:
1. Den MASIM-Server mit ./MASIMServer.sh auf dem Serverrechner starten
2. Den Client-Webserver mit ./clientWebserver.sh auf dem Clientrechner starten
3. Den MASIM-Client mit ./MASIMClient.sh auf dem Clientrechner starten.

Anlegen eines neuen Spiels:
1. "Neues Spiel" 
2. Szenario auswählen, !!WICHTIG!! nur das island-Szenario ist ausführbar, das
   fgml-Szenario dient nur dazu, dass noch das der Auswahlmechanismus gezeigt 
   wird. Die Anforderung der Szenario-Parameter funktioniert, beim fgml-Szenario
   nicht korrekt.
2. Die Parameter im GameTab (Spiel) ausfüllen und auf "Spiel anmelden" drücken.
   Wichtig ist, dass alle Parameter ausgefüllt sein müssen.
3. Die angegebene Anzahl an Agenten für das Spiel registrieren:
   1. "Agent registrieren" drücken, 
   2. Agentennamen eingeben
   3. Klasse des Agenten auswählen
   4. Die URL des Webservers angeben, auf dem die Clientklassen liegen, einfach 
      mit http://rechner-name:port/ !!WICHTIG!!
4. Wenn alle Agenten angemeldet sind, dann simulieren drücken. Das Spiel wird 
   dann simuliert.

