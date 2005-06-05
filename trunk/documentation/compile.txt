Zum compilieren der Distribution wir Ant benoetigt.
Zum Compilieren im Verzeichnis in dem build.xml sich befindet
einfach eingeben: ant allTar
daraufhin werden zwei Tar-Files im Ordner /dist abgelegt: Client.tar.gz 
und Server.tar.gz

Um neue Scenarien zu compilieren, müssen diese sich im Ordner:
source/scenario befinden.

Die Struktur eines Scenario muss wie folgt aussehen:
Verzeichnis mit scenario/<scenario-name>/agents  enthält Agenten fuer den Client,
wobei jeder lauffähige Agent mit "Agent" enden muss z.B: BienenstockAgent.java.
Die Scenario.java muss sich in dem Hauptverzeichnis des scenario-Ordner befinden:
d.h.: scenario/<scenario-name>/Scenario.java