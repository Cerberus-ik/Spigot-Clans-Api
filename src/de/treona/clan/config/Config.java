package de.treona.clan.config;

public interface Config {

    String getDbHost();

    String getDbUser();

    String getDbPassword();

    String getDbDb();

    int getDbPort();

    int getBaseElo();

    int getEloK();

}
