package org.team5.app.main;

import org.team5.app.dataprocessing.DataPoint;
import org.team5.app.gui.SwingUI;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProcessingThread extends Thread implements IThreadIO{

    public int process_time;
    public IThreadIO instream;
    public IThreadIO outstream;

    public ProcessingThread(int process_time) {
        this.process_time = process_time;
    }
    
    //Is dummy because processor takes in its own time
    //should probably throw and error
    public void push(DataPoint p){
        return;
    }
    
    //Is dummy because processor pushed data forward at its own time
    //should probably throw an error
    public DataPoint pull(){
        return null;
    }

    //Sets the intake source of this thread
    public void setInstream(IThreadIO obj){
        this.instream = obj;
    }

    //Sets the output target of this thread
    public void setOutstream(IThreadIO obj){
        this.outstream = obj;
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

        DataPoint currentPoint = new DataPoint(0l,0);
        boolean done = false;
        while(!done){
            currentPoint = instream.pull();
            if(currentPoint.getValue() == -1){
                done = true;
            }
            for(int i=0; i<currentPoint.getValue(); i++){
                outstream.push(new DataPoint(currentPoint.getTimeIn(), 1));
                try {
                    Thread.sleep(0l, this.process_time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
