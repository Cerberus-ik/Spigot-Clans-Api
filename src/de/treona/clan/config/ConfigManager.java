package de.treona.clan.config;

import de.treona.clan.Clans;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class ConfigManager {

    private Config config;
    private File configFile;
    private File configDirectory;

    public ConfigManager() {
        this.configFile = new File("plugins/Clan/config.yml");
        this.configDirectory = new File("plugins/Clan/");
    }

    public void loadConfig(){
        if(!this.configFile.exists()){
            this.writeDefaultConfig();
        }
        try {
            YamlConfiguration yamlConfiguration = new YamlConfiguration();
            yamlConfiguration.loadFromString(FileUtils.readFileToString(this.configFile));
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
                public int getEloK() {
                    return yamlConfiguration.getInt("eloK");
                }
            };
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void writeDefaultConfig(){
        Bukkit.getLogger().info("Created the default config.");
        InputStream inputStream = Clans.getPlugin().getResource("config.yml");
        try {
            if(this.configDirectory.mkdirs()){
                Clans.getPlugin().getLogger().info("Created the plugin directory.");
            }
            if(this.configFile.createNewFile()){
                Clans.getPlugin().getLogger().info("Created the default config.");
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.configFile));
            IOUtils.copy(inputStream, bufferedWriter);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Config getConfig() {
        return config;
    }
}
