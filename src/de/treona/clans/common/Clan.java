package de.treona.clans.common;

import java.util.List;
import java.util.UUID;

public interface Clan {

    int getClanId();

    String getClanName();

    String getClanTag();

    int getWins();

    int getLosses();

    List<UUID> getMembers();

    UUID getOwner();

    int getElo();
}
