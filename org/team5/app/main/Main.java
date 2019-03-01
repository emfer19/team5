//package main.app;

import main.app.Buffer;

public class Main {

    public static void main(String[] args) {

        Thread mainThread = Thread.currentThread();
        // getting name of Main thread
        System.out.println("Current thread: " + mainThread.getName());

        Buffer buffer = new Buffer();
        Thread bufferThread = new Thread(buffer);
        bufferThread.start();

        try {
            buffer.checkSize();
            mainThread.join(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        buffer.checkSize();

        DataProcessor.processBufferData();

        buffer.checkSize();
    }
}
