/* Class: DataPoint
* @Param long timeIn: Decimal value indicating at what time in minutes
* of the day the data point was entered into the system (i.e. 0.0 is midnight,
* 720.0 would be noon, and 1439 would be 11:59PM)
* Implements IDataObject and provides a means for tracking
* individual data points through the simulation.
*/

public class DataPoint implements IDataObject{

    private String date;
    private double  timeIn;
    private double  timeOut;
    private int     value;

    public DataPoint(double tIn, int v){
        this.timeIn = tIn;
        this.value = v;
    }

    public void setTimeOut(double time){
        this.timeOut = time;
    }

    public String asString(){
        return String.format("Value: %d TimeIn: %f", this.value, this.timeIn);
    }

}
