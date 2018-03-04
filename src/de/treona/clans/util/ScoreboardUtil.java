package de.treona.clans.util;

import de.treona.clans.Clans;
import de.treona.clans.common.Clan;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public class ScoreboardUtil {

    @Deprecated
    public static void removeClan(Clan clan) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam(clan.getClanTag());
        if (team == null) {
            return;
        }
        team.unregister();
    }

    @Deprecated
    public static void updateScoreboard(Clan clan) {
        Bukkit.getScheduler().runTaskAsynchronously(Clans.getPlugin(), () -> {
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
            Team team = scoreboard.getTeam(clan.getClanTag());
            if (team == null) {
                team = scoreboard.registerNewTeam(clan.getClanTag());
            }
            team.setPrefix(ChatColor.GRAY + "[" + ChatColor.AQUA + clan.getClanTag() + ChatColor.GRAY + "] ");
            for (UUID uuid : clan.getMembers()) {
                if(team.hasEntry(Bukkit.getOfflinePlayer(uuid).getName())){
                    continue;
                }
                team.addEntry(Bukkit.getOfflinePlayer(uuid).getName());
            }

            for (Team otherTeam : scoreboard.getTeams()) {
                if(otherTeam.equals(team)){
                    continue;
                }
                for (UUID uuid : clan.getMembers()) {
                    if(team.hasEntry(Bukkit.getOfflinePlayer(uuid).getName())){
                        team.removeEntry(Bukkit.getOfflinePlayer(uuid).getName());
                    }
                }
            }
        });
    }

    public static void flushScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        scoreboard.getTeams().forEach(Team::unregister);
        scoreboard.clearSlot(DisplaySlot.PLAYER_LIST);
        player.setScoreboard(scoreboard);
    }

    public static void updateScoreboard(){
        if(Clans.getConfigManager().getConfig().setClanTagTabPrefix()){
            Scoreboard scoreboard = Clans.getClansScoreboard();
            Bukkit.getOnlinePlayers().forEach(player -> player.setScoreboard(scoreboard));
        }
    }
}
