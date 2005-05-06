/*
 * Created on 17.07.2003
 *
 * Part of the Diplomthesis with the title: 
 * 
 * "Identifikation von Lern- und Kommunikationsstrategie in einem Multiagenten-
 * Szenario."
 *
 * @author Daniel Friedrich
 * @copyright Institut für Intelligente Systeme, Universität Stuttgart (2003)
 */
package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Die Klasse stellt den Agenten und dem Szenario einen Logging-Mechanismus für 
 * das Loggen von Textzeilen in eine Datei zur Verfügung. 
 * 
 * @author Daniel Friedrich
 */
public class Logger implements ILogging {

	/**
	 * Pfad für das Logfile
	 */
	private String m_path;
		
	/**
	 * Name des Logfiles
	 */
	private String m_filename = "simulator.log";
	
	/**
	 * Writer zum Schreiben ins File
	 */
	private FileWriter m_logFileWriter;
	
	/**
	 * Filehandler
	 */
	private File m_logFile;
	/**
	 * Konstruktor
	 */
	public Logger() {
		super();
		/*
		 * Erzeuge das Logfile
		 */
		m_logFile = new File(m_path + "\\" + m_filename); 
		 
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param path
	 * @param filename
	 */
	public Logger(String path, String filename) {
		super();
		m_path = path;
		if((filename != null) && !(filename.equals(""))) {
			m_filename = filename;
		}
		/*
		 * Erzeuge das Logfile
		 */
		m_logFile = new File(m_path + "\\" + m_filename); 
		 
	}	

	/* (non-Javadoc)
	 * @see utils.ILogging#getLogFile()
	 */
	public File getLogFile() {
		return m_logFile;
	}

	/* (non-Javadoc)
	 * @see utils.ILogging#getLogFilePath()
	 */
	public String getLogFilePath() {
		return m_path;
	}

	/* (non-Javadoc)
	 * @see utils.ILogging#log(java.lang.String)
	 */
	public void log(String message) {
		try {		
			m_logFileWriter = new FileWriter(m_logFile);
			m_logFileWriter.write(message + "\n");
			m_logFileWriter.close();
		}catch(IOException ioe) {
			System.out.println(
				"Fehler beim loggin der der Message : " 
				+ message);
			ioe.printStackTrace(System.out);	
		}
	}

	/* (non-Javadoc)
	 * @see utils.ILogging#setLogFilePath(java.lang.String)
	 */
	public void setLogFilePath(String path) {
		m_path = path;
	}
}
