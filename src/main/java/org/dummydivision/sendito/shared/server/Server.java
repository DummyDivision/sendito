package org.dummydivision.sendito.shared.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

public class Server extends CouchDbDocument {

    private final String name;
    @TypeDiscriminator
    private final String uri;

    // TODO: Change to private
    @JsonCreator
    public Server(
            @JsonProperty("name") String name,
            @JsonProperty("uri") String url) {
        this.name = name;
        this.uri = url;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return getName();
    }
}
