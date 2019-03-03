package org.team5.app.main;

import org.team5.app.dataprocessing.DataPoint;

public class DataProcessor {

    public static void processBufferData() {

        int count = 0;
        long sumProcessTime = 0;
        long sumMessageRates = 0;
        long sleepTime = 100; //in millisecond

        int size = Buffer.buffer.size();
        for (int i = 0; i < size; i++) {

            long startTime = System.nanoTime(); //Get nano time just before removing message data from buffer
            DataPoint dataPoint = Buffer.remove();
            int dataRate = dataPoint.getValue();
            sumMessageRates += dataRate;
            System.out.println("Message Rate: " + dataRate);
            //-------------------------------
            //Do some processing around this point, maybe
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //---------------------------------
            long timeNow = System.nanoTime(); //get current nano time after the processing above
            long estimatedTime = timeNow - startTime; //This gives time spent per message data

            //System.out.println("Time to process message[" + count + "] (ns): " + estimatedTime);
            sumProcessTime += estimatedTime;
            count++;
        }

        System.out.println();
        System.out.println("Total latency (ns): " + sumProcessTime);
        System.out.println("No of Messages: " + sumMessageRates);
        System.out.println("Average latency (ns): " + (double) sumProcessTime / sumMessageRates);
    }
}
