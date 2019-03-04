/* SimClock
 * A simulated clock for tracking virtual time down to the nanosecond
 */

public class SimClock{
    private double time; //The current "time"
    private double deltaTime; //The default time increment
    private double minStart; //when the last "minute" started
    private double secStart; //For tracking seconds    
    /*
    * @param td: the default time increment when calling SimClock.update()
    */
    public SimClock(double dt){
        deltaTime = dt;
    }
    
    //Increments the clock based on the deltaTime value
    public void update(){
        time += deltaTime;
    }
    
    //Easy method for knowing if it's the next minute i.e. rate change time
    // returns true if the rate should be updated
    public boolean isNextMinute(){
        if(minStart+60 < time){
            minStart = time;
            return true;
        }
        else{
            return false;
        }
    }
    
    public boolean isNextSecond(){
        if(secStart+1 > time){
            secStart = time;
            return true;
        }
        else{
            return false;
        }
    }
    
    //Setting the first piece of time data to create the proper start time
    public void setTime(double firstTime){
        time = firstTime;
        minStart = firstTime;
        secStart = firstTime;
    }
    
    //returns the current "time"
    public double getTime(){
        return time;
    }
    

}
