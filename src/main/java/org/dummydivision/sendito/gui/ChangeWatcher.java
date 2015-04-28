package org.dummydivision.sendito.gui;

import org.dummydivision.sendito.shared.message.Message;
import org.dummydivision.sendito.shared.message.MessageRepository;
import org.ektorp.changes.ChangesFeed;
import org.ektorp.changes.DocumentChange;

public class ChangeWatcher extends Thread {

    private final ChangesFeed feed;
    private final MessageRepository messageRepository;
    private final JMessagePane messagePane;

    public ChangeWatcher(ChangesFeed feed, MessageRepository m, JMessagePane messagePane) {
        this.feed = feed;
        this.messageRepository = m;
        this.messagePane = messagePane;
    }

    @Override
    public void run() {
        try {
            while (feed.isAlive()) {
                DocumentChange change = feed.next();
                if (change.getDocAsNode().has("body")) {
                    // Got new message
                    Message m = messageRepository.get(change.getId());
                    messagePane.addMessage(m);
                } else if (change.getDocAsNode().has("uri")) {
                    // TODO: Server list change
                } else {
                    System.out.println(change.getDoc());
                }
            }
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
    }
}
