package de.freezy.joinme.main;

import jdk.nashorn.internal.parser.JSONParser;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

public class Lizenz {


    public static String pluginName = "JoinME";

    public static File lif = new File("plugins/" + pluginName, "lizenz.yml");
    public static Configuration licfg;

    static {
        try {
            licfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(lif);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String user = null;
    public static String key = null;


    public static void createConfigFile(){
        if (!lif.exists()) {
            licfg.set("Lizenz.User", "");
            licfg.set("Lizenz.Key", "");

            try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(licfg, lif);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }








}
