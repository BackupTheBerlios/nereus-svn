/*
 * Created on 04.08.2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package exceptions;

/**
 * Die Exception wird geworfen, wenn ein Platz schon von einem Agenten belegt 
 * ist und deshalb dort kein weiterer Agent gespeichert werden kann.
 * 
 * @author Daniel Friedrich
 */
public class FullPlaceException extends FullEnviromentException {
	
	private String m_message;
	/**
	 * Konstruktor.
	 */
	public FullPlaceException() {
		super();
		m_message = "Der Platz ist besetzt der Agent kann nicht mehr" 
			+ " hinzugefügt werden.";
	}

	/**
	 * Konstruktor.
	 * @param message
	 */
	public FullPlaceException(String message) {
		super();
		m_message = "Der Platz " 
			+ message 
			+ " ist besetzt der Agent kann nicht mehr" 
			+ " hinzugefügt werden.";
	}

	/**
	 * Konstruktor.
	 * @param message
	 * @param cause
	 */
	public FullPlaceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Konstruktor.
	 * @param cause
	 */
	public FullPlaceException(Throwable cause) {
		super(cause);
		m_message = "Der Platz ist besetzt der Agent kann nicht mehr" 
			+ " hinzugefügt werden.";
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		if(m_message != null) {
			return m_message;
		}
		return super.getMessage();
	}

}
