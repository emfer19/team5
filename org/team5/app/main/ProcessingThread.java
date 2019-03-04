package org.team5.app.main;

import org.team5.app.dataprocessing.CSVReader;
import org.team5.app.dataprocessing.DataPoint;
import org.team5.app.gui.SwingUI;

import java.util.Queue;
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

        long sumProcessTime = 0;
        long sumMessageRates = 0;
        long sleepTime = 100; //in millisecond
        int progressBarUpdater = 0;
        
        boolean primed = false;
        double processTime = 2d*(0.000000001d); //Change this to the input from the window
        DataAnalyzer analyzer = new DataAnalyzer();
        bufferedMessages = new Queue<int[2]>();
        
        try {

            DataPoint messageRate;
            SimClock clock = new SimClock(processTime);
            
            messageRate = buffer.take();
            int message[2] = {0, 0};
            //Note: value of -1 marks the end of the buffer content.
            while (messageRate.getValue() != -1) {

                long startTime = System.nanoTime(); //Get nano time just before removing message data from buffer
                //-------------------------------
                // Do some processing around at this point.
                
                //First startup
                if(!primed){
                    clock.setStartTime(messageRate.getTimeIn());
                    messageRate = buffer
                    primed = true;
                }
                //Updating clock
               
                clock.update();
                if(clock.isNextMinute()){ //If it's a new minute update the message rate
                    messageRate = buffer.take()
                }
                //Adding messages to the buffered data based on the current rate
                if(clock.isNextSecond()){
                    bufferedMessages.add({messageRate.getValue(), clock.getTime()});
                }
               
                //Updating Data
                //Update the current set of messages once the program gets through that backlog
                if(message[1] <= 0){
                    if(bufferedMessages.peek() != null){message = bufferedMessages.remove();}
                }
                else{
                    message[1]--;
                }
                
                //Recording Stats
                analyzer.writeData(message[0], clock.getTime());
                
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
        System.out.println("Total latency (ns): " + sumProcessTime);
        System.out.println("No of Messages: " + sumMessageRates);
        System.out.println("Average latency (ns): " + (double) sumProcessTime / sumMessageRates);
        System.out.println("Throughput (Messages/sec): " + (double) sumMessageRates / (sumProcessTime *1e-9));
        System.out.println("percent: " + progressBarUpdater);
        analzyer.printStats();
    }
}
