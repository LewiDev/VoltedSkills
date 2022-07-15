package me.lewi.volted.Listeners;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.lewi.volted.skills;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GUIListener {

    private skills plugin;

    private int SKILL_POINTS;
    private int REQ_EXP;
    private int STRENGTH;
    private int DEFENSE;
    private int VITALITY;


    private BukkitTask open;
    private Boolean openmenu = true;

    private ResultSet rs;


    public GUIListener(skills plugin) {this.plugin = plugin;}


    public void openSkillsGUI(Player p) {
        try {
            PreparedStatement statement = plugin.getDatabase().getConnection().prepareStatement("SELECT SKILL_POINTS, TOTAL_SKILL_POINTS, REQUIRED_EXP, STRENGTH, DEFENSE, VITALITY FROM player_skills WHERE UUID = ?");
            statement.setString(1, p.getUniqueId().toString());
            rs = statement.executeQuery();

            if (rs.next()) {
                SKILL_POINTS = rs.getInt("SKILL_POINTS");
                REQ_EXP = rs.getInt("REQUIRED_EXP");
                STRENGTH = rs.getInt("STRENGTH");
                DEFENSE = rs.getInt("DEFENSE");
                VITALITY = rs.getInt("VITALITY");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Gui gui = Gui.gui()
                .title(Component.text(ChatColor.translateAlternateColorCodes('&', "&aSkills")))
                .rows(3)
                .create();

        ItemStack item = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Strength Skill");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.GRAY + "Adds a permanent strength effect dependant on level");
        lore.add("");
        lore.add(ChatColor.GRAY + "Level: " + ChatColor.RED + STRENGTH + "/2");
        if(STRENGTH == 1) {
            lore.add(ChatColor.GRAY + "Next Level: " + ChatColor.RED + SKILL_POINTS + "/" + "20");
        } else if (STRENGTH == 0) {
            lore.add(ChatColor.GRAY + "Next Level: " + ChatColor.RED + SKILL_POINTS + "/" + "10");
        }
        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemMeta);

        ItemStack item2 = new ItemStack(Material.SHIELD, 1);
        ItemMeta itemMeta2 = item2.getItemMeta();
        itemMeta2.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Defense Skill");
        ArrayList<String> lore2 = new ArrayList<>();
        lore2.add("");
        lore2.add(ChatColor.GRAY + "Adds a permanent resistance effect dependant on level");
        lore2.add("");
        lore2.add(ChatColor.GRAY + "Level: " + ChatColor.RED + DEFENSE + "/3");
        if(DEFENSE == 2) {
            lore2.add(ChatColor.GRAY + "Next Level: " + ChatColor.RED + SKILL_POINTS + "/" + "45");
        } else if (DEFENSE == 1) {
            lore2.add(ChatColor.GRAY + "Next Level: " + ChatColor.RED + SKILL_POINTS + "/" + "30");
        } else if (DEFENSE == 0) {
            lore2.add(ChatColor.GRAY + "Next Level: " + ChatColor.RED + SKILL_POINTS + "/" + "15");
        }
        itemMeta2.setLore(lore2);
        itemMeta2.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item2.setItemMeta(itemMeta2);

        ItemStack item3 = new ItemStack(Material.IRON_CHESTPLATE, 1);
        ItemMeta itemMeta3 = item3.getItemMeta();
        itemMeta3.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Vitality Skill");
        ArrayList<String> lore3 = new ArrayList<>();
        lore3.add("");
        lore3.add(ChatColor.GRAY + "Adds an extra heart dependant on level");
        lore3.add("");
        lore3.add(ChatColor.GRAY + "Level: " + ChatColor.RED + VITALITY + "/5");
        if(VITALITY == 4) {
            lore3.add(ChatColor.GRAY + "Next Level: " + ChatColor.RED + SKILL_POINTS + "/" + "25");
        } else if (VITALITY == 3) {
            lore3.add(ChatColor.GRAY + "Next Level: " + ChatColor.RED + SKILL_POINTS + "/" + "20");
        } else if (VITALITY == 2) {
            lore3.add(ChatColor.GRAY + "Next Level: " + ChatColor.RED + SKILL_POINTS + "/" + "15");
        } else if (VITALITY == 1) {
            lore3.add(ChatColor.GRAY + "Next Level: " + ChatColor.RED + SKILL_POINTS + "/" + "10");
        } else if (VITALITY == 0) {
            lore3.add(ChatColor.GRAY + "Next Level: " + ChatColor.RED + SKILL_POINTS + "/" + "5");
        }
        itemMeta3.setLore(lore3);
        itemMeta3.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item3.setItemMeta(itemMeta3);

        ItemStack points = new ItemStack(Material.PAPER, 1);
        ItemMeta pointMeta = points.getItemMeta();
        pointMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Buy Skill points");
        ArrayList<String> lore5 = new ArrayList<>();
        lore5.add("");
        lore5.add(ChatColor.GRAY + "Purchase skill points with EXP to spend on skills!");
        lore5.add(ChatColor.GRAY + "Skill points progressively get more expensive");
        lore5.add("");
        lore5.add(ChatColor.GRAY + "Skill Points: " + ChatColor.RED + SKILL_POINTS);
        lore5.add(ChatColor.GRAY + "EXP: " + ChatColor.RED + getTotalExperience(p) + "/" + Math.round(REQ_EXP));
        lore5.add("");
        pointMeta.setLore(lore5);
        pointMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        points.setItemMeta(pointMeta);



        ItemStack close = new ItemStack(Material.BARRIER, 1);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName(ChatColor.DARK_RED + "Close Menu");
        closeMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        close.setItemMeta(closeMeta);

        GuiItem closeItem = new GuiItem(close);
        closeItem.setAction(action -> {
            action.setCancelled(true);
            gui.close(p);
        });

        GuiItem back = new GuiItem(Material.GRAY_STAINED_GLASS_PANE);
        back.setAction(action -> {
            action.setCancelled(true);
        });

        GuiItem gpoints = new GuiItem(points);
        gpoints.setAction(action -> {
            action.setCancelled(true);
            if(getTotalExperience(p) >= Math.round(REQ_EXP)) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "xp set " + p.getName() + " " + (getTotalExperience(p) - REQ_EXP));
                try {
                    PreparedStatement statement2 = plugin.getDatabase().getConnection().prepareStatement("UPDATE player_skills SET REQUIRED_EXP = "+ REQ_EXP*1.1 + ", SKILL_POINTS =  " + (SKILL_POINTS+1) + "  WHERE UUID = '" + p.getUniqueId().toString() + "';");
                    statement2.executeUpdate();
                    openmenu = true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                gui.close(p);
                openmenu = false;
                p.sendMessage(ChatColor.RED + "You do not have enough XP to purchase a skill point");
            }
            gui.close(p);
            if(openmenu) {
                open = new BukkitRunnable() {
                    @Override
                    public void run() {
                        openSkillsGUI(p);
                    }
                }.runTaskLater(plugin, 5);
            }
        });

        GuiItem strength = new GuiItem(item);
        strength.setAction(action -> {
            action.setCancelled(true);
            if(STRENGTH == 1) {
                if(SKILL_POINTS >= 20) {
                    try {
                        PreparedStatement statement2 = plugin.getDatabase().getConnection().prepareStatement("UPDATE player_skills SET STRENGTH = "+ 1 + ", SKILL_POINTS =  " + (SKILL_POINTS-20) + "  WHERE UUID = '" + p.getUniqueId().toString() + "';");
                        statement2.executeUpdate();
                        p.addPotionEffect(new PotionEffect(PotionType.STRENGTH.getEffectType(), Integer.MAX_VALUE, 1, true, false, true));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (SKILL_POINTS < 20) {
                    gui.close(p);
                    p.sendMessage(ChatColor.RED + "You do not have enough skill points to do this!");
                }
            } else if (STRENGTH == 0) {
                if(SKILL_POINTS >= 10) {
                    try {
                        PreparedStatement statement2 = plugin.getDatabase().getConnection().prepareStatement("UPDATE player_skills SET STRENGTH = "+ 1 + ", SKILL_POINTS =  " + (SKILL_POINTS-10) + "  WHERE UUID = '" + p.getUniqueId().toString() + "';");
                        statement2.executeUpdate();
                        p.addPotionEffect(new PotionEffect(PotionType.STRENGTH.getEffectType(), Integer.MAX_VALUE, 0, true, false, true));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (SKILL_POINTS < 10) {
                    gui.close(p);
                    p.sendMessage(ChatColor.RED + "You do not have enough skill points to do this!");
                }
            } else {
                gui.close(p);
                p.sendMessage(ChatColor.GREEN + "Your strength skill is maxed!");
            }

            gui.close(p);
        });
        GuiItem defense = new GuiItem(item2);
        defense.setAction(action -> {
            action.setCancelled(true);
            if(DEFENSE == 2) {
                if(SKILL_POINTS >= 45) {
                    try {
                        PreparedStatement statement2 = plugin.getDatabase().getConnection().prepareStatement("UPDATE player_skills SET DEFENSE = "+ 3 + ", SKILL_POINTS =  " + (SKILL_POINTS-45) + "  WHERE UUID = '" + p.getUniqueId().toString() + "';");
                        statement2.executeUpdate();
                        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2, true, false, true));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (SKILL_POINTS < 45) {
                    gui.close(p);
                    p.sendMessage(ChatColor.RED + "You do not have enough skill points to do this!");
                }
            } else if (DEFENSE == 1) {
                if(SKILL_POINTS >= 30) {
                    try {
                        PreparedStatement statement2 = plugin.getDatabase().getConnection().prepareStatement("UPDATE player_skills SET DEFENSE = "+ 2 + ", SKILL_POINTS =  " + (SKILL_POINTS-30) + "  WHERE UUID = '" + p.getUniqueId().toString() + "';");
                        statement2.executeUpdate();
                        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, true, false, true));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (SKILL_POINTS < 30) {
                    gui.close(p);

                    p.sendMessage(ChatColor.RED + "You do not have enough skill points to do this!");
                }
            }  else if (DEFENSE == 0) {
                if(SKILL_POINTS >= 15) {
                    try {
                        PreparedStatement statement2 = plugin.getDatabase().getConnection().prepareStatement("UPDATE player_skills SET DEFENSE = "+ 1 + ", SKILL_POINTS =  " + (SKILL_POINTS-15) + "  WHERE UUID = '" + p.getUniqueId().toString() + "';");
                        statement2.executeUpdate();
                        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, true, false, true));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (SKILL_POINTS < 15) {
                    gui.close(p);

                    p.sendMessage(ChatColor.RED + "You do not have enough skill points to do this!");
                }
            }  else {
                gui.close(p);

                p.sendMessage(ChatColor.GREEN + "Your defense skill is maxed!");
            }
            gui.close(p);
        });
        GuiItem vitality = new GuiItem(item3);
        vitality.setAction(action -> {
            action.setCancelled(true);
            if(VITALITY == 4) {
                if(SKILL_POINTS >= 25) {
                    try {
                        PreparedStatement statement2 = plugin.getDatabase().getConnection().prepareStatement("UPDATE player_skills SET VITALITY = "+ 5 + ", SKILL_POINTS =  " + (SKILL_POINTS-25) + "  WHERE UUID = '" + p.getUniqueId().toString() + "';");
                        statement2.executeUpdate();
                        p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(30);
                        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (SKILL_POINTS < 25) {
                    gui.close(p);

                    p.sendMessage(ChatColor.RED + "You do not have enough skill points to do this!");
                }
            } else if (VITALITY == 3) {
                if(SKILL_POINTS >= 20) {
                    try {
                        PreparedStatement statement2 = plugin.getDatabase().getConnection().prepareStatement("UPDATE player_skills SET VITALITY = "+ 4 + ", SKILL_POINTS =  " + (SKILL_POINTS-20) + "  WHERE UUID = '" + p.getUniqueId().toString() + "';");
                        statement2.executeUpdate();
                        p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(28);
                        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (SKILL_POINTS < 20) {
                    gui.close(p);

                    p.sendMessage(ChatColor.RED + "You do not have enough skill points to do this!");
                }
            }  else if (VITALITY == 2) {
                if(SKILL_POINTS >= 15) {
                    try {
                        PreparedStatement statement2 = plugin.getDatabase().getConnection().prepareStatement("UPDATE player_skills SET VITALITY = "+ 3 + ", SKILL_POINTS =  " + (SKILL_POINTS-15) + "  WHERE UUID = '" + p.getUniqueId().toString() + "';");
                        statement2.executeUpdate();
                        p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(26);
                        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (SKILL_POINTS < 15) {
                    gui.close(p);

                    p.sendMessage(ChatColor.RED + "You do not have enough skill points to do this!");
                }
            } else if (VITALITY == 1) {
                if(SKILL_POINTS >= 10) {
                    try {
                        PreparedStatement statement2 = plugin.getDatabase().getConnection().prepareStatement("UPDATE player_skills SET VITALITY = "+ 2 + ", SKILL_POINTS =  " + (SKILL_POINTS-10) + "  WHERE UUID = '" + p.getUniqueId().toString() + "';");
                        statement2.executeUpdate();
                        p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(24);
                        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (SKILL_POINTS < 10) {
                    gui.close(p);

                    p.sendMessage(ChatColor.RED + "You do not have enough skill points to do this!");
                }
            } else if (VITALITY == 0) {
                if(SKILL_POINTS >= 5) {
                    try {
                        PreparedStatement statement2 = plugin.getDatabase().getConnection().prepareStatement("UPDATE player_skills SET VITALITY = "+ 1 + ", SKILL_POINTS =  " + (SKILL_POINTS-5) + "  WHERE UUID = '" + p.getUniqueId().toString() + "';");
                        statement2.executeUpdate();
                        p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(22);
                        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (SKILL_POINTS < 5) {
                    gui.close(p);

                    p.sendMessage(ChatColor.RED + "You do not have enough skill points to do this!");
                }
            }  else {
                gui.close(p);

                p.sendMessage(ChatColor.GREEN + "Your vitality skill is maxed!");
            }
            gui.close(p);


        });

        gui.setItem(11, strength);
        gui.setItem(13, defense);
        gui.setItem(15, vitality);
        gui.setItem(26, closeItem);
        gui.setItem(18, gpoints);
        gui.setItem(0, back);
        gui.setItem(1, back);
        gui.setItem(2, back);
        gui.setItem(3, back);
        gui.setItem(4, back);
        gui.setItem(5, back);
        gui.setItem(6, back);
        gui.setItem(7, back);
        gui.setItem(8, back);
        gui.setItem(9, back);
        gui.setItem(10, back);
        gui.setItem(12, back);
        gui.setItem(14, back);
        gui.setItem(16, back);
        gui.setItem(17, back);
        gui.setItem(19, back);
        gui.setItem(20, back);
        gui.setItem(21, back);
        gui.setItem(22, back);
        gui.setItem(23, back);
        gui.setItem(24, back);
        gui.setItem(25, back);

        gui.open(p);


    }



    private int getTotalExperience(Player player) {
        int experience = 0;
        int level = player.getLevel();
        if (level >= 0 && level <= 15) {
            experience = (int) Math.ceil(Math.pow(level, 2) + (6 * level));
            int requiredExperience = 2 * level + 7;
            double currentExp = Double.parseDouble(Float.toString(player.getExp()));
            experience += Math.ceil(currentExp * requiredExperience);
            return experience;
        } else if (level > 15 && level <= 30) {
            experience = (int) Math.ceil((2.5 * Math.pow(level, 2) - (40.5 * level) + 360));
            int requiredExperience = 5 * level - 38;
            double currentExp = Double.parseDouble(Float.toString(player.getExp()));
            experience += Math.ceil(currentExp * requiredExperience);
            return experience;
        } else {
            experience = (int) Math.ceil(((4.5 * Math.pow(level, 2) - (162.5 * level) + 2220)));
            int requiredExperience = 9 * level - 158;
            double currentExp = Double.parseDouble(Float.toString(player.getExp()));
            experience += Math.ceil(currentExp * requiredExperience);
            return experience;
        }
    }

}
