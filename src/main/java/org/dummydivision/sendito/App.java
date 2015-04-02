package org.dummydivision.sendito;

import java.net.MalformedURLException;
import org.dummydivision.sendito.gui.JConversationFrame;
import org.dummydivision.sendito.shared.MessageRepository;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

public class App {

    public static void main(String[] args) {
        try {
            System.out.println("Hello World!");
            HttpClient httpClient = new StdHttpClient.Builder()
                    .url("http://localhost:5984")
                    .build();

            CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
            CouchDbConnector db = new StdCouchDbConnector("sendito", dbInstance);

            db.createDatabaseIfNotExists();

            MessageRepository messageRepository = new MessageRepository(db);
            JConversationFrame conversation = new JConversationFrame(messageRepository, "Dominik", "Jasmin");
            //messageRepository.add(new Message("Dominik", "Jasmin", "Hallo Jasmin!"));

        } catch (MalformedURLException ex) {
            System.out.println(ex);
        }
    }
}
