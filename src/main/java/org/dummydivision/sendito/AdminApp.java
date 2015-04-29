package org.dummydivision.sendito;

import java.io.File;
import java.net.MalformedURLException;
import org.dummydivision.sendito.shared.message.MessageRepository;
import org.dummydivision.sendito.shared.server.Server;
import org.dummydivision.sendito.shared.server.ServerList;
import org.dummydivision.sendito.shared.server.ServerRepository;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

/**
 * Temporary main class to setup a database. Usually this should not be used.
 */
public class AdminApp {

    private static final String NAME = "sendito-master";
    private static final String URI = "http://46.101.145.49";

    /**
     * Starts the application.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        try {
            HttpClient httpClient = new StdHttpClient.Builder()
                    .url(URI)
                    .build();

            CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
            CouchDbConnector db = new StdCouchDbConnector("sendito", dbInstance);

            db.createDatabaseIfNotExists();

            MessageRepository messageRepository = new MessageRepository(db);

            ServerRepository serverRepository = new ServerRepository(db);
            serverRepository.add(new Server(NAME, URI));

            ServerList list = new ServerList(new File("servers.list"));
            list.updateFromRepository(serverRepository);
        } catch (MalformedURLException ex) {
            System.out.println(ex);
        }
    }
}
