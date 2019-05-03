package org.team5.app.main;

import org.team5.app.dataprocessing.CSVReader;
import org.team5.app.dataprocessing.DataPoint;

import java.util.concurrent.LinkedBlockingQueue;

import java.lang.System;

public class BufferThread extends Thread implements IThreadIO{

    private LinkedBlockingQueue<DataPoint> buffer;
    private IThreadIO outstream;
    private IThreadIO instream;
    private int cap;
    private int currentTotal;
    private int maxOverflow;

    public BufferThread(int capacity) {
        this.cap = capacity;
        this.currentTotal = 0;
        this.maxOverflow = 0;
        this.buffer = new LinkedBlockingQueue<DataPoint>(1000);
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
    
    //Used by main to know to start it as a thread or not
    public boolean isRunnable(){
        return true;
    }
    
    @Override
    public void run() {
        System.out.println("Buffer thread running");
        //Periodically check the buffer status
        //Should this have an outstream to the DataAnalyzer for overflow analysis?
    }

    public DataPoint remove() {
        try {
            return buffer.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Returns the next point in the queue currently and updates current total
    public DataPoint pull(){
        DataPoint p = this.remove();
        this.currentTotal -= p.getValue();
        return p;
    }

    //Adds input to the Q and updates currentTotal.
    //Also checks for/updates overflow
    public void push(DataPoint p){
        this.currentTotal += p.getValue();
        if(this.currentTotal > this.cap){
            this.maxOverflow = this.currentTotal-this.cap;
        }
        try {
            this.buffer.put(p);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Sets the output target of this thread
    public void setOutstream(IThreadIO obj){
        this.outstream = obj;
    }

    //Sets the intake target of this thread
    public void setInstream(IThreadIO obj){
        this.instream = obj;
    }

}
