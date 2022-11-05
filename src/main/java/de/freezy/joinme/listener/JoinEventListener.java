package de.freezy.joinme.listener;

import de.freezy.joinme.cmd.JoinMeCMD;
import de.freezy.joinme.main.JoinME;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class JoinEventListener implements Listener {

    @EventHandler
    public void onJoin(PostLoginEvent event) {


        try {
            String sqlcheckexit = "SELECT * FROM `joinme` WHERE `uuid` = '" + event.getPlayer().getUniqueId().toString() + "'";
            PreparedStatement pscheck = JoinME.connection.prepareStatement(sqlcheckexit);
            ResultSet rs = pscheck.executeQuery();
            if(!rs.next()) {
                String sql = "INSERT INTO `joinme` (`uuid`, `tokens`, `last_joinme`) VALUES (?, ?, ?);";
                PreparedStatement ps = JoinME.connection.prepareStatement(sql);
                ps.setString(1, event.getPlayer().getUniqueId().toString());
                ps.setInt(2, 0);
                ps.setLong(3, System.currentTimeMillis() - 600000);
                ps.executeUpdate();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

    }



    public static int getJoinMeTokens(String uuid) {
        String sqlcheckexit = "SELECT * FROM `joinme` WHERE `uuid` = '" + uuid + "'";

        try {
            PreparedStatement pscheck = JoinME.connection.prepareStatement(sqlcheckexit);
            ResultSet rs = pscheck.executeQuery();
            if (rs.next()) {
                return rs.getInt("tokens");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }



    public static long getJoinMeCooldown(String uuid) {
        String sqlcheckexit = "SELECT * FROM `joinme` WHERE `uuid` = '" + uuid + "'";

        try {
            PreparedStatement pscheck = JoinME.connection.prepareStatement(sqlcheckexit);
            ResultSet rs = pscheck.executeQuery();
            if (rs.next()) {
                return rs.getLong("last_joinme");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }


    }



    public static void setJoinMeCooldown(String uuid, String time) {

        String sql = "UPDATE `joinme` SET `last_joinme` = ? WHERE `uuid` = ?";
        try {
            PreparedStatement ps = JoinME.connection.prepareStatement(sql);
            ps.setString(1, time);
            ps.setString(2, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void addJoinMeTokens(String uuid, int tokens) {

        String sql = "UPDATE joinme SET tokens = tokens + ? WHERE uuid = ?";
        try {
            PreparedStatement ps = JoinME.connection.prepareStatement(sql);
            ps.setInt(1, tokens);
            ps.setString(2, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void removeJoinMeTokens(String uuid, int tokens) {

        String sql = "UPDATE joinme SET tokens = tokens - ? WHERE uuid = ?";
        try {
            PreparedStatement ps = JoinME.connection.prepareStatement(sql);
            ps.setInt(1, tokens);
            ps.setString(2, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void sendJoinMeMessage(ProxiedPlayer player) {





            TextComponent message1 = new TextComponent("§a§lJoinMe §8┃ §8§m-----------------------------------");
            ProxyServer.getInstance().broadcast(message1);

            TextComponent message2 = new TextComponent("§a§lJoinMe §8┃");
            ProxyServer.getInstance().broadcast(message2);


            TextComponent message3 = new TextComponent("§a§lJoinMe §8┃ §7"+getPrefix.getPrefix(player) +player.getName()+ " §7spielt auf §e" + JoinMeCMD.server.getInfo().getName() + "§7.");
            ProxyServer.getInstance().broadcast(message3);


            TextComponent message4 = new TextComponent("§a§lJoinMe §8┃ §e§lKLICKE HIER §7um auf den Server zu betreten!");
            message4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/joinme gotoserver"));
            ProxyServer.getInstance().broadcast(message4);
            ProxyServer.getInstance().broadcast(message2);
            ProxyServer.getInstance().broadcast(message1);


    }



}
