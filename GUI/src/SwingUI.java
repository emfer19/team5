import java.awt.*;
import java.awt.Event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import javax.swing.*;

public class SwingUI extends JFrame {

    private JPanel mainWindow;
    private JButton uploadButton;
    private JFileChooser csvChooser;
    private JLabel uploadLabel;
    private JTextField parameter1;


    //BE SURE NOT TO SET PARAMETER HINTS TO ANY
    //POSSIBLE PARAMETER INPUTS AS FOCUS LISTENER
    //WILL CLEAR IT OUT ON FOCUS IF IT EQUALS THE HINT
    private final String PARAMETER_1_HINT = "Sample Parameter"; //set equal to parameter names/inputs

    public SwingUI() {

        final JFileChooser csvChooser = new JFileChooser();

        //Initialize new JPanel
        mainWindow = new JPanel();

        //Setting frame properties
        setTitle("Processor");
        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        setVisible(true);

        //Initialize upload button
        uploadButton = new JButton("Upload CSV");

        //add listener
        ActionListener uploadButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //handle CSV upload HERE
                int returnValue = csvChooser.showOpenDialog(null);
                // int returnValue = csvChooser.showSaveDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = csvChooser.getSelectedFile();
                    System.out.println(selectedFile.getAbsolutePath());
                }
                uploadLabel.setText(parameter1.getText());

            }
        };

        uploadButton.addActionListener(uploadButtonListener);

        uploadLabel = new JLabel("Upload your CSV data file here");


        //Initialize text box
        parameter1 = new JTextField(PARAMETER_1_HINT);
        parameter1.setForeground(Color.gray);

        //Create a new FocusListener for each Parameter
        FocusListener parameter1Focus = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (parameter1.getText().equals(PARAMETER_1_HINT)){
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

        getContentPane().add(mainWindow);

        mainWindow.add(uploadButton);
        mainWindow.add(uploadLabel);
        mainWindow.add(parameter1);
    }
}
