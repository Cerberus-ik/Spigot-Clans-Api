package de.treona.clans.managers;

import de.treona.clans.common.Clan;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public interface ScoreboardManager {

    void removeClan(Clan clan);

    void registerClan(Clan clan);

    void removePlayer(Clan clan, OfflinePlayer offlinePlayer);

    void removePlayer(Clan clan, Player player);

    void removePlayer(Clan clan, String entry);

    void addPlayer(Clan clan, OfflinePlayer player);

    void addPlayer(Clan clan, Player player);

    void addPlayer(Clan clan, String entry);

    Scoreboard getScoreboard();
}
