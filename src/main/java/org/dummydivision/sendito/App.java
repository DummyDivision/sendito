package org.dummydivision.sendito;

import java.net.MalformedURLException;
import org.dummydivision.sendito.gui.JConversationFrame;
import org.dummydivision.sendito.gui.JLoginDialog;
import org.dummydivision.sendito.shared.message.MessageRepository;
import org.dummydivision.sendito.shared.server.ServerRepository;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

public class App {

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
        } catch (MalformedURLException ex) {
            System.out.println(ex);
        }
    }

    public static void main(String[] args) {
        try {
            initializeDatabase();

            JLoginDialog loginDialog = new JLoginDialog();
            loginDialog.setVisible(true);

            System.out.println("Logging in...");
            HttpClient httpClient = new StdHttpClient.Builder()
                    .url("http://localhost:5984")
                    .username(loginDialog.getUsername())
                    .password(loginDialog.getPassword())
                    .build();
            CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
            CouchDbConnector db = new StdCouchDbConnector("couchdb_sendito_ektorp", dbInstance);
            MessageRepository messageRepository = new MessageRepository(db);
            JConversationFrame conversation = new JConversationFrame(messageRepository, loginDialog.getUsername());
        } catch (MalformedURLException ex) {
            System.out.println(ex);
        }
    }
}
