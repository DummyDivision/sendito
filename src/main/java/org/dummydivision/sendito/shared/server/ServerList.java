package org.dummydivision.sendito.shared.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Helper class to load and rotate the list of servers.
 */
public class ServerList {

    // Servers that we have not managed to reach lately
    private final List<Server> unreachableServers;
    // Servers that we will try next
    private final Queue<Server> availableServers;
    // File to cache our server list in
    private final File settingsFile;

    /**
     * Create a new ServerList and load the servers from the given settingsFile.
     * The settingsFile will be stored and cannot be changed!
     *
     * @param settingsFile Place to store the server list in
     */
    public ServerList(File settingsFile) {
        this.unreachableServers = new LinkedList<Server>();
        this.availableServers = new LinkedList<Server>();
        this.settingsFile = settingsFile;

        loadFromFile(settingsFile);
    }

    /**
     * Gets the server that should currently be used. If there is no Server,
     * null will be returned.
     *
     * @return The server to use currently
     */
    public Server getServer() {
        return availableServers.peek();
    }

    /**
     * Rotate to the next server. Marks the current server as unreachable.
     *
     * @return False if there are no more servers
     */
    public boolean nextServer() {
        // Move current server to unreachable servers
        unreachableServers.add(availableServers.poll());

        // See if there are more servers available
        return availableServers.size() != 0;
    }

    /*
     * Loads the serverList from the settingsFile.
     *
     * @param settingsFile The File to load the serverList from
     */
    private void loadFromFile(File settingsFile) {
        try {
            settingsFile.createNewFile(); // Create the file if it does not exist
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(settingsFile));
            try {
                // Read from file
                Server[] storedList = (Server[]) stream.readObject();
                for (Server s : storedList) {
                    // Add to available server list
                    availableServers.add(s);
                }
                stream.close();
            } catch (ClassNotFoundException ex) {
                // Should never occur
                System.out.println(ex);
            }
        } catch (IOException ex) {
            // In this case, we don't have any servers
            System.out.println(ex);
        }
    }

    /*
     * Stores the serverList in the settingsFile. This will include unreachable
     * servers, but make sure that there are no duplicates through filtering by
     * name.
     */
    private void saveToFile() {
        try {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(settingsFile));

            // Consolidate the currently stored servers into one HashMap
            HashMap<String, Server> cleanedList = new HashMap<String, Server>();
            for (Server s : unreachableServers) {
                cleanedList.put(s.getName(), s);
            }
            for (Server s : availableServers) {
                cleanedList.put(s.getName(), s);
            }

            // Now convert the duplicate-free list to an array
            Server[] servers = cleanedList.values().toArray(new Server[cleanedList.size()]);
            // Store the array in our settingsFile
            stream.writeObject(servers);
            stream.close();
        } catch (IOException ex) {
            // If we can't write the serverlist for some reason, nothing happens
            System.out.println(ex);
        }
    }

    /**
     * Loads the serverList from the given Repository. This will cause the List
     * to be saved.
     *
     * @param repository The ServerRepository to load Servers from
     */
    public void updateFromRepository(ServerRepository repository) {
        // Read all servers and add them to our list
        List<Server> servers = repository.getAll();
        for (Server s : servers) {
            availableServers.add(s); // Do this directly so we don't save everytime
        }

        // Store the new servers
        saveToFile();
    }

    /**
     * Adds a server to the list. This will cause the List to be saved.
     *
     * @param s The Server to add
     */
    public void addServer(Server s) {
        availableServers.add(s);

        saveToFile();
    }

    /**
     * Mark all unreachable servers as reachable.
     */
    public void reset() {
        // Add all unreachable servers to the available servers list
        for (Server s : unreachableServers) {
            availableServers.add(s);
        }

        // Remove the added servers
        unreachableServers.clear();

        // No need to save here, as no new servers have been added
    }
}
