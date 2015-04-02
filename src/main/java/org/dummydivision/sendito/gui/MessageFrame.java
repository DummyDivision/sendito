package org.dummydivision.sendito.gui;

import java.awt.Color;
import java.awt.HeadlessException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import org.dummydivision.sendito.shared.Message;

public class MessageFrame extends JFrame {

    public MessageFrame(List<Message> messages) throws HeadlessException {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sendito");

        String messageString = "";
        for (Message m : messages) {
            messageString += m + "\n";
        }
        JTextArea textArea = new JTextArea(messageString);
        Color LIGHT_BLUE = new Color(233, 233, 233);
        textArea.setBackground(LIGHT_BLUE);
        add(textArea);
        
        pack();
        setVisible(true);
    }
}
