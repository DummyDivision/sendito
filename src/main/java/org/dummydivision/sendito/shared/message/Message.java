package org.dummydivision.sendito.shared.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

public class Message extends CouchDbDocument implements Comparable<Message> {

    private final String sender;
    private final Date dateSent;
    @TypeDiscriminator
    private final String body;

    @JsonCreator
    private Message(
            @JsonProperty("sender") String sender,
            @JsonProperty("dateSent") Date dateSent,
            @JsonProperty("body") String body) {
        this.sender = sender;
        this.dateSent = dateSent;
        this.body = body;
    }

    public Message(String message) {
        this.sender = null;
        this.dateSent = null;
        this.body = message;
    }

    public String getBody() {
        return body;
    }

    public String getSender() {
        return sender;
    }

    public Date getDateSent() {
        return dateSent;
    }

    @Override
    public String toString() {
        return String.format("%s (%s):\n%s", sender, dateSent, body);
    }

    public int compareTo(Message t) {
        return dateSent.compareTo(t.dateSent);
    }
}
