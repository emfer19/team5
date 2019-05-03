package org.team5.app.main;

import org.team5.app.dataprocessing.CSVReader;
import org.team5.app.dataprocessing.DataPoint;

import java.util.concurrent.BlockingQueue;

import java.lang.System;

public class InputThread extends Thread implements IThreadIO{

    private CSVReader reader;
    private IThreadIO outstream;
    private IThreadIO instream;

    /**
     * This thread is for properly pushing csv data into the first buffer.
     * Buffers are passive while this input thread is active.
     *
     * @param reader the csv objects
     */
    public InputThread(CSVReader reader) {
        this.reader = reader;
    }

    public void go(){
//        start();
    }

    /**
     * This thread reads the message rates into a blocking queue buffer.
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
        System.out.println("InputThread running");

      /*----This for loop is to loop through the input data----*/
        for (int i = 0; i < reader.dataPoints.size(); i++) {
            //This for loop repersents using the given rate in millisecond
            //increments for one second.
        /*----This for loop loops sixty times to repersent one minute----*/
          for ( int k=0; k<60;k++){
          /*----This for loop executs for ~1 second----*/
            int rate = reader.next().getValue(); //Intake from CSVreader
            for (int j=0; j<1000; j++){
                //At certain time intervals push a new set of messages into the attached buffer
                try {
                    Thread.sleep(1); //Sleep for a millisecond before adding anything
                    //get the time and the rate over 1000 and place it in buffer
                    outstream.push(new DataPoint(System.nanoTime(), rate));
                    //System.out.println("Put message rate "+reader.dataPoints.get(i).getValue());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }//close 1 second for loop
          }//close close 60 loops of 1 second for loop
        }//close data length for loop

        System.out.println("InputThread exiting");
    }

    //Returns the next point in the queue currently
    public DataPoint pull(){
    //Dummy because nothing should pull from it
        return null;    
    }
    
    //Takes the input and adds it to the queue
    public void push(DataPoint p){
    //Dummy because nothing should push to it
    }

    //Sets the outstream for this thread, should be the first buffer
    //of the sim.
    public void setOutstream(IThreadIO obj){
        this.outstream = obj;
    }

    public void setInstream(IThreadIO obj){
        this.instream = obj;
    }
}
