package de.treona.clan;

import de.treona.clan.commands.ClanCommand;
import de.treona.clan.common.Clan;
import de.treona.clan.config.Config;
import de.treona.clan.config.ConfigManager;
import de.treona.clan.db.DatabaseCredentials;
import de.treona.clan.db.DatabaseManager;
import de.treona.clan.managers.InviteManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

public class Clans extends JavaPlugin{

    public static final String PREFIX_COLOR = ChatColor.GRAY + "[" + ChatColor.DARK_AQUA + "Clan" + ChatColor.GRAY + "]" + ChatColor.RESET;
    public static final String PREFIX = "[Clan]";
    private static InviteManager inviteManager;
    private static JavaPlugin plugin;
    private static ConfigManager configManager;
    private static DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        super.getCommand("Clan").setExecutor(new ClanCommand());
        plugin = this;

        configManager = new ConfigManager();
        configManager.loadConfig();
        databaseManager = new DatabaseManager(this.getDatabaseCredentials());
        inviteManager = new InviteManager();

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


}
