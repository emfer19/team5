package org.team5.app.main;
/* Author: Holden D
 * DataAnalzyer
 *  Is used for tracking and calculating statistical data point by point
 */
 
public class DataAnalyzer{
    private int count;
    private double mean;
    private double max;
    private double min;
        
    public DataAnalyzer(){}
    
    public void writeData(double timeIn, double timeOut){
        count++;
        //latency
        long latency = timeOut-timeIn;
        
        //update running mean
        mean  *= (count-1)/count + (latency/count) ;
        
        //update max
        if(latency > max){
            max = lantency;
        }
    }
    
    public void printStats(){
        System.out.println(String.format("Average Latency: %.9f\nMax Latency: %.9f", mean, max));
    }
}
