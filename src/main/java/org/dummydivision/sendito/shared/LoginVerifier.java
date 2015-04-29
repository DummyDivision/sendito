package org.dummydivision.sendito.shared;

import org.dummydivision.sendito.shared.server.Server;

/**
 * Interface to enable verification of login credentials.
 */
public interface LoginVerifier {

    /**
     * Check the given credentials. If no connection can be established, null
     * will be returned!
     *
     * @param s Server to connect to
     * @param username Username to check
     * @param password Password to check
     * @return True if the credentials are valid, false otherwise. null if
     * connection can't be established!
     */
    public Boolean checkLogin(Server s, String username, String password);
}
