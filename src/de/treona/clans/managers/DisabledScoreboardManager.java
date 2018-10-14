package de.treona.clans.managers;

import de.treona.clans.common.Clan;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class DisabledScoreboardManager implements ScoreboardManager {

    @Override
    public void removeClan(Clan clan) {
    }

    @Override
    public void registerClan(Clan clan) {
    }

    @Override
    public void removePlayer(Clan clan, OfflinePlayer offlinePlayer) {
    }

    @Override
    public void removePlayer(Clan clan, Player player) {
    }

    @Override
    public void removePlayer(Clan clan, String entry) {
    }

    @Override
    public void addPlayer(Clan clan, OfflinePlayer player) {
    }

    @Override
    public void addPlayer(Clan clan, Player player) {
    }

    @Override
    public void addPlayer(Clan clan, String entry) {
    }

    @Override
    public Scoreboard getScoreboard() {
        return Bukkit.getScoreboardManager().getNewScoreboard();
    }
}
