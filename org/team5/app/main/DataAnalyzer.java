package org.team5.app.main;
import java.util.Collections;
import java.util.ArrayList;
import java.lang.Math.*;
import java.lang.*;

import org.team5.app.dataprocessing.DataPoint;

/* Author: Holden D
 * DataAnalzyer
 *  Is used for tracking and calculating statistical data point by point
 */
 
public class DataAnalyzer implements IThreadIO {
    private int count;
    private double total;
    private double max;
    private double min;
    private IThreadIO instream;
    private IThreadIO outstream;
    
    private ArrayList<Double> data = new ArrayList<Double>();
        
    public DataAnalyzer(){}

    //Is used to prevent main from running it as a thread
    public boolean isRunnable(){
        return false;
    }
    
    //Dummy function because nothing should take a DataPoint from this
    //Honestly should throw an error if even called.
    public DataPoint pull(){
        return null;
    }

    public void setInstream(IThreadIO obj){
        this.instream = obj;
    }

    public void setOutstream(IThreadIO obj){
        this.outstream = obj;
    }
    
    
    //Takes a DataPoint and adds consolidates it's data
    public void push(DataPoint p){
        this.writeData(p.getTimeIn(), System.nanoTime());
    }
    
    //Takes two doubles and  stores the data for later calculations
    public void writeData(double timeIn, double timeOut){
        count++;
        //latency
        double latency = timeOut-timeIn;
        data.add(latency);
        this.total += latency;
    }
    
    //Calculate and return the mean
    public double mean(){
        return (this.total/(double)this.count);
    }
    
    //Calculate a given percentile based on the current data
    public double percentile(double p){
        Collections.sort(this.data);
        int borderValue = (int)Math.ceil(((double)p / (double)100) * (double)this.count);
        return this.data.get(borderValue-1);
    }

    //Calculate and return the program throughput
    public double throughput(){
        return this.count/this.total; //this.total repersents the total amount of latency and thus count/total = messages per second
    }
    
    //A function that easily prints out some relevent stats
    public String printStats(){
        return String.format(
                "Average Latency: %.9f\nThroughput %.4f msg/s\nPercentiles:\n50th: %.9f\n75th: %.9f\n90th: %.9f\n99th: %.9f\n",
                this.mean(),this.throughput(), this.percentile(50), this.percentile(75), this.percentile(90), this.percentile(99)
            );
    }

    //Simple main for playtesting the stat calculation
    public static void main(String[] args){
        DataAnalyzer da = new DataAnalyzer();
        da.writeData(1,2);
        da.writeData(1,3);
        da.writeData(1,4);
        da.writeData(1,5);
        da.writeData(1,6);
        da.writeData(1,7);
        da.writeData(1,8);
        da.writeData(1,9);
        da.writeData(1,10);
        System.out.println(da.printStats());

    }

}
