package org.dummydivision.sendito.shared;

import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;

public class Message extends CouchDbDocument {

    @TypeDiscriminator
    private final String sender;
    private final String reciever;
    private final DateTime dateSent;
    private final String body;

    public Message(String sender, String reciever, String body) {
        this.sender = sender;
        this.reciever = reciever;
        this.dateSent = new DateTime();
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public String getSender() {
        return sender;
    }

    public String getReciever() {
        return reciever;
    }

    public DateTime getDateSent() {
        return dateSent;
    }
}
