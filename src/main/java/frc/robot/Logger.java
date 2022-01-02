package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/*
*   This class logs .txt files to a usb stick.
*   The purpose of this is to use it for logging 
*   errors and other useful data from our FRC Robot.
*/
public class Logger 
{
    // TODO - find out what the flash drive directory is

    public static final int LOG_LEVEL_INFO = 0;
    public static final int LOG_LEVEL_ERROR = 1;
    public static final int LOG_LEVEL_NO_LOGS = 5000;

    private final int MAX_LOG_STATEMENT_NUMBER = 50;

    // Member Variables
    private Logger loggerInstance;

    private String fileName;
    private String logBuffer;
    private String directory; // find name
    
    private Timer timer;

    private int logLevel;
    private int counter;

    private Logger()
    {
        fileName = "logger.txt";
        logBuffer = "";
        directory = "/u";

        timer = new Timer();
        timer.reset();
        timer.start();

        logLevel = LOG_LEVEL_NO_LOGS;

        printLogHeader();

        counter = 0;
    }

    public Logger getInstance()
    {
        if (loggerInstance == null)
        {
            loggerInstance = new Logger();
        }
        return loggerInstance;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName + ".txt";
    }

    public void setLogLevel(int level)
    {
        logLevel = level;
    }

    public void setDirectory(String directory)
    {
        this.directory = directory;
    }

    public void logError(String title, String message)
    {
        if (logLevel <= LOG_LEVEL_ERROR)
        {
            logBuffer += format("ERROR", title, message) + "\n";

            flushIfRequired();
        }
    }

    public void logInfo(String title, String message)
    {
        if (logLevel <= LOG_LEVEL_INFO)
        {
            logBuffer += format("INFO", title, message) + "\n";

            flushIfRequired();
        }
    }

    public void flush()
    {
        try
        {
            FileWriter fw = new FileWriter(directory + "/" + fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(logBuffer);
            bw.close();
            logBuffer = "";
        }
        catch (IOException e)
        {

        }
    }

    private String format(String logLevel, String title, String message)
    {
        return Double.toString(timer.get()) + ": [" + logLevel + "] [" + title + "] - " + message;
    }

    private void printLogHeader()
    {
        try
        {
            FileWriter fw = new FileWriter(directory + "/" + fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("-------------------------------------------------------------------------------------------------");
            bw.newLine();
            bw.close();
        }
        catch (IOException e)
        {

        }
    }

    private void flushIfRequired()
    {
        counter++;

        if (counter >= MAX_LOG_STATEMENT_NUMBER)
        {
            flush();
            counter = 0;
        }
    }
}