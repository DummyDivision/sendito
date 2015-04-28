package org.dummydivision.sendito.shared;

import org.ektorp.changes.ChangesFeed;
import org.ektorp.changes.DocumentChange;

public class ChangeWatcher extends Thread {

    private final ChangesFeed feed;
    private final SenditoBasicClient basicClient;

    public ChangeWatcher(ChangesFeed feed, SenditoBasicClient basicClient) {
        this.feed = feed;
        this.basicClient = basicClient;
    }

    @Override
    public void run() {
        try {
            while (feed.isAlive()) {
                DocumentChange change = feed.next();
                if (!change.isDeleted()) {
                    if (change.getDocAsNode().has("body")) {
                        // Got new message
                        basicClient.onAddMessage(change.getId());
                    } else if (change.getDocAsNode().has("uri")) {
                        // Got a new server
                        basicClient.onAddServer(change.getId());
                    } else {
                        // We don't know what it is, so we print it
                        System.out.println(change.getDoc());
                    }
                } else {
                    // Something got deleted. We ignore this
                    System.out.println(change.toString());
                }
            }
        } catch (InterruptedException ex) {
            basicClient.onLostConnection();
        }
    }
}
