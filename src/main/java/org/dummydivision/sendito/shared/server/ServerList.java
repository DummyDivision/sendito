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

public class ServerList {

    private final List<Server> unreachableServers;
    private final Queue<Server> availableServers;
    private final File settingsFile;

    public ServerList(File settingsFile) {
        this.unreachableServers = new LinkedList<Server>();
        this.availableServers = new LinkedList<Server>();
        this.settingsFile = settingsFile;

        loadFromFile(settingsFile);
    }

    public Server getServer() {
        return availableServers.element();
    }

    public boolean nextServer() {
        unreachableServers.add(availableServers.poll());
        return availableServers.size() != 0;
    }

    private void loadFromFile(File settingsFile) {
        try {
            settingsFile.createNewFile();
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(settingsFile));
            try {
                Server[] storedList = (Server[]) stream.readObject();
                for (Server s : storedList) {
                    availableServers.add(s);
                }
                stream.close();
            } catch (ClassNotFoundException ex) {
                System.out.println(ex);
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    private void saveToFile() {
        try {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(settingsFile));
            HashMap<String, Server> cleanedList = new HashMap<String, Server>();
            for (Server s : unreachableServers) {
                cleanedList.put(s.getName(), s);
            }
            for (Server s : availableServers) {
                cleanedList.put(s.getName(), s);
            }
            Server[] servers = cleanedList.values().toArray(new Server[cleanedList.size()]);
            stream.writeObject(servers);
            stream.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void updateFromRepository(ServerRepository repository) {
        List<Server> servers = repository.getAll();
        for (Server s : servers) {
            addServer(s);
        }
    }

    public void addServer(Server s) {
        availableServers.add(s);

        saveToFile();
    }

    public void reset() {
        for (Server s : unreachableServers) {
            availableServers.add(s);
        }

        unreachableServers.clear();
    }
}
