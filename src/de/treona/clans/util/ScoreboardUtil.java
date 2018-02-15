package de.treona.clans.util;

import de.treona.clans.Clans;
import de.treona.clans.common.Clan;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardUtil {

    public static void updateScoreboard(Player player){
        Bukkit.getScheduler().runTaskAsynchronously(Clans.getPlugin(), () -> {
            Scoreboard scoreboard = player.getScoreboard();
            Clan clan = Clans.getClan(player);
            if(clan == null){
                return;
            }
            Team team = scoreboard.getTeam(clan.getClanTag());
            if(team == null){
                team = scoreboard.registerNewTeam(clan.getClanTag());
            }
            team.setPrefix(ChatColor.GRAY + "[" + ChatColor.AQUA + clan.getClanTag() + ChatColor.GRAY + "] ");
            team.addEntry(player.getName());
            Bukkit.getOnlinePlayers().forEach(streamPlayer -> streamPlayer.setScoreboard(scoreboard));
        });
    }
}
