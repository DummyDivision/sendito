package org.dummydivision.sendito.gui;

import java.awt.HeadlessException;
import java.util.List;
import javax.swing.JFrame;
import org.dummydivision.sendito.shared.Message;

public class JConversationFrame extends JFrame {

    public JConversationFrame(List<Message> messages, String self) throws HeadlessException {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sendito");

        JMessagePane messagePane = new JMessagePane(self);

        for (Message m : messages) {
            messagePane.addMessage(m);
        }

        add(messagePane);

        pack();
        setVisible(true);
    }
}
