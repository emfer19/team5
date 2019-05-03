package org.team5.app.main;

import org.team5.app.dataprocessing.DataPoint;
/*
* Interface: IThreadIO
* Holden Duncan
* An interface for input threads and processing threads
* to pass messages around. Also to be used by the DataAnalyzer
* to intake data at the end of the process.
*/

public interface IThreadIO {

public void push(DataPoint p); //should be called to put data into the thread
public DataPoint pull(); //should be called to receive the next piece of data from that object
public void setInstream(IThreadIO obj);
public void setOutstream(IThreadIO obj);
public boolean isRunnable();//Some "threads" are more so end point objects and should not be run

}
