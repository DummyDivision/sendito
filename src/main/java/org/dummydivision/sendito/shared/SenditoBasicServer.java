package org.dummydivision.sendito.shared;

import java.util.List;
import org.dummydivision.sendito.shared.message.Message;

/**
 * Main interface for interacting with sendito. This should be implemented by
 * the main application.
 */
public interface SenditoBasicServer {

    /**
     * Sends a new message.
     *
     * @param message The message to be sent
     * @return The Message object that was created
     */
    public Message sendMessage(String message);

    /**
     * Loads a message.
     *
     * @param id The unique id of the Message
     * @return The Message to be loaded
     */
    public Message getMessage(String id);

    /**
     * Gets the username of the current user.
     *
     * @return Username of current user
     */
    public String getSelf();

    /**
     * Gets a list of all Messages.
     *
     * @return List of all Messages
     */
    public List<Message> getAllMessages();
}
