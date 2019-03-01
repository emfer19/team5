package org.team5.app.main;

import org.team5.app.dataprocessing.CSVReader;
import org.team5.app.dataprocessing.DataPoint;

import java.util.LinkedList;
import java.util.Queue;

public class Buffer implements Runnable {

    private static final int MAX_BUFFER_SIZE = 100;
    private static final String FILE_NAME = "org/team5/app/data/" + "sampleMarketData.csv";
    public static Queue<DataPoint> buffer = new LinkedList<>();

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

        CSVReader reader = new CSVReader(FILE_NAME);
        int count = 0;
        for (int i = 0; i < reader.dataPoints.size(); i++) {

            //Chech Buffer size before adding org.team5.app.data points
            if (count < MAX_BUFFER_SIZE)
                buffer.add(reader.dataPoints.get(i));
            else
                break;

            count++;
        }

        System.out.println("Buffer Thread exiting");
    }

    public void checkSize() {
        System.out.println("Buffer size: " + buffer.size());
    }

    public static DataPoint remove() {
        return buffer.remove();
    }
}
