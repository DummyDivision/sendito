package org.dummydivision.sendito.shared;

import org.dummydivision.sendito.shared.server.Server;

public interface LoginVerifier {

    public Boolean checkLogin(Server s, String username, String password);
}
