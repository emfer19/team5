package org.team5.app.main;

import org.team5.app.dataprocessing.CSVReader;
import org.team5.app.dataprocessing.DataPoint;

import java.util.LinkedList;
import java.util.Queue;

public class Buffer implements Runnable {

    private static final int MAX_BUFFER_SIZE = 1000;
    public static Queue<DataPoint> buffer = new LinkedList<>();

    public String csvFilePath;

    public Buffer(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    /**
     * This thread reads the org.team5.app.data points into the buffer with a max size specified
     *
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        System.out.println("Buffer Thread running");

        CSVReader reader = new CSVReader(csvFilePath);
        int count = 0;
        for (int i = 0; i < reader.dataPoints.size(); i++) {

            //Check Buffer size before adding data points
            if (count < MAX_BUFFER_SIZE)
                buffer.add(reader.dataPoints.get(i));
            else
                break;

            count++;
        }

        checkSize();
        DataProcessor.processBufferData();
        System.out.println("Buffer Thread exiting");
    }

    public void checkSize() {
        System.out.println("Buffer size: " + buffer.size());
    }

    public static DataPoint remove() {
        return buffer.remove();
    }
}
