package org.dummydivision.sendito.shared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

public class Message extends CouchDbDocument {

    @TypeDiscriminator
    private final String sender;
    private final String reciever;
    private final Date dateSent;
    private final String body;

    public Message(String sender, String reciever, String body) {
        this.sender = sender;
        this.reciever = reciever;
        this.dateSent = new Date();
        this.body = body;
    }

    @JsonCreator
    private Message(
            @JsonProperty("sender") String sender,
            @JsonProperty("reciever") String reciever,
            @JsonProperty("dateSent") Date dateSent,
            @JsonProperty("body") String body) {
        this.sender = sender;
        this.reciever = reciever;
        this.dateSent = dateSent;
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

    public Date getDateSent() {
        return dateSent;
    }

    @Override
    public String toString() {
        return String.format("%s (%s):\n%s", sender, dateSent, body);
    }
}
