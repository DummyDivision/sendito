package org.dummydivision.sendito.shared;

import org.ektorp.changes.ChangesFeed;
import org.ektorp.changes.DocumentChange;

/**
 * Thread to keep the connection to the database alive and watch for changes.
 * The thread will be destroyed if the connection breaks down.
 */
public class ChangeWatcher extends Thread {

    // ChangesFeed to watch
    private final ChangesFeed feed;
    // The sendito component to notify of changes
    private final SenditoBasicClient basicClient;

    /**
     * Construct a new thread to watch the given ChangesFeed. The client will be
     * stored and cannot be changed!
     *
     * @param feed The ChangesFeed to watch
     * @param basicClient Client to notify if a change occurs
     */
    public ChangeWatcher(ChangesFeed feed, SenditoBasicClient basicClient) {
        this.feed = feed;
        this.basicClient = basicClient;
    }

    /**
     * Watch for changes. Will terminate the thread when the connection is lost.
     */
    @Override
    public void run() {
        try {
            // Make sure our connection is still alive
            while (feed.isAlive()) {
                DocumentChange change = feed.next(); // Will block until a new change is found
                if (!change.isDeleted()) { // Ignore delete-events
                    if (change.getDocAsNode().has("body")) {
                        // Got new message
                        basicClient.onAddMessage(change.getId());
                    } else if (change.getDocAsNode().has("uri")) {
                        // Got a new server
                        basicClient.onAddServer(change.getId());
                    } else {
                        // We don't know what it is, so we print and ignore it
                        System.out.println(change.getDoc());
                    }
                } else {
                    // Something got deleted. We ignore this
                    System.out.println(change.toString());
                }
            }
        } catch (InterruptedException ex) {
            // We lost our connection, so we notify our client and exit afterwards
            basicClient.onLostConnection();
        }
    }
}
