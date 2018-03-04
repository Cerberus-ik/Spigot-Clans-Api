package de.treona.clans.config;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class ConfigManager {

    private Config config;
    private final File configFile;
    private final File configDirectory;
    private final JavaPlugin javaPlugin;

    public ConfigManager(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
        this.configFile = new File(this.javaPlugin.getDataFolder(), "config.yml");
        this.configDirectory = new File(this.javaPlugin.getDataFolder().getPath());
    }

    public void loadConfig() {
        if (!this.configFile.exists()) {
            this.writeDefaultConfig();
        }
        try {
            YamlConfiguration yamlConfiguration = new YamlConfiguration();
            yamlConfiguration.loadFromString(FileUtils.readFileToString(this.configFile));
            if (!yamlConfiguration.contains("configVersion") || yamlConfiguration.getInt("configVersion") < 2) {
                this.backupConfig();
                this.writeDefaultConfig();
            }
            this.config = new Config() {
                @Override
                public String getDbHost() {
                    return yamlConfiguration.getString("dbHost");
                }

                @Override
                public String getDbUser() {
                    return yamlConfiguration.getString("dbUser");
                }

                @Override
                public String getDbPassword() {
                    return yamlConfiguration.getString("dbPassword");
                }

                @Override
                public String getDbDb() {
                    return yamlConfiguration.getString("dbDb");
                }

                @Override
                public int getDbPort() {
                    return yamlConfiguration.getInt("dbPort");
                }

                @Override
                public int getBaseElo() {
                    return yamlConfiguration.getInt("eloBase");
                }

                @Override
                public boolean setClanTagTabPrefix() {
                    return yamlConfiguration.getBoolean("setClanTagTabPrefix");
                }

                @Override
                public int getEloK() {
                    return yamlConfiguration.getInt("eloK");
                }

                @Override
                public boolean flushScoreboardOnJoin() {
                    return yamlConfiguration.getBoolean("flushScoreboardOnJoin");
                }
            };
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void backupConfig() {
        try {
            FileUtils.copyFile(this.configFile, new File(this.javaPlugin.getDataFolder(), "backup_config.yml"));
            this.javaPlugin.getLogger().warning("Backing up old config...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeDefaultConfig() {
        this.javaPlugin.getLogger().info("Created the default config.");
        InputStream inputStream = this.javaPlugin.getResource("config.yml");
        if (this.configDirectory.mkdirs()) {
            this.javaPlugin.getLogger().info("Created the plugin directory.");
        }
        try {
            if (this.configFile.createNewFile()) {
                this.javaPlugin.getLogger().info("Created the default config.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;

        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.configFile))) {
            IOUtils.copy(inputStream, bufferedWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Config getConfig() {
        return this.config;
    }
}
