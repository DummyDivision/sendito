package org.dummydivision.sendito.shared.server;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;

@View(name = "all", map = "classpath:views/all.js")
public class ServerRepository extends CouchDbRepositorySupport<Server> {

    public ServerRepository(CouchDbConnector db) {
        super(Server.class, db);
        initStandardDesignDocument();
    }
}
