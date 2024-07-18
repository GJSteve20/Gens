package ro.steve.gens.storage;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {

    private final Plugin plugin;
    private Connection connection;

    public Storage(Plugin plugin) {
        this.plugin = plugin;
        init();
    }

    private void connect() {
        try {
            plugin.getDataFolder().mkdir();
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:/" + plugin.getDataFolder().getAbsolutePath() + "/storage.db");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        plugin.getLogger().info("Init storage...");
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }

            var createTable = "CREATE TABLE IF NOT EXISTS Gens (location TEXT PRIMARY KEY, tier INTEGER)";
            var statement = connection.createStatement();
            statement.execute(createTable);
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addGen(Location loc, String tier, boolean upgrade) {
        var parsedLoc = loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ();
        String add;
        if (upgrade) {
            add = "UPDATE Gens SET tier = ? WHERE location = ?";
        } else {
            add = "INSERT INTO Gens (location, tier) VALUES (?,?)";
        }
        int t;
        try {
            t = Integer.parseInt(tier.replace("tier_", ""));
        } catch (NumberFormatException e1) {
            plugin.getLogger().severe("Please update at gens.yml at Gens." + tier + " into a valid number, eg.: tier_1");
            return false;
        }
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
            var statement = connection.prepareStatement(add);
            statement.setString(1, parsedLoc);
            statement.setInt(2, t);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            plugin.getLogger().severe(e.getMessage());
            return false;
        }
        return true;
    }

    public void removeGen(Location loc) {
        var parsedLoc = loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ();
        var delete = "DELETE FROM Gens WHERE location = ?";
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
            var statement = connection.prepareStatement(delete);
            statement.setString(1, parsedLoc);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isGen(Location loc) {
        var parsedLoc = loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ();
        var select = "SELECT * FROM Gens";
        var exists = false;
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
            var statement = connection.createStatement();
            var rs = statement.executeQuery(select);
            while (rs.next()) {
                if (exists) {
                    return true;
                }
                if (rs.getString("location").equalsIgnoreCase(parsedLoc)) {
                    exists = true;
                }
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            return false;
        }
        return exists;
    }

    public List<String> getLocation(int tier) {
        List<String> locations = new ArrayList<>();
        var select = "SELECT * FROM Gens";
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
            var statement = connection.createStatement();
            var rs = statement.executeQuery(select);
            while (rs.next()) {
                if (rs.getInt("tier") == tier) {
                    locations.add(rs.getString("location"));
                }
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return locations;
    }

    public Object getTier(Location loc) {
        var parsedLoc = loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ();
        var select = "SELECT * FROM Gens";
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
            var statement = connection.createStatement();
            var rs = statement.executeQuery(select);
            while (rs.next()) {
                if (rs.getString("location").equalsIgnoreCase(parsedLoc)) {
                    return rs.getInt("tier");
                }
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return null;
    }
}
