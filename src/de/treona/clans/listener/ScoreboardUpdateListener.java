package de.treona.clans.listener;

import de.treona.clans.Clans;
import de.treona.clans.util.ScoreboardUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@SuppressWarnings("unused")
public class ScoreboardUpdateListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event){
        if(Clans.getConfigManager().getConfig().flushScoreboardOnJoin()){
            Bukkit.getOnlinePlayers().forEach(ScoreboardUtil::flushScoreboard);
        }

        if(Clans.getConfigManager().getConfig().setClanTagTabPrefix()){
            Bukkit.getOnlinePlayers().forEach(ScoreboardUtil::updateScoreboard);
        }
    }
}
