package org.team5.app.gui;

import com.sun.deploy.panel.JavaPanel;
import org.team5.app.dataprocessing.CSVReader;
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
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class SwingUI extends JFrame {

    private static JProgressBar progressBar;
    private JPanel mainWindow;
    private JPanel topInputPanel;
    private JPanel bottomOutputPanel;
    public static JButton uploadButton;
    private JFileChooser csvChooser;
    private JLabel uploadLabel;
    private JTextField bufferSize;
    public static JTextArea textArea;
    //Get dimension of any screen
    private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    private final String BUFFER_SIZE_HINT = "Enter Buffer Size Here";

    public SwingUI() {

        initComponents();
    }

    public static void updateProgressBar(int value) {
        progressBar.setValue(value);
    }

    public void initComponents() {
        //Initialze the filechooser
        csvChooser = new JFileChooser();

        //Initialize the progress bar
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);

        //Setting frame properties
        setTitle("Processor");
        setSize(getScreenWidth() / 2 + 100, getScreenHeight() / 2 + 100);
        //setResizable(false);
        setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        topInputPanel = new JPanel(new GridBagLayout());
        topInputPanel.setPreferredSize(new Dimension(getScreenWidth() / 2,getScreenHeight() / 4));

        //Set titled border for top panel
        TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Input");
        title.setTitleJustification(TitledBorder.LEFT);
        topInputPanel.setBorder(title);

        //Initialize upload button
        uploadButton = new JButton("Upload CSV");
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        topInputPanel.add(uploadButton,gbc);

        uploadLabel = new JLabel();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 0;
        topInputPanel.add(uploadLabel,gbc);

        //add action listener
        uploadButton.addActionListener(e -> {
            //handle CSV upload HERE
            int returnValue = csvChooser.showOpenDialog(this);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = csvChooser.getSelectedFile();
                if(selectedFile.isFile()) {

                    String csvFilePath = selectedFile.getAbsolutePath();
                    try {
                        if (csvFilePath.substring(csvFilePath.lastIndexOf(".")).equals(".csv")) {
                            uploadLabel.setText(csvFilePath);
                            startProcessing(csvFilePath);
                        } else {
                            JOptionPane.showMessageDialog(this, "Not a CSV file. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    catch (StringIndexOutOfBoundsException ex)
                    {
                        JOptionPane.showMessageDialog(this, "Not a valid file type. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        });

        //Initialize text box
        bufferSize = new JTextField(BUFFER_SIZE_HINT);
        bufferSize.setForeground(Color.gray);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 1;
        topInputPanel.add(bufferSize,gbc);

        //Create a new FocusListener for each Parameter
        FocusListener bufferSizeFocus = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (bufferSize.getText().equals(BUFFER_SIZE_HINT)) {
                    bufferSize.setText("");
                    bufferSize.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (bufferSize.getText().equals("")) {
                    bufferSize.setText(BUFFER_SIZE_HINT);
                    bufferSize.setForeground(Color.gray);
                }
            }
        };
        bufferSize.addFocusListener(bufferSizeFocus);


        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        topInputPanel.add(progressBar,gbc);

        //Initialize the main panel and set its layout constraints
        GridBagConstraints gbcMain = new GridBagConstraints();
        mainWindow = new JPanel(new GridBagLayout());

        gbcMain.insets = new Insets(10,20,10,20);
        gbcMain.fill = GridBagConstraints.HORIZONTAL;
        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        mainWindow.add(topInputPanel,gbcMain);

        bottomOutputPanel = new JPanel(new GridBagLayout());
        title = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Output");
        title.setTitleJustification(TitledBorder.LEFT);
        bottomOutputPanel.setBorder(title);

        gbcMain.fill = GridBagConstraints.HORIZONTAL;
        gbcMain.gridx = 0;
        gbcMain.gridy = 1;
        mainWindow.add(bottomOutputPanel,gbcMain);

        //Initialize the text area to display output
        GridBagConstraints gbcBottom = new GridBagConstraints();
        textArea = new JTextArea(10,20);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        gbcBottom.fill = GridBagConstraints.BOTH;
        gbcBottom.weightx = 1.0;
        gbcBottom.weighty = 1.0;
        bottomOutputPanel.add(scrollPane,gbcBottom);

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
        BlockingQueue<DataPoint> buffer = new ArrayBlockingQueue<>(1000000, true);

        //Read the CSV from the file system
        CSVReader reader = new CSVReader(csvFilePath);

        //Creat the input thread to load the csv into the buffer
        InputThread inputThread = new InputThread(buffer, reader);

        //Creat the processing thread to fetch data from buffer concurrently
        ProcessingThread processingThread = new ProcessingThread(buffer);

        // -1 added to cater for the extra (-1,-1) data point added to the array list in CSVReader.java class
        progressBar.setMaximum(reader.getDataSize() - 1);

        //Start the input thread
        new Thread(inputThread).start();
        //Start the processing thread
        new Thread(processingThread).start();
    }
}
