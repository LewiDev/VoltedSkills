package me.lewi.volted;

import me.lewi.volted.Listeners.GUIListener;
import me.lewi.volted.Listeners.SkillsListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;

public final class skills extends JavaPlugin {

    String myTableName = "CREATE TABLE IF NOT EXISTS `player_skills` (`UUID` VARCHAR(36), `SKILL_POINTS` INT(11), `TOTAL_SKILL_POINTS` INT(11), `REQUIRED_EXP` INT(11), `STRENGTH` INT(11), `DEFENSE` INT(11), `VITALITY` INT(11))";

    private Database database;
    private GUIListener guiListener;


    @Override
    public void onEnable() {
        database = new Database(this);
        this.saveDefaultConfig();
        Bukkit.getLogger().log(Level.FINE, ChatColor.GREEN + "Volted Skills V1 by LewiDEV");


        try {
            database.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (database.isConnected()) {
            try {
                Statement statement = database.getConnection().createStatement();
                statement.executeUpdate(myTableName);
                System.out.println("CREATED TABLE");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // events
        this.getServer().getPluginManager().registerEvents(new SkillsListener(this), this);

        guiListener = new GUIListener(this);




        //commands
        this.getCommand("skills").setExecutor(new SkillCommand(this));



    }

    @Override
    public void onDisable() {
        if(!database.isConnected()) {
            database.disconnect();
        }
    }

    public Database getDatabase() {
        return database;
    }

    public GUIListener getGuiListener() {return guiListener;}
}
