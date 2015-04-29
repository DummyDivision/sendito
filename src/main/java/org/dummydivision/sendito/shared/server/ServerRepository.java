package org.dummydivision.sendito.shared.server;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;

/**
 * Main collection class to manage Servers.
 */
@View(name = "all", map = "classpath:views/all.js") // Map function for grabbing all Servers from the DB
public class ServerRepository extends CouchDbRepositorySupport<Server> {

    /**
     * Create a new ServerRepository on the given database. This will
     * automatically setup the design documents and views.
     *
     * @param db Database connection to use
     */
    public ServerRepository(CouchDbConnector db) {
        // Call superconstructor
        super(Server.class, db);
        // Use default design document
        initStandardDesignDocument();
    }
}
