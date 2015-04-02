package org.dummydivision.sendito;

import java.net.MalformedURLException;
import java.util.List;
import org.dummydivision.sendito.gui.JConversationFrame;
import org.dummydivision.sendito.shared.Message;
import org.dummydivision.sendito.shared.MessageRepository;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

/**
 * Hello world!
 *
 */
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

            /*Sofa sofa = db.get(Sofa.class, "ektorp");
             sofa.setColor("blue");
             db.update(sofa);*/
            MessageRepository messageRepository = new MessageRepository(db);
            List<Message> messageList = messageRepository.getAll();
            JConversationFrame conversation = new JConversationFrame(messageList, "Jasmin");
            //messageRepository.add(new Message("Dominik", "Jasmin", "Hallo Jasmin!"));

        } catch (MalformedURLException ex) {
            System.out.println(ex);
        }
    }
}
