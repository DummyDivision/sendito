package org.dummydivision.sendito.shared;

public interface SenditoBasicClient {

    public void onLostConnection();

    public void onAddMessage(String id);

    public void onAddServer(String id);
}
