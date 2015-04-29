package org.dummydivision.sendito.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.dummydivision.sendito.shared.SenditoBasicServer;

/**
 * Custom Panel to hold text input controls.
 */
public class JInputPanel extends JPanel implements ActionListener {

    // Text input field
    private final JTextField input;
    // Send button
    private final JButton sendButton;
    // Allows us to access sendito's functionality
    private final SenditoBasicServer server;
    // Window to browse history
    private final JHistoryFrame historyFrame;

    /**
     * Construct a new panel. The server will be stored and cannot be changed!
     *
     * @param server A class that provides the services defined by
     * SenditoBasicServer
     */
    public JInputPanel(SenditoBasicServer server) {
        super(new BorderLayout());
        this.server = server;
        historyFrame = new JHistoryFrame(server);

        // Construct the GUI
        input = new JTextField();
        input.addActionListener(this); // "this" leaking should not be a problem here
        add(input, BorderLayout.CENTER);

        // Panel to hold send and history button
        JPanel buttons = new JPanel(new FlowLayout());
        sendButton = new JButton("Send");
        sendButton.addActionListener(this);
        buttons.add(sendButton);
        
        JButton historyButton = new JButton("History");
        historyButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                // Just show the history window
                historyFrame.setVisible(true);
            }
        });
        buttons.add(historyButton);
        add(buttons, BorderLayout.EAST);
    }

    /**
     * Sends a message. This will be used as ActionListener for the input text
     * field and the send button
     *
     * @param ae Will be ignored
     */
    public void actionPerformed(ActionEvent ae) {
        // Pass the message to our server
        server.sendMessage(input.getText());
        // Delete the text that was entered by the user
        input.setText(null);
    }

    /**
     * Disables or enables the panel's input controls.
     *
     * @param enable Enable the input controls
     */
    @Override
    public void setEnabled(boolean enable) {
        // Enable / Disable our input elements
        input.setEnabled(enable);
        sendButton.setEnabled(enable);
    }
}
