package de.treona.clans.db;

public interface DatabaseCredentials {

    int getPort();

    String getHost();

    String getDatabase();

    String getUser();

    String getPassword();
}
