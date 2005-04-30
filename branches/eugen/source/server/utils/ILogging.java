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

/**
 * Das Interface definiert Methoden zum Logging von Textnachrichten in eine 
 * Datei.
 * 
 * @author Daniel Friedrich
 */
public interface ILogging {
	
	/**
	 * Loggt die Message message in das Logfile
	 * 
	 * @param message
	 */
	public void log(String message);
	
	/**
	 * Setzt den Pfad an dem das LogFile gespeichert werden soll.
	 * 
	 * @param String path - Pfad an dem das LogFile gespeichert werden soll.
	 */
	public void setLogFilePath(String path);
	
	/**
	 * Liefert den Pfad des Logfiles zurück.
	 * 
	 * @return String - Pfad des LogFiles
	 */
	public String getLogFilePath();
	
	/**
	 * Liefert das komplette Logfile zurück.
	 * 
	 * @return File - LogFile
	 */
	public File getLogFile();

}
