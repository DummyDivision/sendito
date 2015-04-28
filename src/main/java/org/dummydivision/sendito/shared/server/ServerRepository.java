package org.dummydivision.sendito.shared.server;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

public class ServerRepository extends CouchDbRepositorySupport<Server> {

    public ServerRepository(CouchDbConnector db) {
        super(Server.class, db);
        initStandardDesignDocument();
    }
}
