package org.team5.app.main;

import org.team5.app.dataprocessing.DataPoint;

public class DataProcessor {

    public static void processBufferData() {

        String line;
        int count = 0;
        long sumProcessTime = 0;

        int size = Buffer.buffer.size();
        for (int i = 0; i < size; i++) {

            long startTime = System.nanoTime();
            DataPoint dataPoint = Buffer.remove(); //Get and remove the next element on the buffer queue
            //Perform action with org.team5.app.data point

            long timeNow = System.nanoTime();
            long estimatedTime = timeNow - startTime;

            //System.out.println("Time to process message[" + count + "] (ns): " + estimatedTime);
            sumProcessTime += estimatedTime;
            count++;
        }

        System.out.println();
        System.out.println("Total Processing Time (ns): " + sumProcessTime);
        System.out.println("No of Messages: " + count);
        System.out.println("Average Processing Time (ns): " + (double) sumProcessTime / count);
    }
}
