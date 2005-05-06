/*
 * Dateiname      : ILogging.java
 * Erzeugt        : 17. Juli 2003
 * Letzte Änderung: 
 * Autoren        : Daniel Friedrich
 *                 
 *
 * Diese Datei gehört zum Projekt Nereus (http://nereus.berlios.de/).
 * Die erste Version dieser Datei wurde erstellt im Rahmen einer
 * Diplomarbeit von Daniel Friedrich am Institut für Intelligente Systeme
 * der Universität Stuttgart unter Betreuung von Dietmar Lippold
 * (dietmar.lippold@informatik.uni-stuttgart.de).
 *
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package nereus.utils;

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
