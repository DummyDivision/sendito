package org.dummydivision.sendito.shared.message;

import java.util.Collections;
import java.util.List;
import org.ektorp.CouchDbConnector;
import org.ektorp.UpdateHandlerRequest;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.UpdateHandler;

@UpdateHandler(name = "send", file = "updatehandlers/send.js")
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

    @Deprecated
    @Override
    public void add(Message entity) {
        throw new UnsupportedOperationException("Do not use this method!");
    }
    
    public Message add(String message) {
        return get(db.callUpdateHandler(new UpdateHandlerRequest().designDocId(stdDesignDocumentId).functionName("send").docId("null").param("body", message)));
    }
}
