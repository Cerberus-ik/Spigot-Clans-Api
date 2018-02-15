package de.treona.clans.config;

public interface Config {

    String getDbHost();

    String getDbUser();

    String getDbPassword();

    String getDbDb();

    int getDbPort();

    int getBaseElo();

    boolean getSetClanTagTabPrefix();

    int getEloK();

}
