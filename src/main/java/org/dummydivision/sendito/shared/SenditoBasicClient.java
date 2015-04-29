package org.dummydivision.sendito.shared;

/**
 * Second interface for interacting with sendito. This should be implemented by
 * the main application and be used to notify it about any events that occured.
 */
public interface SenditoBasicClient {

    /**
     * Notifies the client of a lost connection.
     */
    public void onLostConnection();

    /**
     * Notifies the client of a new message.
     *
     * @param id The unique id of the new message
     */
    public void onAddMessage(String id);

    /**
     * Notifies the client of a new server.
     *
     * @param id The unique id of the new server
     */
    public void onAddServer(String id);
}
