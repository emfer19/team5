import java.awt.*;
import java.awt.Event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class SwingUI extends JFrame {

    private JPanel mainWindow;
    private JButton uploadButton;
    private JLabel uploadLabel;

    public SwingUI() {

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
                uploadLabel.setText("Upload Processing...");
            }
        };

        uploadButton.addActionListener(uploadButtonListener);

        uploadLabel = new JLabel("Upload your CSV data file here");



        getContentPane().add(mainWindow);

        mainWindow.add(uploadButton);
        mainWindow.add(uploadLabel);
    }
}
