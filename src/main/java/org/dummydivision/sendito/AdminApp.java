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

public class AdminApp {

    private static void initializeDatabase() {
        try {
            System.out.println("Creating DB...");
            HttpClient httpClient = new StdHttpClient.Builder()
                    .url("http://localhost:5984")
                    .build();

            CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
            CouchDbConnector db = new StdCouchDbConnector("couchdb_sendito_ektorp", dbInstance);
            db.createDatabaseIfNotExists();
            MessageRepository messageRepository = new MessageRepository(db);
            ServerRepository serverRepository = new ServerRepository(db);
            serverRepository.add(new Server("Localhost", "http://localhost:5984"));
            ServerList list = new ServerList(new File("servers.list"));
            list.updateFromRepository(serverRepository);
        } catch (MalformedURLException ex) {
            System.out.println(ex);
        }
    }

    public static void main(String[] args) {
        initializeDatabase();
    }
}
