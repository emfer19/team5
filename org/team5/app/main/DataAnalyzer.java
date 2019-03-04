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
        double latency = timeOut-timeIn;
        
        //update running mean
        mean  += (latency - mean)/count ;
        
        //update max
        if(latency > max){
            max = latency;
        }
    }
    
    public String printStats(){
        return String.format("Average Latency: %.9f\n Max Latency: %.9f\n", mean, max);
    }
}
