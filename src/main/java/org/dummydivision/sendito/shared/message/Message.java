package org.dummydivision.sendito.shared.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

/**
 * Class that represents a Message. The annotations take care of serialization.
 */
public class Message extends CouchDbDocument implements Comparable<Message> {

    // Sender of the message
    private final String sender;
    // Timestamp of the message
    private final Date dateSent;
    // Body of the message
    @TypeDiscriminator
    private final String body;

    /*
     * Creates a new Message with the given attributes. This will only be called
     * by the deserialization process!
     *
     * @param sender Sender of the message
     * @param dateSent Timestamp of the message
     * @param body Body of the message
     */
    @JsonCreator
    private Message(
            @JsonProperty("sender") String sender,
            @JsonProperty("dateSent") Date dateSent,
            @JsonProperty("body") String body) {
        this.sender = sender;
        this.dateSent = dateSent;
        this.body = body;
    }

    /**
     * Creates a new message with the given body. The resulting message object
     * should be passed to a SenditoBasicServer, as most attributes are null.
     *
     * @param body Body of the message
     */
    public Message(String body) {
        this.sender = null;
        this.dateSent = null;
        this.body = body;
    }

    /**
     * Get the message's body. This is its main content.
     *
     * @return Body of the message
     */
    public String getBody() {
        return body;
    }

    /**
     * Get the name of the message's sender.
     *
     * @return Sender of the message
     */
    public String getSender() {
        return sender;
    }

    /**
     * Get the time the message was sent.
     *
     * @return Timestamp of the message
     */
    public Date getDateSent() {
        return dateSent;
    }

    /**
     * Converts the message to a string representation.
     *
     * @return String representation of the message
     */
    @Override
    public String toString() {
        return String.format("%s (%s):\n%s", sender, dateSent, body);
    }

    /**
     * Compares two messages. This is the same as comparing their dates.
     *
     * @param t Message to compare to
     * @return Result of comparing the dates
     */
    public int compareTo(Message t) {
        return dateSent.compareTo(t.dateSent);
    }
}
