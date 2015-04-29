package org.dummydivision.sendito.shared.message;

import java.util.Collections;
import java.util.List;
import org.dummydivision.sendito.shared.SenditoBasicServer;
import org.ektorp.CouchDbConnector;
import org.ektorp.UpdateHandlerRequest;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.UpdateHandler;
import org.ektorp.support.View;

@View(name = "all", map = "classpath:views/all.js")
@UpdateHandler(name = "send", file = "updatehandlers/send.js")
public class MessageRepository extends CouchDbRepositorySupport<Message> implements SenditoBasicServer {

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

    public Message sendMessage(String message) {
        UpdateHandlerRequest sendMessage = new UpdateHandlerRequest()
                .designDocId(stdDesignDocumentId)
                .functionName("send")
                .docId("null")
                .param("body", message);
        String id = db.callUpdateHandler(sendMessage);
        return get(id);
    }

    public Message getMessage(String id) {
        return get(id);
    }

    public String getSelf() {
        return null;
    }
}
