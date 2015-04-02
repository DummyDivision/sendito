package org.dummydivision.sendito.shared;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

public class MessageRepository extends CouchDbRepositorySupport<Message> {

    public MessageRepository(CouchDbConnector db) {
        super(Message.class, db);
        initStandardDesignDocument();
    }
}
