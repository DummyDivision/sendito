package org.dummydivision.sendito.gui;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.dummydivision.sendito.shared.Message;
import org.dummydivision.sendito.shared.MessageRepository;

public class JConversationFrame extends JFrame {

    private final MessageRepository messageRepository;
    private final String self;
    private final String reciever;

    private void onSendMessage(JMessagePane messagePane, JTextField input) {
        Message m = new Message(self, reciever, input.getText());
        messageRepository.add(m);
        input.setText(null);
        messagePane.addMessage(m);
    }

    private void setupGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        final JMessagePane messagePane = new JMessagePane(self);
        List<Message> messages = messageRepository.getAll();

        for (Message m : messages) {
            messagePane.addMessage(m);
        }

        mainPanel.add(messagePane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());

        final JTextField input = new JTextField();
        input.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                onSendMessage(messagePane, input);
            }
        });
        inputPanel.add(input, BorderLayout.CENTER);
        JButton sendButton = new JButton();
        sendButton.setText("Senden");
        sendButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                onSendMessage(messagePane, input);
            }
        });
        inputPanel.add(sendButton, BorderLayout.EAST);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
    }

    public JConversationFrame(MessageRepository messageRepository, String self, String reciever) throws HeadlessException {
        super();
        this.messageRepository = messageRepository;
        this.self = self;
        this.reciever = reciever;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sendito");

        setupGUI();

        pack();
        setVisible(true);
    }
}
