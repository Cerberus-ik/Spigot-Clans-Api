package de.treona.clans.listener;

import de.treona.clans.util.ScoreboardUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ScoreboardUpdateListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Bukkit.getOnlinePlayers().forEach(ScoreboardUtil::updateScoreboard);
    }
}
