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

/**
 * A window that holds a sendito conversation. The main purpose is to provide an
 * accessible user interface.
 */
public class JConversationFrame extends JFrame implements SenditoBasicClient {

    // Allows us to access sendito's functionality
    private final SenditoBasicServer server;
    // Used to display messages
    private final JMessagePane messagePane;
    // Bundles the input controls of the window
    private final JInputPanel inputPanel;

    /**
     * Create a new conversation window. The server will be stored and cannot be
     * changed!
     *
     * @param server A class that provides the services defined by
     * SenditoBasicServer
     * @throws HeadlessException Inherited from JFrame
     */
    public JConversationFrame(SenditoBasicServer server) throws HeadlessException {
        super("Sendito");
        this.server = server;

        // Closing this window will quit the application
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel to hold our GUI elements
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Message display
        messagePane = new JMessagePane(server);
        mainPanel.add(messagePane, BorderLayout.CENTER);
        // Input controls
        inputPanel = new JInputPanel(server);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
        setPreferredSize(new Dimension(800, 600));

        pack();
    }

    /**
     * Notifies the window of a lost connection. This will disable text input
     * and message sending
     */
    public void onLostConnection() {
        // Inform the user of the lost connection
        setTitle("Sendito [No Connection]");

        // Disable the GUI
        inputPanel.setEnabled(false);
    }

    /**
     * Notifies the window of a restored connection. This will re-enable text
     * input and message sending
     */
    public void onRestoreConnection() {
        // Change the title back to normal
        setTitle(server.getSelf() + " - Sendito");

        // Re-Enable the GUI
        inputPanel.setEnabled(true);
    }

    /**
     * Notifies the window of a new incoming message. This will add the message
     * to the message display area
     *
     * @param id The unique id of the incoming message
     */
    public void onAddMessage(String id) {
        // Pass the Message to our messagePane
        messagePane.addMessage(server.getMessage(id));
    }

    /**
     * Notifies the window of a new server. This will not have any effect
     *
     * @param id The unique id of the new server
     */
    public void onAddServer(String id) {
        // We actually don't do anything when a server is added
    }

    // TODO: only display messages since last login
    public void loadFromRepository(MessageRepository messageRepository) {
        List<Message> messages = messageRepository.getAll();

        for (Message m : messages) {
            messagePane.addMessage(m);
        }
    }
}
