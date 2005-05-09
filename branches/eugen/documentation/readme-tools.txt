- Anleitung ConceptVerifier

  Dient dazu Konzepte zu verifizieren. Das Tool verwendet dazu das Prism-
  Lernverfahren. Einfach eine Datei mit vollständigen Testdaten irgendwo ablegen
  und den ConceptVerifier mit dem Pfad der Datei aufrufen. Das Tool gibt dann in
  Form von Prism-Regeln dass Concept aus.
  
  Bsp: ConceptVerifier F:\concepts\isle300attributes_9er.arff
  
- Anleitung GMLToArffConverter
  
  Das Tool verwandelt eine GML-Beschreibung für eine Insel, d.h. einen Graphen
  mit beliebigem Verbindungsgrad, in eine Inselbeschreibung im ARFF-Format, wie
  sie das Inselszenario benötigt.
  
  Bsp: GmlToArffConverter /home/testperson/gml-file.gml /home/testperson/file.arff
  
- Anleitung Testsimulator

  Mit diesem Tool können komplette Simulationen lokal durchgeführt werden, ohne
  die Simulationsumgebung in Anspruchnehmen zum müssen. Der Simulator verwendet 
  die gleichen Klassen, wie die Simulationsumgebung, ist aber viel beschränkter 
  bezüglich der Resourcen.
  
  Testsimulator Island UnCooperativeAgent Id3 isle300 6 20 -Windows -Xms2m -Xmx512m


