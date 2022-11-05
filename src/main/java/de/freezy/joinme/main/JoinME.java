package de.freezy.joinme.main;

import de.freezy.joinme.cmd.JoinMeCMD;
import de.freezy.joinme.listener.JoinEventListener;
import de.freezy.joinme.main.Lizenz;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JoinME extends Plugin {


    //create a config file
    public static File configFile;


    public static File file = new File("plugins/JoinME", "config.yml");
    public static Configuration cfg;

    static {
        try {
            cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection connection;


    final String username = cfg.getString("database.user");
    final String password = cfg.getString("database.password");
    final String url = "jdbc:mysql://" + cfg.getString("database.host") + ":" + cfg.getString("database.port") + "/" + cfg.getString("database.dbname") + "?autoReconnect=true&useUnicode=yes";




    @SuppressWarnings("unused")
    private Connection createConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        System.out.println("Connected to Database.");
        return connection;
    }


    //Close Connection
    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void createDatabaseInConfig() throws IOException {
        if (!file.exists()) {

            cfg.set("database.host", "localhost");
            cfg.set("database.port", "3306");
            cfg.set("database.user", "dbuser");
            cfg.set("database.dbname", "dbname");
            cfg.set("database.password", "dbpassword");


            //create a List and add the config
            List<String> list = new ArrayList<>();
            list.add("");
            cfg.set("joinme.notallowedservers", list);



            ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, file);


        }
    }


    public static JoinME instance;

    public static Plugin getPlugin() {
        return instance;
    }


    @Override
    public void onEnable() {

        /*Lizenz.createConfigFile();


        Lizenz.key = Lizenz.licfg.getString("Lizenz.Key");
        Lizenz.user = Lizenz.licfg.getString("Lizenz.User");

        System.out.println("Lizenz Key: " + Lizenz.key);
        System.out.println("Lizenz User: " + Lizenz.user);


        try {
            System.out.println(CheckLizenz.check("status"));

        } catch (IOException e) {
            e.printStackTrace();
        }*/


        instance = this;

        getLogger().info("JoinME-Plugin wurde geladen!");

        getProxy().getPluginManager().registerCommand(this, new JoinMeCMD());
        //register listener
        getProxy().getPluginManager().registerListener(this, new JoinEventListener());


        try {
            createDatabaseInConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            createConnection();
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        try { // try catch to get any SQL errors (for example connections errors)
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Mit Datenbank verbunden.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Konnte nicht mit datenbank verbunden werden.");
        }


        String crt_joinme = "CREATE TABLE if not exists joinme (uuid VARCHAR(36) NOT NULL, tokens TEXT(1000000) NULL, last_joinme TEXT(1000000) NULL);";

        try {
            PreparedStatement stmt5 = connection.prepareStatement(crt_joinme);
            stmt5.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }





    }

    @Override
    public void onDisable() {
        // You should not put an enable message in your plugin.
        // BungeeCord already does so
        getLogger().info("JoinME-Plugin wurde deaktiviert!");


    }


}
