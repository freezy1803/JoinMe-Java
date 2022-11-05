package de.freezy.joinme.cmd;

import de.freezy.joinme.listener.JoinEventListener;
import de.freezy.joinme.main.JoinME;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerConnectRequest;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class JoinMeCMD extends Command {

    public static long nowIn2mins;
    public static Server server = null;

    public JoinMeCMD() {
        super("joinme", "", "createjoinme");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            if (player.hasPermission("joinme.use")) {
                if (strings.length == 0) {

                    if (JoinEventListener.getJoinMeTokens(player.getUniqueId().toString()) > 0 || player.hasPermission("joinme.use.unlimited")) {

                        if (System.currentTimeMillis() >= nowIn2mins || nowIn2mins == 0) {
                            if ((System.currentTimeMillis() >= JoinEventListener.getJoinMeCooldown(player.getUniqueId().toString()))) {

                                List<String> badserver = JoinME.cfg.getStringList("joinme.notallowedservers");

                                if (badserver.contains(player.getServer().getInfo().getName())) {
                                    player.sendMessage("§a§lJoinMe §8┃ §cDu kannst auf diesem Server kein JoinMe senden!");
                                } else {

                                    nowIn2mins = System.currentTimeMillis() + 120000;


                                    server = player.getServer();


                                    ProxyServer.getInstance().getScheduler().schedule(JoinME.instance, new Runnable() {

                                                @Override
                                                public void run() {
                                                    server = null;
                                                }
                                            }
                                            , 60L, TimeUnit.SECONDS);


                                    JoinEventListener.setJoinMeCooldown(player.getUniqueId().toString(), String.valueOf(System.currentTimeMillis() + 600000));


                                    if(!player.hasPermission("joinme.use.unlimited")) {
                                        JoinEventListener.removeJoinMeTokens(player.getUniqueId().toString(), 1);
                                    }

                                    JoinEventListener.sendJoinMeMessage(player);
                                }
                            } else {
                                //millis to date
                                long millis = JoinEventListener.getJoinMeCooldown(player.getUniqueId().toString()) - System.currentTimeMillis();
                                long seconds = (millis / 1000) % 60;
                                long minutes = (millis / (1000 * 60)) % 60;
                                player.sendMessage("§a§lJoinMe §8┃ §cDu kannst erst wieder in §e" + minutes + "m §e" + seconds + "s §cein neues JoinMe erstellen!");
                            }
                        } else {
                            //millis to date
                            long millis = nowIn2mins - System.currentTimeMillis();
                            long seconds = (millis / 1000) % 60;
                            long minutes = (millis / (1000 * 60)) % 60;
                            player.sendMessage("§a§lJoinMe §8┃ §cEs kann erst wieder in §e" + minutes + "m §e" + seconds + "s §cein neues JoinMe erstellt werden!");
                        }
                    } else {
                        player.sendMessage("§a§lJoinMe §8┃ §cDu hast keine JoinMe Tokens!");
                    }
                } else if (strings.length == 1) {
                    if (strings[0].equalsIgnoreCase("admin")) {
                        if (player.hasPermission("joinme.admin")) {


                            server = player.getServer();
                            ProxyServer.getInstance().getScheduler().schedule(JoinME.instance, new Runnable() {

                                        @Override
                                        public void run() {
                                            server = null;
                                        }
                                    }
                                    , 60L, TimeUnit.SECONDS);

                            JoinEventListener.sendJoinMeMessage(player);
                        } else {
                            player.sendMessage("§a§lJoinMe §8┃ §cDu hast keine Rechte dazu!");
                        }
                    } else if (strings[0].equalsIgnoreCase("gotoserver")) {
                        if (server != null) {
                            if (player.getServer().getInfo().getName().equals(server.getInfo().getName())) {
                                player.sendMessage("§a§lJoinMe §8┃ §cDu bist bereits auf diesem Server!");
                            } else {
                                player.connect(server.getInfo());
                                player.sendMessage("§a§lJoinMe §8┃ §aDu wurdest zum Server §e" + server.getInfo().getName() + " §agesendet!");
                            }
                        } else {
                            player.sendMessage("§a§lJoinMe §8┃ §cEr ist derzeit kein JoinME aktiv!");
                        }
                    } else {
                        player.sendMessage("§a§lJoinMe §8┃ §7Du hast derzeit §e" + JoinEventListener.getJoinMeTokens(player.getUniqueId().toString()) + " §7JoinMe Tokens!");
                    }
                } else if (strings.length == 2) {
                    if (strings[0].equalsIgnoreCase("admin")) {
                        if (player.hasPermission("joinme.admin")) {
                            player.sendMessage("§a§lJoinMe §8┃ §7/joinme admin <Spieler> add/remove <Anzahl>");
                        } else {
                            player.sendMessage("§a§lJoinMe §8┃ §cDu hast keine Rechte dazu!");
                        }
                    } else if(strings[0].equalsIgnoreCase("get")) {
                        if (player.hasPermission("joinme.admin")) {
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(strings[1]);
                            if (target != null) {
                                player.sendMessage("§a§lJoinMe §8┃ §7Der Spieler hat §e" + JoinEventListener.getJoinMeTokens(target.getUniqueId().toString()) + " §7JoinMe Tokens!");
                            }
                        }
                    }
                } else if (strings.length == 3) {
                    if (strings[0].equalsIgnoreCase("admin")) {
                        if(strings[1].equalsIgnoreCase("addserver")) {
                            if (player.hasPermission("joinme.admin")) {
                                player.sendMessage("§a§lJoinMe §8┃ §7Du hast den Server §e" + strings[2] + " §7erfolgreich hinzugefügt!");
                                List<String> list = JoinME.cfg.getStringList("joinme.notallowedservers");
                                list.add(strings[2]);
                                JoinME.cfg.set("joinme.notallowedservers", list);
                                try {
                                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(JoinME.cfg, JoinME.file);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                player.sendMessage("§a§lJoinMe §8┃ §cDu hast keine Rechte dazu!");
                            }
                        } else {
                            player.sendMessage("§a§lJoinMe §8┃ §7/joinme admin <Spieler> add/remove <Anzahl>");

                        }
                    }

                } else if (strings.length == 4) {
                    if (player.hasPermission("joinme.admin")) {
                        if (strings[2].equalsIgnoreCase("add")) {
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(strings[1]);
                            if (target != null) {
                                player.sendMessage("§a§lJoinMe §8┃ §7Du hast §e" + Integer.parseInt(strings[3]) + " §7JoinMe Tokens §e" + target.getName() + " §7gegeben!");

                                JoinEventListener.addJoinMeTokens(target.getUniqueId().toString(), Integer.parseInt(strings[3]));
                            } else {
                                player.sendMessage("§a§lJoinMe §8┃ §cDer Spieler ist nicht online!");
                            }
                        } else if (strings[2].equalsIgnoreCase("remove")) {
                            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(strings[1]);
                            if (target != null) {
                                player.sendMessage("§a§lJoinMe §8┃ §7Du hast §e" + Integer.parseInt(strings[3]) + " §7JoinMe Tokens von §e" + target.getName() + " §7entfernt!");
                                JoinEventListener.removeJoinMeTokens(target.getUniqueId().toString(), Integer.parseInt(strings[3]));
                            } else {
                                player.sendMessage("§a§lJoinMe §8┃ §cDer Spieler ist nicht online!");
                            }
                        } else {
                            player.sendMessage("§a§lJoinMe §8┃ §7/joinme admin <Spieler> add/remove <Anzahl>");
                        }
                    } else {
                        player.sendMessage("§a§lJoinMe §8┃ §cDu hast keine Rechte dazu!");
                    }
                }
            } else {
                player.sendMessage("§a§lJoinMe §8┃ §cDu hast keine Rechte dazu!");
            }
        }
    }
}
