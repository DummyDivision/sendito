package org.dummydivision.sendito.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.dummydivision.sendito.shared.SenditoBasicServer;

public class JInputPanel extends JPanel implements ActionListener {

    private final JTextField input;
    private final JButton sendButton;
    private final SenditoBasicServer server;

    public JInputPanel(SenditoBasicServer server) {
        super(new BorderLayout());
        this.server = server;

        input = new JTextField();
        input.addActionListener(this);
        add(input, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        sendButton.addActionListener(this);
        add(sendButton, BorderLayout.EAST);
    }

    public void actionPerformed(ActionEvent ae) {
        server.sendMessage(input.getText());
    }

    /*public void setEnabled(boolean bln) {
     input.setEnabled(bln);
     sendButton.setEnabled(bln);
     }*/
}
