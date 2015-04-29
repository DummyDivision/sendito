package org.dummydivision.sendito;

import java.io.File;
import java.net.MalformedURLException;
import javax.swing.JOptionPane;
import org.dummydivision.sendito.gui.JConversationFrame;
import org.dummydivision.sendito.gui.JLoginDialog;
import org.dummydivision.sendito.shared.ChangeWatcher;
import org.dummydivision.sendito.shared.LoginVerifier;
import org.dummydivision.sendito.shared.SenditoBasicClient;
import org.dummydivision.sendito.shared.SenditoBasicServer;
import org.dummydivision.sendito.shared.message.Message;
import org.dummydivision.sendito.shared.message.MessageRepository;
import org.dummydivision.sendito.shared.server.Server;
import org.dummydivision.sendito.shared.server.ServerList;
import org.dummydivision.sendito.shared.server.ServerRepository;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.DbAccessException;
import org.ektorp.changes.ChangesCommand;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

public class GUIApplication implements SenditoBasicClient, Runnable, SenditoBasicServer, LoginVerifier {

    private static final String DB_NAME = "couchdb_sendito_ektorp";

    private CouchDbConnector db;
    private final ServerList serverList;
    private MessageRepository messageRepository;
    private ChangeWatcher changeWatcher;
    private final JConversationFrame conversationFrame;

    private String username;
    private String password;

    public GUIApplication() {
        this.db = null;
        this.serverList = new ServerList(new File("servers.list"));
        this.changeWatcher = null;
        this.conversationFrame = new JConversationFrame(this);
    }

    public CouchDbConnector connect(Server s, String username, String password) {
        try {
            HttpClient httpClient = new StdHttpClient.Builder()
                    .url(s.getUri())
                    .username(username)
                    .password(password)
                    .build();
            CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
            CouchDbConnector db = new StdCouchDbConnector(DB_NAME, dbInstance);
            try {
                db.getDbInfo();
                return db;
            } catch (DbAccessException dae) {
                if (dae.getCause() == null) {
                    // Wrong username
                    throw dae;
                } else {
                    //ConnectTimeoutException
                    return null;
                }
            }
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    public Boolean checkLogin(Server s, String username, String password) {
        try {
            db = connect(s, username, password);
            if (db == null) {
                return null;
            } else {
                return Boolean.TRUE;
            }
        } catch (DbAccessException dae) {
            return Boolean.FALSE;
        }
    }

    public void run() {
        Server s = serverList.getServer();
        String username = null;
        String password = null;

        // Establish a connection
        while (db == null) {
            // Show login dialog
            JLoginDialog loginDialog = new JLoginDialog("Sendito - Login");
            if (loginDialog.showDialog(username)) {
                username = loginDialog.getUsername();
                password = loginDialog.getPassword();

                // Try to login
                Boolean loggedIn = checkLogin(s, username, password);
                if (loggedIn == null) {
                    // No connection to the database, rotate server
                    if (!serverList.nextServer()) {
                        // We tried all available servers
                        JOptionPane.showMessageDialog(null, "Could not establish connection to a server.", "Sendito - Error", JOptionPane.ERROR_MESSAGE);
                    }
                    s = serverList.getServer();
                } else if (!loggedIn) {
                    // Our login failed
                    JOptionPane.showMessageDialog(null, "The username or password you specified is invalid.", "Sendito - Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // User clicked cancel
                System.exit(0);
            }
        }

        // Store username and password
        this.username = username;
        this.password = password;

        onLostConnection();

        // Initialize ChangesFeed
        ChangesCommand cmd = new ChangesCommand.Builder()
                .includeDocs(true)
                .continuous(true)
                .build();
        changeWatcher = new ChangeWatcher(db.changesFeed(cmd), this);
        changeWatcher.start();

        // Show main window
        conversationFrame.loadFromRepository(new MessageRepository(db));
        conversationFrame.setVisible(true);
    }

    public void onLostConnection() {
        db = null;
        
        // Disable the GUI
        conversationFrame.onLostConnection();

        Server s = serverList.getServer();
        // Reconnect
        while (db == null) {
            db = connect(s, username, password);
            if (db == null) {
                if (!serverList.nextServer()) {
                    JOptionPane.showMessageDialog(null, "Could not establish connection to a server.", "Sendito - Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                } else {
                    s = serverList.getServer();
                }
            }
        }        
        messageRepository = new MessageRepository(db);
        serverList.updateFromRepository(new ServerRepository(db));

        // Re-Enable the GUI
        conversationFrame.onRestoreConnection();
    }

    public void onAddMessage(String id) {
        conversationFrame.onAddMessage(id);
    }

    public void onAddServer(String id) {
        ServerRepository repository = new ServerRepository(db);
        serverList.addServer(repository.get(id));
        
        conversationFrame.onAddServer(id);
    }

    public Message sendMessage(String message) {
        return messageRepository.sendMessage(message);
    }

    public static void main(String[] args) {
        GUIApplication application = new GUIApplication();
        application.run();
    }

    public Message getMessage(String id) {
        return messageRepository.getMessage(id);
    }

    public String getSelf() {
        return username;
    }
}
