/*
 * Created on 16.06.2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package scenario.islandenviroments;

import java.io.FileReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import utils.Id;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import exceptions.FullEnviromentException;
import exceptions.FullPlaceException;
import exceptions.InvalidAgentException;
import exceptions.InvalidElementException;

/**
 * Umwelt, d.h. die Insel im Insel-Szenario. Die Insel besteht aus einer Anzahl
 * von Plätzen, die zusammen einen Graphen bilden und auf denen sich die Agenten
 * bewegen, sowie Aktionen ausführen können.
 * 
 * @author Daniel Friedrich 
 */
public class IslandEnviroment {
	/**
	 * Hashtable mit den Plätzen des Graphs nach Ids sortiert.
	 */
	private Hashtable m_places = new Hashtable(); 
	
	/**
	 * hashtable mit den Ids der Plätze an dem sich die agenten gerade in der 
	 * Umwelt grade aufhalten.
	 */
	private Hashtable m_agentPlaces = new Hashtable();
	
	/**
	 * Menge der aktuell nicht von Agenten besetzen Plätze 
	 */
	private Vector m_emptyPlaces = new Vector();
	
	/**
	 * Anzahl der Plätze mit Energiezellen
	 */
	private int m_numPlacesWithEnergyCell = 0;
	
	/**
	 * Konstruktor
	 */
	public IslandEnviroment() {
		super();
	}

	/**
	 * Baut die Umwelt aus den beiden übergebenen Files auf.
	 * 
	 * @param graphFile - File im Arff-Format mit den Daten
	 * @param attributeFile
	 * @throws InvalidAttributeOrPlaceNumberException
	 */
	public Instances createEnviroment(String graphFile, String attributeFile) 
		throws InvalidElementException {
		try {
			System.out.println("Graphfile: " +graphFile);
			System.out.println("Attributefile: " + attributeFile);
			Instances graph = new Instances(new FileReader(graphFile));
			Instances pAttributes = new Instances(new FileReader(attributeFile));
			pAttributes.setClassIndex(pAttributes.numAttributes() - 1);
			if( graph.numInstances() == pAttributes.numInstances()) {
				// Plätze erzeugen und zwischenspeichern
				Place[] tmpPlaces = new Place[graph.numInstances()];
				Enumeration places = graph.enumerateInstances();
				Enumeration pEnum = pAttributes.enumerateInstances(); 
				int index = 0;
				while(places.hasMoreElements()) {
					Instance tmpInstance = (Instance)places.nextElement();
					Instance tmpAttributeValues = (Instance)pEnum.nextElement();	
					Hashtable attributes = 
						new Hashtable(tmpAttributeValues.numAttributes() * 2 );
					for(int i =0; i < tmpAttributeValues.numAttributes(); i++) {
						Attribute attr = tmpAttributeValues.attribute(i);
						attributes.put(attr.name(), attr.copy());						
					}
					tmpPlaces[index] = new Place(
						tmpAttributeValues, 
						(Hashtable)attributes.clone());	
					if(tmpPlaces[index].containsEnergyCell()) {
						m_numPlacesWithEnergyCell++;	
					}
					index++;						
				}
				// Wegenetz nachladen und an die Plätze überspeichern.
				places = graph.enumerateInstances();
				index = 0;
				while(places.hasMoreElements()) {
					Instance tmpInstance = (Instance)places.nextElement();
					Vector ways = new Vector();
					
					for(int i =0; i < tmpInstance.numValues(); i++) {
						String value = tmpInstance.stringValue(i);
						/*
						 * Wenn Wert == True, den Platz i als Verbindung
						 * nach index einfügen.
						 */
						if(value.equals("TRUE")) {
							ways.addElement(tmpPlaces[i].getId());
						}
					}
					if(ways.size() == 0) {
						System.out.println("Fehler in Wege-Platzmatrix bei Platz (" + index + " : ");
						System.out.println(tmpInstance.toString());
					}
					tmpPlaces[index].setNeighbourhood((Vector)ways);
					index++;
				}
				for(int i = 0; i < tmpPlaces.length; i++) {
					/*
					 * Platz der Liste der leeren Plätze und der Liste aller
					 * Plätze hinzufügen.
					 */ 
					m_emptyPlaces.addElement((Place)tmpPlaces[i]);
					m_places.put(((Place)tmpPlaces[i]).getId().toString(),tmpPlaces[i]);
				}
			}else {
				throw new InvalidElementException();
			}
			return new Instances(pAttributes);
		}catch(Exception e) {
			System.out.println("Problem");
			e.printStackTrace(System.out);
		}
		return null;
	}
	
	/**
	 * Gibt den gewünschten Platz zurück.
	 * 
	 * @param Id placeId - Id des Platzes
	 * @return Place - Platz mit der Id placeId
	 */
	public Place getPlace(Id placeId) 
		throws InvalidElementException {
		if(m_places.containsKey(placeId.toString())) {
			return (Place)m_places.get(placeId.toString());
		}else {
			throw new InvalidElementException(placeId.toString());
		}
	}
	
	/**
	 * Gibt den Platz zurück, an dem sich der Agent gerade befindet.
	 * 		
	 * @param agentId - Id des Agenten
	 * @return Place - Platz an dem sich der Agent mit der Id agentId befindet
	 * @throws InvalidAgentException
	 */
	public Place getPlaceForAgent(Id agentId) 
		throws InvalidAgentException {
			if(m_agentPlaces.containsKey(agentId.toString())) {
				Id placeId = (Id)m_agentPlaces.get(agentId.toString());
				if(m_places.containsKey(placeId.toString())) {
					return (Place)m_places.get(placeId.toString());
				}else {
					// Platz existiert nicht
					throw new InvalidAgentException();
				}
			}else {
				// Id des Agenten ist falsch.
				throw new InvalidAgentException();
			}
	}
	
	/**
	 * Bewegt einen Agenten vom Platz a zum Platz b in der Umwelt.
	 * 
	 * @param Id agentId - Id des Agenten der bewegt werden soll
	 * @param Id toPlaceId - Id des Zielortes
	 * 
	 * @throws InvalidAgentException
	 */
	public synchronized void moveAgentToPlace(Id agentId, Id toPlaceId) 
		throws InvalidAgentException, 
	InvalidElementException {
		Place place = null;
		Id placeId;	
		// Aktuellen standort ermitteln
		if(m_agentPlaces.containsKey(agentId.toString())) {
			placeId = (Id)m_agentPlaces.get(agentId.toString());
			if(m_places.containsKey(placeId.toString())) {
				place = (Place)m_places.get(placeId.toString());
			}else {
				// Platz existiert nicht
				throw new InvalidElementException(
					"Platz " 
					+ placeId.toString());
			}
		}else {
			// Id des Agenten ist falsch.
			throw new InvalidAgentException();
		}
		// dort removen
		place.removeAgent();
		// aus der Liste austragen
		m_agentPlaces.remove(agentId.toString());
		// Platz in die Liste der leeren Plätze eintragen
		m_emptyPlaces.addElement(place);
		// an neuen Standort speichern	
		Vector ways = place.getReachablePlaces();
		if(ways.contains(toPlaceId)) {
			Place toPlace = ((Place)m_places.get(toPlaceId.toString()));
			if(toPlace.isEmpty()) {
				toPlace.setAgent(agentId);
				// in Liste neu eintragen
				m_agentPlaces.put(agentId.toString(), toPlaceId);
				// Platz aus der Liste der leeren Plätze entfernen
				m_emptyPlaces.remove(toPlace);
			}else {
				throw new InvalidElementException(
					"Platz "
					+ placeId.toString(),
					new FullPlaceException(toPlaceId.toString()));
			}
		}else {
			throw new InvalidElementException(
				"Platz " + toPlaceId.toString());
		}
	}
	
	/**
	 * Nimmt einen Agenten aus der Umwelt.
	 * 
	 * @param Id agentId - Id des Agenten der aus der Umwelt genommen wird.
	 * @throws InvalidAgentException
	 */
	public void removeAgentFromEnviroment(Id agentId) 
		throws InvalidAgentException {
		Place place = null;	
		// Aktuellen standort ermitteln
		if(m_agentPlaces.containsKey(agentId.toString())) {
			Id placeId = (Id)m_agentPlaces.get(agentId.toString());
			if(m_places.containsKey(placeId.toString())) {
				place = (Place)m_places.get(placeId.toString());
			}else {
				/*
				 * Platz existiert nicht, werfe aber trotzdem eine 
				 * InvalidAgentException statt einer NoSuchElementException, da
				 * der Fehler zuvor entweder durch einen nicht existierenden 
				 * Agenten oder durch das fehlerhafte Verwenden eines 
				 * existierenden Agenten passierte.
				 */ 
				throw new InvalidAgentException();
			}
		}else {
			// Id des Agenten ist falsch.
			throw new InvalidAgentException();
		}
		// dort removen
		place.removeAgent();		
		// aus der Liste entfernen
		m_agentPlaces.remove(agentId.toString());	
		// Platz in die Liste der leeren Plätze aufnehmen
		m_emptyPlaces.addElement(place);
	}
	
	/**
	 * Bestimmt einen zufälligen Platz für den Agenten.
	 * 
	 * @param Id agentId - Id des neuen Agenten.
	 * @throws InvalidAgentException
	 */
	public void addAgentToEnviroment(Id agentId) 
		throws InvalidAgentException,
				FullEnviromentException {
		if(m_emptyPlaces.size() == 0) {
			throw new FullEnviromentException(agentId.toString());			
		}	
		// zufälligen Platz bestimmen
		long randomPlace = -1;
		while(randomPlace < 1) {
			randomPlace = Math.round(Math.random() * m_emptyPlaces.size());
		}
		Place place = (Place)m_emptyPlaces.get((new Long(randomPlace)).intValue()-1); 
		/*
		 * überprüfen, ob der Platz schon besetzt ist, wenn ja dann eine Fehler-
		 * meldung werfen, ansonsten den Agenten dem Platz hinzufügen.
		 */ 
		if(place.isEmpty()) {
			place.setAgent(agentId);
		}else {
			throw new FullEnviromentException(
				agentId.toString(),
				new InvalidElementException(
					"Der Platz " 
					+ place.getId().toString() 
					+ " ist bereits belegt mit einem Agenten."));
		}
		// Agent in Liste eintragen
		m_agentPlaces.put(agentId.toString(), place.getId());	
		// Platz aus der Liste der leeren Plätze entfernen
		m_emptyPlaces.remove(place);	
	}
	
	/**
	 * Setzt den Agenten auf den Platz mit der Id placeId.
	 * 
	 * @param Id agentId - Id des neuen Agenten.
	 * @param Id placeId - Id des Platzes an den Agent soll
	 * @throws InvalidAgentException
	 */
	public void addAgentToEnviroment(Id agentId, Id placeId) 
			throws InvalidAgentException, 
					InvalidElementException,
					FullEnviromentException {
		// Platz suchen
		Place place = this.getPlace(placeId);
		/*
		 * überprüfen, ob der Platz schon besetzt ist, wenn ja dann eine Fehler-
		 * meldung werfen, ansonsten den Agenten dem Platz hinzufügen.
		 */ 
		if(place.isEmpty()) {
			place.setAgent(agentId);
		}else {
			throw new FullEnviromentException(
				agentId.toString(),
				new InvalidElementException(
					"Der Platz " 
					+ place.getId().toString() 
					+ " ist bereits belegt mit einem Agenten."));
		}
		// Agent in Liste eintragen
		m_agentPlaces.put(agentId.toString(), place.getId());	
		// Platz aus der Liste der leeren Plätze entfernen
		m_emptyPlaces.remove(place);	
	}
	
	/**
	 * Gibt alle Agenten im Umkreis von distance Plätzen zurück.
	 * 
	 * Alle Agenten, die in distance-Schritten vom Platz mit der Id erreichbar
	 * sind werden zurückgegeben.
	 * 
	 * @param placeId
	 * @param distance
	 * @return Vector - Menge von Agenten, innhalb des Radius
	 * @throws InvalidElementException
	 */
	public Vector getNearestAgents(Id placeId, int distance) 
		throws InvalidElementException {
		 Vector retval = new Vector();		
		 Vector places = new Vector(); 
		 
		 
		 if(m_places.containsKey(placeId.toString())) {
		 	/*
		 	 * Sammele erst einmal alle Plätze ein, die innerhalb der Distanz 
		 	 * liegen.
		 	 */
		 	Place place = (Place)m_places.get(placeId.toString());
		 	places.addElement(place);
			Vector newPlaces = new Vector();
			newPlaces.addElement(place);
		 	for(int i=1; i < (distance + 1);i++) {
		 		Enumeration enum = newPlaces.elements();
				while(enum.hasMoreElements()) {
					Vector placesFound = new Vector();
					Place tmpPlace = (Place)enum.nextElement();
					Vector ways = tmpPlace.getReachablePlaces();
					Enumeration wayEnum = ways.elements();
					while(wayEnum.hasMoreElements()) {
						Place newPlace = (Place)wayEnum.nextElement(); 	
						if(!places.contains(newPlace)) {
							placesFound.addElement(newPlace);
							places.addElement(newPlace);	
						}
					}
					newPlaces = placesFound;
				}	
				
		 	}
		 	/*
		 	 * jetzt suche die Agenten raus.
		 	 */
		 	Enumeration foundedPlaces = places.elements();
		 	while(foundedPlaces.hasMoreElements()) {
		 		Place p = (Place)foundedPlaces.nextElement();
		 		if(!p.isEmpty()) {
		 			retval.addElement(p.getAgentId());
		 		}
		 	}
		 }else {
		 	throw new InvalidElementException(
				"Platz " 
		 		+ placeId.toString());
		 }		 
		 return retval;
	}
	
	/**
	 * Gibt alle Plätze zurück
	 * 
	 * @return Vector - Alle Plätze der Insel.
	 */
	public Vector getPlaces() {
		Vector retval = new Vector();
		
		Enumeration places = m_places.keys();
		while(places.hasMoreElements()) {
			String key = (String)places.nextElement();
			retval.addElement(m_places.get(key));
		}
		return retval;
	}
	
	/**
	 * Gibt die Anzahl der Plätze in der Umwelt zurück.
	 * 
	 * @return int - Anzahl der Plätze
	 */
	public int numPlaces() {
		return m_places.size();
	}
	
	/**
	 * Gibt die Anzahl der Plätze mit Energiezellen zurück.
	 * 
	 * Diese Methode ist nur zu statistischen Zwecken gedacht. Die Anzahl der
	 * Energiezellen ist dabei auch statisch, d.h. sie ändert sich durch 
	 * Grabungen nicht.
	 * 
	 * @return int - Anzahl der Plätze mit einer Energiezelle
	 */
	public int numPlacesWithEnergyCells() {
		return m_numPlacesWithEnergyCell;
	}
	
	/**
	 * Gibt die Anzahl der gefundenen Energiezellen zurück.
	 * 
	 * Diese Methode ist nur zu statistischen Zwecken gedacht.
	 *  
	 * @return int - Anzahl der gefundenen Energiezellen
	 */
	public int exploredEnergyCells() {
		int retval = 0;
		Enumeration places = m_places.elements();
		while(places.hasMoreElements()) {
			Place place = (Place)places.nextElement();
			if(place.containsEnergyCell()) {
				if(place.isExplored()) {
					retval++;
				}
			}
		}
		return retval;
	}	
}
