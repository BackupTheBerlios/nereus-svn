- Anleitung ConceptVerifier

  Dient dazu Konzepte zu verifizieren. Das Tool verwendet dazu das Prism-
  Lernverfahren. Einfach eine Datei mit vollst�ndigen Testdaten irgendwo ablegen
  und den ConceptVerifier mit dem Pfad der Datei aufrufen. Das Tool gibt dann in
  Form von Prism-Regeln dass Concept aus.
  
  Bsp: ConceptVerifier F:\concepts\isle300attributes_9er.arff
  
- Anleitung GMLToArffConverter
  
  Das Tool verwandelt eine GML-Beschreibung f�r eine Insel, d.h. einen Graphen
  mit beliebigem Verbindungsgrad, in eine Inselbeschreibung im ARFF-Format, wie
  sie das Inselszenario ben�tigt.
  
  Bsp: GmlToArffConverter /home/testperson/gml-file.gml /home/testperson/file.arff
  
- Anleitung Testsimulator

  Mit diesem Tool k�nnen komplette Simulationen lokal durchgef�hrt werden, ohne
  die Simulationsumgebung in Anspruchnehmen zum m�ssen. Der Simulator verwendet 
  die gleichen Klassen, wie die Simulationsumgebung, ist aber viel beschr�nkter 
  bez�glich der Resourcen.
  
  Testsimulator Island UnCooperativeAgent Id3 isle300 6 20 -Windows -Xms2m -Xmx512m


