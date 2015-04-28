package org.dummydivision.sendito.shared;

import org.dummydivision.sendito.shared.message.Message;

public interface SenditoBasicServer {

    public Message sendMessage(String message);

    public Message getMessage(String id);

    public String getSelf();
}
