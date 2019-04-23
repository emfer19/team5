package org.team5.app.dataprocessing;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.team5.app.main.IThreadIO;

/* Class: CSVReader
*   A class for parsing a csv file into DataPoint's from the Market Data Peaks
*   website. Use a string repersenting the path to the relevent csv file to
*   load the org.team5.app.data into memory and then call the next() function to return the
*   next dataPoint in time. 
*   @Param String filePath: Path the the relevent CSV file
*   @Author: Holden Duncan
*   @Created: Feb. 15, 2019
*   Resource Used: http://www.java67.com/2015/08/how-to-load-data-from-csv-file-in-java.html
*/
public class CSVReader implements IDataHandler, IThreadIO {

    private Path filePath;
    public ArrayList<DataPoint> dataPoints;
    private String delimeter = ","; // Seperates org.team5.app.data fields via comma by default

    private int current = 0;

    public CSVReader() {
    }

    public CSVReader(String fileName) {
        this.setFilePath(fileName);
        loadData();
    }

    public void setFilePath(String fileName) {
        this.filePath = Paths.get(fileName);
    }

    public void loadData() {
        this.dataPoints = new ArrayList<>();

        try (BufferedReader buffer = Files.newBufferedReader(this.filePath, StandardCharsets.US_ASCII)) {
            String line = buffer.readLine();

            while (line != null) {
                String[] variables = line.split(this.delimeter);

                // The following assumes the file structure is Datetime, MsgRate
                String timestamp = variables[0];
                int packetAmount = Integer.parseInt(variables[1]);

                // Parse the timestamp to get just the time as a variable
                String stampParts[] = timestamp.split(" ")[1].split(":"); // Hacky way to get the org.team5.app.data, but works for
                                                                          // now
                double hours = 60.0 * Double.parseDouble(stampParts[0]);
                double minutes = Double.parseDouble(stampParts[1]);
                double timeIn = hours + minutes;

                dataPoints.add(new DataPoint(timeIn, packetAmount));
                line = buffer.readLine();
            }

            // This values mark the end of the array list to be stored in the buffer.
            // This is going to be useful in ProcessingThread.java class to check for
            // the end of the buffer.
            dataPoints.add(new DataPoint(-1, -1));
        }

        catch (IOException ioe) {
            ioe.printStackTrace();
        }

        this.current = 0;
    }

    public int getDataSize()
    {
        return dataPoints.size();
    }

    public IDataObject next() {
        if (current < dataPoints.size()) {
            return (dataPoints.get(current++));
        } else {
            return (null);
        }
    }
    
    //Dummy function because this class should always be the source never the recipient
    //Should probally throw a helpful error
    public void in(DataPoint p){
        return NULL;
    }
    
    //Uses the given file path to generate rates and queue them
    public DataPoint out(){
        return this.next();
    }
    

    /*
     * A short main function that may be called to test a simple case by printing
     * some information for a sampled file
     */
    public static void main(String[] args) {
        //CSVReader reader = new CSVReader("./sampleMarketData.csv");
        //System.out.println(reader.next().asString());
    }
}
