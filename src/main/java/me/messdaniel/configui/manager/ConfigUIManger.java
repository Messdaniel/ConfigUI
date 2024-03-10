package me.messdaniel.configui.manager;

import com.sun.org.apache.xpath.internal.operations.Bool;
import me.messdaniel.configui.ConfigUI;
import me.messdaniel.configui.menu.Menu;
import me.messdaniel.configui.menu.MenuItem;
import me.messdaniel.configui.utils.ColorUtils;
import me.messdaniel.configui.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.awt.*;
import java.io.File;
import java.lang.foreign.PaddingLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ConfigUIManger {

    private static HashMap<String, Menu> allGuis;

    private static File folder;
    public ConfigUIManger() {
        folder = new File(ConfigUI.getInstance().getDataFolder(),"guis");
        load();
    }

    public static void load() {
        allGuis = new HashMap<>();
        for (File file : folder.listFiles()) {
            ConfigUI.getInstance().getLogger().info("Loading: " + file.getName());
            YamlConfiguration fc = YamlConfiguration.loadConfiguration(file);
            String name = file.getName().replace(".yml", "");

            Menu menu = new Menu(name);
            menu.setTitle(fc.getString("gui.title"));
            menu.setSize(fc.getInt("gui.size"));
            menu.setDisabledMovingItems(fc.getBoolean("gui.disable-moving-items"));
            menu.setPermissionNeeded(fc.getString("gui.permission-needed"));
            if (fc.get("gui.sound") != null) {
                boolean setSound = true;
                Sound sound = null;
                try {
                    sound = Sound.valueOf(fc.getString("gui.sound.sound").toUpperCase());
                } catch (IllegalArgumentException e) {
                    ConfigUI.getInstance().getLogger().warning("Warning: Invalid sound for gui '" + name + "'.");
                    setSound = false;
                }
                if (setSound) {
                    menu.setSound(sound);
                    menu.setVolume(fc.getDouble("gui.sound.volume"));
                    menu.setPitch(fc.getDouble("gui.sound.pitch"));
                }
            }
            menu.setMessage(fc.getString("gui.message"));
            menu.setCommandPlayer(fc.getString("gui.command-player"));
            menu.setCommandConsole(fc.getString("gui.command-console"));
            menu.setMessage(fc.getString("gui.message"));
            ConfigurationSection cs = fc.getConfigurationSection("items");
            if (cs != null) {
                ArrayList<MenuItem> contents = new ArrayList<>();
                for (String key : cs.getKeys(false)) {
                    MenuItem menuItem = new MenuItem();
                    boolean setMaterial = true;
                    Material material = Material.DIRT;
                    try {
                        material = Material.valueOf(cs.getString(key + ".material").toUpperCase());
                    } catch (IllegalArgumentException e) {
                        ConfigUI.getInstance().getLogger().warning("Warning: Invalid material for item '" + key + "'.");
                        setMaterial = false;
                    }
                    if (setMaterial) menuItem.setMaterial(material);
                    menuItem.setName(cs.getString(key + ".display-name"));
                    menuItem.setAmount(cs.getInt(key + ".amount"));
                    menuItem.setLore(cs.getStringList(key + ".lore"));
                    if (cs.get(key + ".slot") != null) menuItem.getSlots().add(cs.getInt(key + ".slot"));
                    for (String slot : cs.getStringList(key + ".slots")) {
                        menuItem.getSlots().add(Integer.parseInt(slot));
                    }
                    for (String itemFlagString : cs.getStringList(key + ".itemflags")) {
                        ItemFlag itemFlag;
                        try {
                            itemFlag = ItemFlag.valueOf(itemFlagString.toUpperCase());
                        } catch (IllegalArgumentException exception) {
                            ConfigUI.getInstance().getLogger().warning("Warning: Invalid item flag for item '" + key + "'.");
                            continue;
                        }
                        menuItem.addItemFlag(itemFlag);
                    }

                    menuItem.setSkullTexture(cs.getString(key + ".skull-texture"));
                    for (String enchantmentData : cs.getStringList(key + ".enchantments")) {
                        Enchantment enchantment = Enchantment.getByName(enchantmentData.split(",")[0].toUpperCase());
                        Integer level = Integer.parseInt(enchantmentData.split(",")[1]);
                        menuItem.addEnchantments(enchantment,level);
                    }
                    if (cs.get(key + ".effect") != null) {
                        PotionType effect = null;
                        try {
                            effect = PotionType.valueOf((cs.getString(key + ".effect").toUpperCase()));
                        } catch (IllegalArgumentException e) {
                            ConfigUI.getInstance().getLogger().warning("Warning: Invalid potion type for item '" + key + "'.");
                        }
                        if (effect != null) {
                            menuItem.setEffect(effect);
                            menuItem.setExtended(cs.getBoolean(key + ".extended"));
                            menuItem.setUpgraded(cs.getBoolean(key + ".upgraded"));
                        }
                    }

                    contents.add(menuItem);
                }
                menu.setContents(contents);
            }
            ConfigUI.getInstance().getLogger().info("Finished loading: " + file.getName());
            allGuis.put(name,menu);
        }

        ConfigUI.getInstance().getLogger().info("Successfully loaded " + allGuis.size() + " gui(s)");
    }

    public void openMenu(Player player,Menu menu) {
        if (menu.getPermissionNeeded() != null) {
            if (!player.hasPermission(menu.getPermissionNeeded())) {
                MessagesUtils.sendMessage(player,"dont-have-perms-inv","name",menu.getName());
                return;
            }
        }

        if (menu.getSound() != null)
            player.playSound(player.getLocation(),menu.getSound(), (float) menu.getVolume(), (float) menu.getPitch());
        if (menu.getMessage() != null)
            player.sendMessage(ColorUtils.translate(menu.getMessage()));
        if (menu.getCommandPlayer() != null)
            player.performCommand(menu.getCommandPlayer().replaceFirst("/",""));
        if (menu.getCommandConsole() != null)
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),menu.getCommandConsole().replace("{player}",player.getName()).replaceFirst("/",""));

        player.openInventory(menu.createInventory(player));
    }

    public Menu getGui(String name) {
        return allGuis.get(name);
    }

    public HashMap<String, Menu> getAllGuis() {
        return allGuis;
    }
}
