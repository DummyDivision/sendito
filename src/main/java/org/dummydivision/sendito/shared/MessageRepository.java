package org.dummydivision.sendito.shared;

import java.util.Collections;
import java.util.List;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

public class MessageRepository extends CouchDbRepositorySupport<Message> {

    public MessageRepository(CouchDbConnector db) {
        super(Message.class, db);
        initStandardDesignDocument();
    }

    @Override
    public List<Message> getAll() {
        List<Message> messageList = super.getAll();
        Collections.sort(messageList);
        return messageList;
    }
}
