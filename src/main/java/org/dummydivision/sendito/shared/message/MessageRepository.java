package org.dummydivision.sendito.shared.message;

import java.util.Collections;
import java.util.List;
import org.dummydivision.sendito.shared.SenditoBasicServer;
import org.ektorp.CouchDbConnector;
import org.ektorp.UpdateHandlerRequest;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.UpdateHandler;
import org.ektorp.support.View;

/**
 * Main collection class to manage Messages. Also implements SenditoBasicServer
 * for convenience.
 */
@View(name = "all", map = "classpath:views/all.js") // Map function for grabbing all Messages from the DB
@UpdateHandler(name = "send", file = "updatehandlers/send.js") // Update handler to ensure consistent timestamps
public class MessageRepository extends CouchDbRepositorySupport<Message> implements SenditoBasicServer {

    /**
     * Create a new MessageRepository on the given database. This will
     * automatically setup the design documents and views.
     *
     * @param db Database connection to use
     */
    public MessageRepository(CouchDbConnector db) {
        // Call superconstructor
        super(Message.class, db);
        // Use default design document
        initStandardDesignDocument();
    }

    /**
     * Gets all messages from the database and sorts them by date.
     *
     * @return Messages sorted by date
     */
    @Override
    public List<Message> getAll() {
        // Load all messages
        List<Message> messageList = super.getAll();
        // Sort messages by date
        Collections.sort(messageList);

        return messageList;
    }

    /**
     * Do not use this method, use sendMessage instead! Will throw an
     * UnsupportedOperationException if used!
     *
     * @param entity ignored
     * @deprecated Use sendMessage instead!
     */
    @Deprecated
    @Override
    public void add(Message entity) {
        // This must not be used anymore to ensure the usage of our UpdateHandler
        throw new UnsupportedOperationException("Do not use this method!");
    }

    /**
     * Sends aa new Message. The timestamp and the sender will be determined by
     * the server through the UpdateHandler.
     *
     * @param message Message to be sent
     * @return The Message object as created by the server
     */
    public Message sendMessage(String message) {
        // Prepare a call to the update handler
        UpdateHandlerRequest sendMessage = new UpdateHandlerRequest()
                .designDocId(stdDesignDocumentId)
                .functionName("send")
                .docId("null")
                .param("body", message);
        // Call the update handler
        String id = db.callUpdateHandler(sendMessage);

        // Read the newly created object
        return get(id);
    }

    /**
     * Loads a message from the database.
     *
     * @param id The unique id of the Message
     * @return The Message to be loaded
     */
    public Message getMessage(String id) {
        return get(id);
    }

    /**
     * This method will return null.
     *
     * @return Always returns null
     */
    public String getSelf() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public List<Message> getAllMessages() {
        // No time to figure out a better way to do this
        return this.getAll();
    }
}
