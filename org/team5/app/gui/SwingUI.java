package org.team5.app.gui;

import org.team5.app.dataprocessing.CSVReader;
import org.team5.app.dataprocessing.DataPoint;
import org.team5.app.main.InputThread;
import org.team5.app.main.ProcessingThread;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SwingUI extends JFrame implements FocusListener, ActionListener, ItemListener, ChangeListener {

    private static JProgressBar progressBar;
    private JPanel mainWindow;
    private JPanel topInputPanel;
    private JPanel bottomOutputPanel;
    public static JButton uploadButton, processButton;
    private JFileChooser csvChooser;
    private JLabel filePathLabel, defaultBufferSize, defaultProcessTime;
    private JTextField bufferSize, processTime;
    private JCheckBox microsecondData;
    private JSpinner numberOfProcessorsSpinner;
    public Boolean ratePreference; // if true in microseconds, if false in seconds
    public int processorNumber;
    public static JTextArea textArea;

    //Get dimension of any screen
    private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

    private final String PROCESS_TIME_HINT = "Enter process time";
    private final String BUFFER_SIZE_HINT = "Enter buffer size";

    private final int DEFAULT_BUFFER_SIZE = 1000000;
    private final double DEFAULT_PROCESS_TIME = 1.0; //in millisecond

    private String csvFilePath = "";

    public SwingUI() {

        initComponents();
    }

    public static void updateProgressBar(int value) {
        progressBar.setValue(value);
    }

    public void initComponents() {
        //Initialze the filechooser
        csvChooser = new JFileChooser();

        //Setting frame properties
        setTitle("Processor");
        setSize(getScreenWidth() / 2 + 100, getScreenHeight() / 2 + 100);
        setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();

        topInputPanel = new JPanel(new GridBagLayout());
        topInputPanel.setPreferredSize(new Dimension(getScreenWidth() / 2, getScreenHeight() / 4));

        //Set titled border for top panel
        TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Input");
        title.setTitleJustification(TitledBorder.LEFT);
        topInputPanel.setBorder(title);

        //Initialize upload button
        uploadButton = new JButton("Upload CSV");
        uploadButton.addActionListener(this);
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        topInputPanel.add(uploadButton, gbc);

        //Initialize buffer size text box
        bufferSize = new JTextField(BUFFER_SIZE_HINT);
        bufferSize.addFocusListener(this);
        bufferSize.setForeground(Color.gray);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 0;
        topInputPanel.add(bufferSize, gbc);

        //Initialize process time text box
        processTime = new JTextField(PROCESS_TIME_HINT);
        processTime.addFocusListener(this);
        processTime.setForeground(Color.gray);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 2;
        gbc.gridy = 0;
        topInputPanel.add(processTime, gbc);

        //initialize checkbox for microsecond data
        microsecondData = new JCheckBox("Check this box if data is already in Microseconds");
        microsecondData.addItemListener(this);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 3;
        topInputPanel.add(microsecondData, gbc);

        numberOfProcessorsSpinner = new JSpinner(new SpinnerNumberModel(1,1,100,1));
        numberOfProcessorsSpinner.addChangeListener(this);
        JFormattedTextField tf = ((JSpinner.DefaultEditor) numberOfProcessorsSpinner.getEditor()).getTextField();
        tf.setEditable(false);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 2;
        gbc.gridy = 3;
        topInputPanel.add(numberOfProcessorsSpinner, gbc);

        filePathLabel = new JLabel();
        filePathLabel.setVisible(false);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        topInputPanel.add(filePathLabel, gbc);

        //Initialize the progress bar
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        topInputPanel.add(progressBar, gbc);

        //Initialize process button
        processButton = new JButton("Process Data");
        processButton.addActionListener(this);
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 7;
        topInputPanel.add(processButton, gbc);

        defaultBufferSize = new JLabel();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
        topInputPanel.add(defaultBufferSize, gbc);

        defaultProcessTime = new JLabel();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 3;
        topInputPanel.add(defaultProcessTime, gbc);

        //Initialize the main panel and set its layout constraints
        GridBagConstraints gbcMain = new GridBagConstraints();
        mainWindow = new JPanel(new GridBagLayout());

        gbcMain.insets = new Insets(10, 20, 10, 20);
        gbcMain.fill = GridBagConstraints.HORIZONTAL;
        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        mainWindow.add(topInputPanel, gbcMain);

        bottomOutputPanel = new JPanel(new GridBagLayout());
        title = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Output");
        title.setTitleJustification(TitledBorder.LEFT);
        bottomOutputPanel.setBorder(title);

        gbcMain.fill = GridBagConstraints.HORIZONTAL;
        gbcMain.gridx = 0;
        gbcMain.gridy = 1;
        mainWindow.add(bottomOutputPanel, gbcMain);

        //Initialize the text area to display output
        GridBagConstraints gbcBottom = new GridBagConstraints();
        textArea = new JTextArea(10, 20);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        gbcBottom.fill = GridBagConstraints.BOTH;
        gbcBottom.weightx = 1.0;
        gbcBottom.weighty = 1.0;
        bottomOutputPanel.add(scrollPane, gbcBottom);

        //add the panel to the content pane of our frame
        Container container = super.getContentPane();
        //container.setLayout(new GridBagLayout());
        container.add(mainWindow);

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
        //int buffer_size = Integer.parseInt(bufferSize.getText())DEFAULT_BUFFER_SIZE
        String buff_size = bufferSize.getText();
        int buff_size_int = (buff_size != null && !buff_size.equals("") && !buff_size.equals(BUFFER_SIZE_HINT)) ? Integer.parseInt(buff_size) : getDefaultBufferSize();
        BlockingQueue<DataPoint> buffer = new ArrayBlockingQueue<>(buff_size_int, true);

        //Read the CSV from the file system
        CSVReader reader = new CSVReader(csvFilePath);

        //Creat the input thread to load the csv into the buffer
        InputThread inputThread = new InputThread(buffer, reader);

        //Creat the processing thread to fetch data from buffer concurrently
        String process_time = processTime.getText();
        double process_time_int = (process_time != null && !process_time.equals("") && !process_time.equals(PROCESS_TIME_HINT)) ? Double.parseDouble(process_time) : getDefaultProcessTime();
        ProcessingThread processingThread = new ProcessingThread(buffer, process_time_int);

        // -1 added to cater for the extra (-1,-1) data point added to the array list in CSVReader.java class
        progressBar.setMaximum(reader.getDataSize() - 1);

        //Start the input thread
        new Thread(inputThread).start();
        //Start the processing thread
        new Thread(processingThread).start();
    }

    /**
     * used to access rate preference in main
     */
    public boolean getRatePref() {
        return ratePreference;
    }

    /**
     * used to access processor count in main
     */
    public int getProcessorNumber() { return processorNumber;}

    /**
     * Invoked when a component gains the keyboard focus.
     *
     * @param e
     */
    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource() == bufferSize) {
            if (bufferSize.getText().equals(BUFFER_SIZE_HINT)) {
                bufferSize.setText("");
                bufferSize.setForeground(Color.black);
            }
        } else {
            if (processTime.getText().equals(PROCESS_TIME_HINT)) {
                processTime.setText("");
                processTime.setForeground(Color.black);
            }
        }

    }

    /**
     * Invoked when a component loses the keyboard focus.
     *
     * @param e
     */
    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() == bufferSize) {
            if (bufferSize.getText().equals("")) {
                bufferSize.setText(BUFFER_SIZE_HINT);
                bufferSize.setForeground(Color.gray);
            }
        } else {
            if (processTime.getText().equals("")) {
                processTime.setText(PROCESS_TIME_HINT);
                processTime.setForeground(Color.gray);
            }
        }

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            ratePreference = Boolean.TRUE;
        }

        if (e.getStateChange() == ItemEvent.DESELECTED) {
            ratePreference = Boolean.FALSE;
        }
    }

    @Override
    public void stateChanged(ChangeEvent e){
        //set processorNumber to whatever the value is in the spinner

    }

    /**
     * Invoked when the uploadButton is clicked
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == uploadButton) {
            //handle CSV upload here
            int returnValue = csvChooser.showOpenDialog(this);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = csvChooser.getSelectedFile();
                if (selectedFile.isFile()) {
                    csvFilePath = selectedFile.getAbsolutePath();
                    try {
                        if (csvFilePath.substring(csvFilePath.lastIndexOf(".")).equals(".csv")) {
                            filePathLabel.setText(csvFilePath);
                            filePathLabel.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(this, "Not a CSV file. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
                            csvFilePath="";
                        }
                    } catch (StringIndexOutOfBoundsException ex) {
                        JOptionPane.showMessageDialog(this, "Not a valid file type. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        csvFilePath="";
                        ex.printStackTrace();
                    }
                }
            }
        }
        else{
            if(!csvFilePath.equals(""))
                startProcessing(csvFilePath);
            else
                JOptionPane.showMessageDialog(this, "Upload your CSV data file first." +
                        "\n\n(Optional) Specify buffer size and process time," +
                        "\nor use the default values of "+DEFAULT_BUFFER_SIZE+" messages and "+DEFAULT_PROCESS_TIME+" millisecond",
                        "Message", JOptionPane.ERROR_MESSAGE);
        }
    }

    public int getDefaultBufferSize() {
        String text = "Using default buffer size of: " + DEFAULT_BUFFER_SIZE + " messages";
        defaultBufferSize.setText(text);
        return DEFAULT_BUFFER_SIZE;
    }

    public double getDefaultProcessTime() {
        String text = "Using default process time of: " + DEFAULT_PROCESS_TIME + " millisecond";
        defaultProcessTime.setText(text);
        return DEFAULT_PROCESS_TIME;
    }
}
