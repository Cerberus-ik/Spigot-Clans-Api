package de.treona.clans.config;

public interface Config {

    String getDbHost();

    String getDbUser();

    String getDbPassword();

    String getDbDb();

    int getDbPort();

    int getBaseElo();

    boolean setClanTagTabPrefix();

    int getEloK();

    boolean flushScoreboardOnJoin();

}
