package de.meyssam.saft.utils;

import de.meyssam.saft.localsystem.FileSystem;
import net.dv8tion.jda.api.entities.Guild;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Meyssam Saghiri on Mai 01, 2020
 */
public class Tables implements FileSystem {

    public void setServer(Guild guild) {
        if(!isRegistered(guild)) {
            MySQL.update("INSERT INTO serverlist (ID, CMD, VOICE) VALUES (" + guild.getId() + ", TRUE, TRUE)");
        }
    }

    public void removeServer(Guild guild, String id) {
        if(guild == null) {
            MySQL.update("DELETE FROM serverlist WHERE ID = '" + id + "'");
            return;
        }
        if(isRegistered(guild)) {
            MySQL.update("DELETE FROM serverlist WHERE ID = '" + id + "'");
        }
    }

    public boolean isCommand(Guild guild) {
        String s = "TRUE";
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("SELECT * FROM serverlist WHERE ID=?");
            ps.setString(1, guild.getId());
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                s = rs.getString("CMD");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return !s.equalsIgnoreCase("0");
    }

    public boolean isVoice(Guild guild) {
        String s = "TRUE";
        try {
            PreparedStatement ps = MySQL.con.prepareStatement("SELECT * FROM serverlist WHERE ID=?");
            ps.setString(1, guild.getId());
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                s = rs.getString("VOICE");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return !s.equalsIgnoreCase("0");
    }

    public void setVoice(Guild guild) {
        MySQL.update("UPDATE serverlist SET VOICE = " + (!isVoice(guild)) + " WHERE ID=" + guild.getId());
    }

    public void setCommand(Guild guild) {
        MySQL.update("UPDATE serverlist SET CMD = " + (!isCommand(guild)) + " WHERE ID=" + guild.getId());
    }

    public boolean isRegistered(Guild guild) {
            try {
                PreparedStatement ps = MySQL.con.prepareStatement("SELECT ID FROM serverlist WHERE ID=?");
                ps.setString(1, guild.getId());
                ResultSet rs = ps.executeQuery();
                return rs.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return false;
    }

    public static int countEntries() {
        try {
            int i = 0;
            PreparedStatement ps = MySQL.con.prepareStatement("SELECT * FROM serverlist");
            ResultSet rs = MySQL.getResult("SELECT * FROM serverlist");
            while (rs.next()) {
                i++;
            }
            return i;
        } catch (SQLException e) {
            System.out.println("Fehler beim Zählen");
        }
        return 0;
    }

    public List<String> allServers() {
        List<String> temp = new ArrayList<>();
        try {
            ResultSet rs = MySQL.getResult("SELECT * FROM serverlist");
            while(rs.next()) {
                temp.add(rs.getString("ID"));
            }
            return temp;
        } catch (SQLException e) {
            System.out.println("Fehler!");
            e.printStackTrace();
        }
        return null;
    }

}
