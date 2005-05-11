/*
 * Dateiname      : Logger.java
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
