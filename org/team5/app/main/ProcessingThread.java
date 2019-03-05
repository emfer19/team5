package org.team5.app.main;

import org.team5.app.dataprocessing.DataPoint;
import org.team5.app.gui.SwingUI;

import java.util.concurrent.BlockingQueue;

public class ProcessingThread implements Runnable {

    public BlockingQueue<DataPoint> buffer;

    public ProcessingThread(BlockingQueue<DataPoint> buffer) {
        this.buffer = buffer;
    }

    /**
     * This thread fetches the message rates from the blocking queue buffer.
     * A BlockingQueue is a queue that additionally supports operations that wait for
     * the queue to become non-empty when retrieving an element, and wait for space to become available
     * in the queue when storing an element.
     * <p>
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     */
    @Override
    public void run() {

        SwingUI.uploadButton.setEnabled(false);
        SwingUI.textArea.setText(null);

        long sumProcessTime = 0;
        long sumMessageRates = 0;
        long sleepTime = 150; //in millisecond
        int progressBarUpdater = 0;

        try {

            DataPoint messageRate;
            //Note: value of -1 marks the end of the buffer content.
            while ((messageRate = buffer.take()).getValue() != -1) {

                long startTime = System.nanoTime(); //Get nano time just before removing message data from buffer
                //-------------------------------
                // Do some processing around at this point.
                // Let's just simulate work time with Thread.sleep()
                try {
                    Thread.sleep(sleepTime);
                    sumMessageRates += messageRate.getValue();
                    System.out.println("Take message rate " + messageRate.getValue());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //---------------------------------
                // End simulation
                //---------------------------------

                long timeNow = System.nanoTime(); //get current nano time after the processing above
                long estimatedTime = timeNow - startTime; //This gives time spent per message data

                sumProcessTime += estimatedTime;

                progressBarUpdater++;

                SwingUI.updateProgressBar(progressBarUpdater);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SwingUI.textArea.append("Total latency (ns): " + sumProcessTime+"\n");
        SwingUI.textArea.append("No of Messages: " + sumMessageRates+"\n");
        SwingUI.textArea.append("Average latency (ns): " + (double) sumProcessTime / sumMessageRates+"\n");
        SwingUI.textArea.append("Throughput (Messages/sec): " + (double) sumMessageRates / (sumProcessTime *1e-9)+"\n");

        SwingUI.uploadButton.setEnabled(true);
    }
}