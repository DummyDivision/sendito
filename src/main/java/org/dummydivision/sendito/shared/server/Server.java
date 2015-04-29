package org.dummydivision.sendito.shared.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

/**
 * Class that represents a Server. The annotations take care of serialization.
 */
public class Server extends CouchDbDocument {

    // Name of the server
    private final String name;
    // URI to connect to the server
    @TypeDiscriminator
    private final String uri;

    /**
     * Creates a new Server with the given attributes.
     *
     * @param name Name of the server
     * @param url URI to connect to the server
     */
    @JsonCreator
    public Server(
            @JsonProperty("name") String name,
            @JsonProperty("uri") String url) {
        this.name = name;
        this.uri = url;
    }

    /**
     * Get the name of the server.
     *
     * @return Name of the Server
     */
    public String getName() {
        return name;
    }

    /**
     * Get the URI used to connect to the server.
     *
     * @return URI of the server
     */
    public String getUri() {
        return uri;
    }

    /**
     * Converts the server to a string representation. This is the same as
     * calling getName.
     *
     * @return String representation of the server
     */
    @Override
    public String toString() {
        return getName();
    }
}
