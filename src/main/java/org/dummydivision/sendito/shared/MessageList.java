package org.dummydivision.sendito.shared;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

public class MessageList extends CouchDbRepositorySupport<Message> {

    public MessageList(CouchDbConnector db) {
        super(Message.class, db);
        initStandardDesignDocument();
    }
}
