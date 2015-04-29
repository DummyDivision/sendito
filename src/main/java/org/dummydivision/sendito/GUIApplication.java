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

/**
 * The main application class. Implements all necessary sendito interfaces and
 * wires everything together.
 */
public class GUIApplication implements SenditoBasicClient, Runnable, SenditoBasicServer, LoginVerifier {

    // Name of the database to be used
    private static final String DB_NAME = "sendito";

    // Database connection
    private CouchDbConnector db;
    // List of available servers
    private final ServerList serverList;
    // Repository of messages
    private MessageRepository messageRepository;
    // Thread to watch the connection to the server
    private ChangeWatcher changeWatcher;
    // Main window GUI
    private final JConversationFrame conversationFrame;

    // User credentials
    private String username;
    private String password;

    /**
     * Creates a new GUIApplication using the defaults.
     */
    public GUIApplication() {
        this.db = null;
        this.serverList = new ServerList(new File("servers.list"));
        this.changeWatcher = null;
        this.conversationFrame = new JConversationFrame(this);
    }

    /**
     * Connect to a server. This will return null if no connection was
     * established. Will throw a DbAccessException if credentials are invalid.
     *
     * @param s Server to connect to
     * @param username Username to use
     * @param password Password to use
     * @return The connection to the database
     */
    public CouchDbConnector connect(Server s, String username, String password) {
        try {
            // Build a new client
            HttpClient httpClient = new StdHttpClient.Builder()
                    .url(s.getUri())
                    .username(username)
                    .password(password)
                    .build();
            CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
            CouchDbConnector db = new StdCouchDbConnector(DB_NAME, dbInstance);

            // Until here, no connection has been established
            try {
                db.getDbInfo(); // Try to get some basic info about the db

                // Everything worked, so we return the new connection
                return db;
            } catch (DbAccessException dae) {
                // Some kind of error occurred
                if (dae.getCause() == null) {
                    // Wrong username, so we pass the exception on
                    throw dae;
                } else {
                    //Something else, like a ConnectTimeoutException
                    return null;
                }
            }
        } catch (MalformedURLException ex) {
            // This will only occur if the server's uri is broken
            return null; // Should never happen
        }
    }

    /**
     * {@inheritDoc}
     */
    public Boolean checkLogin(Server s, String username, String password) {
        try {
            // Try to connect
            db = connect(s, username, password);
            if (db == null) {
                // Could not connect
                return null;
            } else {
                // Connection successful
                return Boolean.TRUE;
            }
        } catch (DbAccessException dae) {
            // Wrong credentials
            return Boolean.FALSE;
        }
    }

    /**
     * Run the application. This will block during the login screen.
     */
    public void run() {
        // Get a server from our list
        Server s = serverList.getServer();

        // Temporary credential storage
        String username = null;
        String password = null;

        // Establish a connection
        while (db == null) {
            // Show login dialog
            JLoginDialog loginDialog = new JLoginDialog("Sendito - Login");
            if (loginDialog.showDialog(username)) { // Blocking until the dialog is done
                username = loginDialog.getUsername();
                password = loginDialog.getPassword();

                // Try to login
                Boolean loggedIn = checkLogin(s, username, password);
                if (loggedIn == null) {
                    // No connection to the database, rotate server
                    if (!serverList.nextServer()) {
                        // We tried all available servers
                        JOptionPane.showMessageDialog(null, "Could not establish connection to a server.", "Sendito - Error", JOptionPane.ERROR_MESSAGE);
                        serverList.reset();
                        // This way, the user has another chance to retry
                    } else {
                        // Try the next server
                        s = serverList.getServer();
                    }
                } else if (!loggedIn) {
                    // Our login failed
                    JOptionPane.showMessageDialog(null, "The username or password you specified is invalid.", "Sendito - Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // User clicked cancel, exit program
                System.exit(0);
            }
        }

        // Store username and password
        this.username = username;
        this.password = password;

        // Trigger reconnection process
        onLostConnection();

        // Show main window
        conversationFrame.loadFromRepository(new MessageRepository(db));
        conversationFrame.setVisible(true);
    }

    /**
     * Notifies us of a lost connection. This will cause an attempt to reconnect
     * and then setup the keep-alive-mechanisms. Usually called from a
     * ChangeWatcher, so the old connection is instantly terminated.
     */
    public void onLostConnection() {
        // Terminate old connection
        db = null;

        // Disable the GUI
        conversationFrame.onLostConnection();

        Server s = serverList.getServer();
        // Reconnect
        while (db == null) {
            db = connect(s, username, password);
            if (db == null) {
                if (!serverList.nextServer()) {
                    // No more Servers to try
                    JOptionPane.showMessageDialog(null, "Could not establish connection to a server.", "Sendito - Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                } else {
                    // Try next server
                    s = serverList.getServer();
                }
            }
        }
        messageRepository = new MessageRepository(db);
        // Grab updated serverList
        serverList.updateFromRepository(new ServerRepository(db));

        // Initialize ChangesFeed
        ChangesCommand cmd = new ChangesCommand.Builder()
                .includeDocs(true)
                .continuous(true)
                .build();
        changeWatcher = new ChangeWatcher(db.changesFeed(cmd), this);
        changeWatcher.start();

        // Re-Enable the GUI
        conversationFrame.onRestoreConnection();
    }

    /**
     * Called when a new message has arrived.
     *
     * @param id The unique id of the new message
     */
    public void onAddMessage(String id) {
        conversationFrame.onAddMessage(id);
    }

    /**
     * Called when a new server has been added.
     *
     * @param id The unique id of the new server
     */
    public void onAddServer(String id) {
        // Add the server to our list
        ServerRepository repository = new ServerRepository(db);
        serverList.addServer(repository.get(id));

        conversationFrame.onAddServer(id); // Does nothing right now
    }

    /**
     * {@inheritDoc}
     */
    public Message sendMessage(String message) {
        return messageRepository.sendMessage(message);
    }

    /**
     * {@inheritDoc}
     */
    public Message getMessage(String id) {
        return messageRepository.getMessage(id);
    }

    /**
     * {@inheritDoc}
     */
    public String getSelf() {
        return username;
    }

    /**
     * Starts the application.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        GUIApplication application = new GUIApplication();
        application.run();
    }
}
