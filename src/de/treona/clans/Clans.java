package de.treona.clans;

import de.treona.clans.commands.ClanCommand;
import de.treona.clans.common.Clan;
import de.treona.clans.config.ConfigManager;
import de.treona.clans.db.DatabaseCredentials;
import de.treona.clans.db.DatabaseManager;
import de.treona.clans.listener.ScoreboardUpdateListener;
import de.treona.clans.managers.DisabledScoreboardManager;
import de.treona.clans.managers.EnabledScoreboardManager;
import de.treona.clans.managers.InviteManager;
import de.treona.clans.managers.ScoreboardManager;
import de.treona.clans.util.ScoreboardUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public class Clans extends JavaPlugin{

    public static final String PREFIX_COLOR = ChatColor.GRAY + "[" + ChatColor.DARK_AQUA + "Clan" + ChatColor.GRAY + "]" + ChatColor.RESET;
    public static final String PREFIX = "[Clan]";
    private static InviteManager inviteManager;
    private static JavaPlugin plugin;
    private static ConfigManager configManager;
    private static DatabaseManager databaseManager;
    private static ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        super.getCommand("Clan").setExecutor(new ClanCommand(this));
        plugin = this;
        configManager = new ConfigManager(this);
        configManager.loadConfig();
        databaseManager = new DatabaseManager(this.getDatabaseCredentials());
        inviteManager = new InviteManager(this);
        if(!databaseManager.canConnect()){
            super.getLogger().warning("Can't connect to the database. Are the credentials correctly set?");
            super.getLogger().warning("This plugin requires a database! Shutting down...");
            super.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if(!databaseManager.doesTableExist()){
            super.getLogger().info("The clans table does not exist, creating it...");
            databaseManager.createTable();
            super.getLogger().info("Finished initializing.");
        }

        if(configManager.getConfig().setClanTagTabPrefix()){
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                scoreboardManager = new EnabledScoreboardManager(this);
                Bukkit.getPluginManager().registerEvents(new ScoreboardUpdateListener(), this);
                ScoreboardUtil.updateScoreboard();
            });
        }else
            scoreboardManager = new DisabledScoreboardManager();
    }

    private DatabaseCredentials getDatabaseCredentials(){
        return new DatabaseCredentials() {
            @Override
            public int getPort() {
                return configManager.getConfig().getDbPort();
            }

            @Override
            public String getHost() {
                return configManager.getConfig().getDbHost();
            }

            @Override
            public String getDatabase() {
                return configManager.getConfig().getDbDb();
            }

            @Override
            public String getUser() {
                return configManager.getConfig().getDbUser();
            }

            @Override
            public String getPassword() {
                return configManager.getConfig().getDbPassword();
            }
        };
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public static InviteManager getInviteManager() {
        return inviteManager;
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static List<Clan> getClans(){
        return databaseManager.getClans();
    }

    public static Clan getClan(String clanName){
        return databaseManager.getClan(clanName);
    }

    public static Clan getClan(UUID member){
        return databaseManager.getClan(member);
    }

    public static Clan getClan(Player player){
        return databaseManager.getClan(player.getUniqueId());
    }

    public static Clan getClan(int clanId){
        return databaseManager.getClan(clanId);
    }

    public static void updateClanMembers(Clan clan, List<UUID> members){
        databaseManager.updateMembers(clan.getClanId(), members);
    }

    public static void updateClanElo(Clan clan, int elo){
        databaseManager.updateClanElo(clan.getClanId(), elo);
    }

    public static void addClanWin(Clan clan){
        databaseManager.addClanWin(clan.getClanId());
    }

    public static void addClanLoss(Clan clan){
        databaseManager.addClanLoss(clan.getClanId());
    }

    public static ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    /**
     * Will return you a scoreboard with teams and prefixes according to the players clans.
     * @return @{@link Scoreboard} a scoreboard by default or an empty scoreboard if the clan prefixes are disabled
     */
    public static Scoreboard getClansScoreboard(){
        return scoreboardManager.getScoreboard();
    }
}
