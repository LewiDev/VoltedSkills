package me.lewi.volted.Listeners;

import me.lewi.volted.skills;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SkillsListener implements Listener {

    private skills plugin;

    public SkillsListener(skills plugin) {
        this.plugin = plugin;
    }

    private UUID uuid;

    private int SKILL_POINTS = 0;
    private int REQ_EXP = 0;
    private int TOTAL_SKILL_POINTS = 0;
    private int STRENGTH = 0;
    private int DEFENSE = 0;
    private int VITALITY = 0;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        this.uuid = p.getUniqueId();
        try {
            PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement("SELECT SKILL_POINTS, TOTAL_SKILL_POINTS, REQUIRED_EXP, STRENGTH, DEFENSE, VITALITY FROM player_skills WHERE UUID = ?");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                SKILL_POINTS = rs.getInt("SKILL_POINTS");
                TOTAL_SKILL_POINTS = rs.getInt("TOTAL_SKILL_POINTS");
                REQ_EXP = rs.getInt("REQUIRED_EXP");
                STRENGTH = rs.getInt("STRENGTH");
                DEFENSE = rs.getInt("DEFENSE");
                VITALITY = rs.getInt("VITALITY");
            } else {
                PreparedStatement statement1 = plugin.getDatabase().getConnection().prepareStatement("INSERT INTO player_skills (UUID, SKILL_POINTS, TOTAL_SKILL_POINTS, REQUIRED_EXP, STRENGTH, DEFENSE, VITALITY) VALUES (" +
                        "'" + uuid.toString() + "'," +
                        SKILL_POINTS + "," +
                        TOTAL_SKILL_POINTS + "," +
                        plugin.getConfig().getInt("DEFAULTEXP") + "," +
                        STRENGTH + "," +
                        DEFENSE + "," +
                        VITALITY +
                        ");");
                statement1.executeUpdate();

            }




        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        if(STRENGTH == 1) {
            p.addPotionEffect(new PotionEffect(PotionType.STRENGTH.getEffectType(), Integer.MAX_VALUE, 0, true, false, true));
        } else if (STRENGTH == 2) {
            p.addPotionEffect(new PotionEffect(PotionType.STRENGTH.getEffectType(), Integer.MAX_VALUE, 1, true, false, true));
        }

        if(DEFENSE == 1) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, true, false, true));
        } else if (DEFENSE == 2) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, true, false, true));
        } else if (DEFENSE == 3) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2, true, false, true));
        }

        if(VITALITY == 1) {
            p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(22);
        } else if (VITALITY == 2) {
            p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(24);
        } else if (VITALITY == 3) {
            p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(26);
        } else if (VITALITY == 4) {
            p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(28);
        } else if (VITALITY == 5) {
            p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(30);
        }

        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
    }

}
