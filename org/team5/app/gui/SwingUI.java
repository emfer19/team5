package org.team5.app.gui;

import org.team5.app.dataprocessing.DataPoint;
import org.team5.app.main.InputThread;
import org.team5.app.main.ProcessingThread;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import javax.swing.*;

public class SwingUI extends JFrame {

    private JPanel mainWindow;
    private JButton uploadButton;
    private JFileChooser csvChooser;
    private JLabel uploadLabel;
    private JTextField parameter1;
    private JTextArea textArea;

    private JProgressBar progressBar;
    private static int min = 0;
    private static int max = 100;

    //Get dimension of any screen
    private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

    private final String PARAMETER_1_HINT = "Sample Parameter"; //set equal to parameter names/inputs

    public SwingUI() {

        initComponents();
    }

    public void initComponents() {
        //Initialze the filechooser
        csvChooser = new JFileChooser();

        //Initialize the progress bar
        progressBar = new JProgressBar();
        progressBar.setMinimum(min);
        progressBar.setMaximum(max);

        //Initialize the text area to display the csv file
        textArea = new JTextArea();

        //Setting frame properties
        setTitle("Processor");
        setSize(getScreenWidth() / 2 + 100, getScreenHeight() / 2 + 100);
        setResizable(false);
        setLocationRelativeTo(null);

        //Initialize upload button
        uploadButton = new JButton("Upload CSV");

        //add action listener
        uploadButton.addActionListener(e -> {
            //handle CSV upload HERE
            int returnValue = csvChooser.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = csvChooser.getSelectedFile();

                String csvFilePath = selectedFile.getAbsolutePath();
                uploadLabel.setText(csvFilePath);

                //Call the startProcessing() function here
                startProcessing(csvFilePath);
            }
        });

        uploadLabel = new JLabel("Upload your CSV file here");

        //Initialize text box
        parameter1 = new JTextField(PARAMETER_1_HINT);
        parameter1.setForeground(Color.gray);

        //Create a new FocusListener for each Parameter
        FocusListener parameter1Focus = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (parameter1.getText().equals(PARAMETER_1_HINT)) {
                    parameter1.setText("");
                    parameter1.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (parameter1.getText().equals("")) {
                    parameter1.setText(PARAMETER_1_HINT);
                    parameter1.setForeground(Color.gray);
                }
            }
        };

        parameter1.addFocusListener(parameter1Focus);

        //Initialize new JPanel and the components
        mainWindow = new JPanel();
        mainWindow.add(uploadButton);
        mainWindow.add(uploadLabel);
        mainWindow.add(parameter1);
        mainWindow.add(progressBar);

        //add the panel to the content pane of our frame
        super.getContentPane().add(mainWindow);

        //Finally display the frame and make sure the application exits in the background when closed
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private int getScreenWidth() {
        return dim.width;
    }

    private int getScreenHeight() {
        return dim.height;
    }

    /**
     * @param csvFilePath the path to the CSV file
     */
    private void startProcessing(String csvFilePath) {

         // A blocking queue buffer if size 1000 that is thread safe. It supports operations that wait for
         // the queue to become non-empty when retrieving an element, and wait for space to become available
         // in the queue when storing an element.
         BlockingQueue<DataPoint> buffer = new ArrayBlockingQueue<>(1000,true);

         //Creat the input and processing threads
        InputThread inputThread = new InputThread(buffer,csvFilePath);
        ProcessingThread processingThread = new ProcessingThread(buffer);

        //Start the input thread
        new Thread(inputThread).start();
        //Start the data processing thread
        new Thread(processingThread).start();
    }
}
