package org.dummydivision.sendito.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.dummydivision.sendito.shared.SenditoBasicClient;
import org.dummydivision.sendito.shared.SenditoBasicServer;
import org.dummydivision.sendito.shared.message.Message;
import org.dummydivision.sendito.shared.message.MessageRepository;

public class JConversationFrame extends JFrame implements SenditoBasicClient {

    private final SenditoBasicServer server;
    private final JMessagePane messagePane;
    private final JInputPanel inputPanel;

    public JConversationFrame(SenditoBasicServer server) throws HeadlessException {
        super("Sendito");
        this.server = server;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        messagePane = new JMessagePane(server);
        mainPanel.add(messagePane, BorderLayout.CENTER);
        inputPanel = new JInputPanel(server);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
        setPreferredSize(new Dimension(800, 600));

        pack();
    }

    public void onLostConnection() {
        // Disable the GUI
        inputPanel.setEnabled(false);
    }

    public void onRestoreConnection() {
        // Re-Enable the GUI
        inputPanel.setEnabled(true);
    }

    public void onAddMessage(String id) {
        messagePane.addMessage(server.getMessage(id));
    }

    public void onAddServer(String id) {
        // We actually don't do anything when a server is added
    }

    public void loadFromRepository(MessageRepository messageRepository) {
        List<Message> messages = messageRepository.getAll();

        for (Message m : messages) {
            messagePane.addMessage(m);
        }
    }
}
