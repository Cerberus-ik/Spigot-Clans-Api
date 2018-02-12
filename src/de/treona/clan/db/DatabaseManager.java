package de.treona.clan.db;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import de.treona.clan.common.Clan;
import org.json.JSONArray;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("Duplicates")
public class DatabaseManager {

    private MysqlDataSource dataSource;

    public DatabaseManager(DatabaseCredentials databaseCredentials) {
        this.dataSource = new MysqlDataSource();
        this.dataSource.setUser(databaseCredentials.getUser());
        this.dataSource.setPassword(databaseCredentials.getPassword());
        this.dataSource.setServerName(databaseCredentials.getHost());
        this.dataSource.setPort(databaseCredentials.getPort());
        this.dataSource.setURL("jdbc:mysql://" + databaseCredentials.getHost() + "/" + databaseCredentials.getDatabase() + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
    }

    public boolean doesTableExist() {
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1 FROM clans LIMIT 1;");
            preparedStatement.executeQuery();
            connection.close();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean canConnect() {
        try {
            Connection connection = this.dataSource.getConnection();
            connection.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void createTable() {
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE clans ( clanId INT PRIMARY KEY NOT NULL AUTO_INCREMENT, clanName TEXT, clanTag TEXT, elo INT, losses INT DEFAULT 0, owner TEXT, members TEXT, wins INT DEFAULT 0 );");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = connection.prepareStatement("CREATE UNIQUE INDEX clans_clanId_uindex ON clans (clanId);");
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isClanNameTaken(String clanName) {
        boolean result = false;
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT clanId FROM `clans` WHERE clans.clanName = ?;");
            preparedStatement.setString(1, clanName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = true;
            }
            connection.close();
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean isInAClan(UUID uuid) {
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT members FROM `clans`;");
            ResultSet resultSet = preparedStatement.executeQuery();
            List<UUID> results = new ArrayList<>();
            while (resultSet.next()) {
                JSONArray jsonArray = new JSONArray(resultSet.getString(1));
                for (int i = 0; i < jsonArray.length(); i++) {
                    results.add(UUID.fromString(jsonArray.getString(i)));
                }
            }
            connection.close();
            resultSet.close();
            preparedStatement.close();
            return results.contains(uuid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public Clan getClan(String clanName) {
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT clanId, clanName, clanTag, wins, losses, members, owner, elo FROM `clans` WHERE  clanName = ?;");
            preparedStatement.setString(1, clanName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()){
                return null;
            }
            int clanId = resultSet.getInt(1);
            String clanTag = resultSet.getString(2);
            int wins = resultSet.getInt(4);
            int losses = resultSet.getInt(5);
            List<UUID> members = new ArrayList<>();
            JSONArray memberArray = new JSONArray(resultSet.getString(6));
            for (int i = 0; i < memberArray.length(); i++) {
                members.add(UUID.fromString(memberArray.getString(i)));
            }
            UUID owner = UUID.fromString(resultSet.getString(7));
            int elo = resultSet.getInt(8);
            connection.close();
            resultSet.close();
            preparedStatement.close();
            return new Clan() {
                @Override
                public int getClanId() {
                    return clanId;
                }

                @Override
                public String getClanName() {
                    return clanName;
                }

                @Override
                public String getClanTag() {
                    return clanTag;
                }

                @Override
                public int getWins() {
                    return wins;
                }

                @Override
                public int getLosses() {
                    return losses;
                }

                @Override
                public List<UUID> getMembers() {
                    return members;
                }

                @Override
                public UUID getOwner() {
                    return owner;
                }

                @Override
                public int getElo() {
                    return elo;
                }
            };
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Clan getClan(int clanId) {
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT clanName, clanTag, wins, losses, members, owner, elo FROM `clans` WHERE clanId = ?;");
            preparedStatement.setInt(1, clanId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()){
                return null;
            }
            String clanName = resultSet.getString(1);
            String clanTag = resultSet.getString(2);
            int wins = resultSet.getInt(3);
            int losses = resultSet.getInt(4);
            List<UUID> members = new ArrayList<>();
            JSONArray memberArray = new JSONArray(resultSet.getString(5));
            for (int i = 0; i < memberArray.length(); i++) {
                members.add(UUID.fromString(memberArray.getString(i)));
            }
            UUID owner = UUID.fromString(resultSet.getString(6));
            int elo = resultSet.getInt(7);
            connection.close();
            resultSet.close();
            preparedStatement.close();
            return new Clan() {
                @Override
                public int getClanId() {
                    return clanId;
                }

                @Override
                public String getClanName() {
                    return clanName;
                }

                @Override
                public String getClanTag() {
                    return clanTag;
                }

                @Override
                public int getWins() {
                    return wins;
                }

                @Override
                public int getLosses() {
                    return losses;
                }

                @Override
                public List<UUID> getMembers() {
                    return members;
                }

                @Override
                public UUID getOwner() {
                    return owner;
                }

                @Override
                public int getElo() {
                    return elo;
                }
            };
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Clan getClan(UUID uuid) {
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT clanId, members FROM `clans`;");
            ResultSet resultSet = preparedStatement.executeQuery();
            Clan clan = null;
            loop:
            while (resultSet.next()){
                JSONArray memberArray = new JSONArray(resultSet.getString(2));
                for (int i = 0; i < memberArray.length(); i++) {
                    if(UUID.fromString(memberArray.getString(i)).equals(uuid)){
                        clan = this.getClan(resultSet.getInt(1));
                        break loop;
                    }
                }
            }
            connection.close();
            resultSet.close();
            preparedStatement.close();
            return clan;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addClanWin(int clanId) {
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE clans SET wins=wins+1 WHERE clanId = ?;");
            preparedStatement.setInt(1, clanId);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addClanLoss(int clanId) {
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE clans SET losses=clans.losses+1 WHERE clanId = ?;");
            preparedStatement.setInt(1, clanId);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateClanElo(int clanId, int elo) {
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE clans SET elo= ? WHERE clanId = ?;");
            preparedStatement.setInt(1, elo);
            preparedStatement.setInt(2, clanId);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createClan(String clanName, String clanTag, int elo, UUID owner) {
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO clans(clanName, clanTag, elo, owner, members) VALUES (?, ?, ?, ?, ?);");
            preparedStatement.setString(1, clanName);
            preparedStatement.setString(2, clanTag);
            preparedStatement.setInt(3, elo);
            preparedStatement.setString(4, owner.toString());
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(owner.toString());
            preparedStatement.setString(5, jsonArray.toString());
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMembers(int clanId, List<UUID> members){
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE clans SET members= ? WHERE clanId = ?;");
            JSONArray jsonArray = new JSONArray();
            members.forEach(member -> jsonArray.put(member.toString()));
            preparedStatement.setString(1, jsonArray.toString());
            preparedStatement.setInt(2, clanId);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteClan(int clanId){
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM clans WHERE clanId = ?;");
            preparedStatement.setInt(1, clanId);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
