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

public void add(DataPoint p); //add should take the data and store it or use it
public DataPont take(); //take should be called to receive the next piece of data from that object

}
