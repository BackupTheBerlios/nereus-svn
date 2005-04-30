package exceptions;

/**
 * Wird ausgeloest, wenn ein Client eine neue Gruppe anmelden will und dabei
 * eine Agentenanzahl angibt, die entweder kleiner 1 ist, oder den in der
 * Szenarienbeschreibung angegebenen Bedingungen wiederspricht.<P>
 * 
 * @author Daniel Friedrich
 */
public class InvalidAgentNumberException extends Exception {

	private int intGivenNumber;

    //Konstruktoren:
    public InvalidAgentNumberException(){};

    public InvalidAgentNumberException(String strMessage, int intGivenNumber)
    {
	//!!! Dietmar, kannst du folgendes loesen:

	// Irgendwie den Bisherige Konstruktor mit strMessage aufrufen

	this.intGivenNumber = intGivenNumber;
    }

    public String getLocalizedMessage()
    {
		// if //Abfragen, ob ein Messagetext uebergeben wurde
		 //   { /* Diese Beschreibung ausgeben */ }
		//else
    	//{ /* Standarrdbeschreibung ausgeben */ }
		return this.getMessage();
    }
}
