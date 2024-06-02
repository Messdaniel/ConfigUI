package me.messdaniel.configui.manager;

import com.sun.org.apache.xpath.internal.operations.Bool;
import me.messdaniel.configui.ConfigUI;
import me.messdaniel.configui.menu.ClickAction;
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
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionType;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ConfigUIManger {

    private static HashMap<String, Menu> allGuis;

    private static File folder;
    public ConfigUIManger() {
        folder = new File(ConfigUI.getInstance().getDataFolder(),"guis");
        load();
    }

    public boolean load() {
        allGuis = new HashMap<>();
        boolean reloadSuccessfully = true;
        if (folder == null || folder.listFiles() == null) return false;
        for (File file : folder.listFiles()) {
            ConfigUI.getInstance().getLogger().info("Loading: " + file.getName());
            YamlConfiguration fc = YamlConfiguration.loadConfiguration(file);
            String name = file.getName().replace(".yml", "");

            Menu menu = new Menu(name);
            menu.setTitle(fc.getString("gui.title"));
            menu.setSize(fc.getInt("gui.size"));
            menu.setDisabledMovingItems(fc.getBoolean("gui.disable-moving-items"));
            menu.setPermissionNeeded(fc.getString("gui.permission-needed"));
            if (fc.get("gui.open-event.sound") != null) {
                boolean setSound = true;
                Sound sound = null;
                try {
                    sound = Sound.valueOf(fc.getString("gui.open-event.sound.sound").toUpperCase());
                } catch (IllegalArgumentException e) {
                    ConfigUI.getInstance().getLogger().warning("Warning: Invalid open sound for gui '" + name + "'.");
                    setSound = false;
                    reloadSuccessfully = false;
                }
                if (setSound) {
                    menu.setOpen_sound(sound);
                    menu.setOpen_volume(fc.getDouble("gui.open-event.sound.volume"));
                    menu.setOpen_pitch(fc.getDouble("gui.open-event.sound.pitch"));
                }
            }
            menu.setOpen_message(fc.getStringList("gui.open-event.message"));
            menu.setOpen_commandPlayer(fc.getStringList("gui.open-event.command-player"));
            menu.setOpen_commandConsole(fc.getStringList("gui.open-event.command-console"));
            if (fc.get("gui.close-event.sound") != null) {
                boolean setSound = true;
                Sound sound = null;
                try {
                    sound = Sound.valueOf(fc.getString("gui.close-event.sound.sound").toUpperCase());
                } catch (IllegalArgumentException e) {
                    ConfigUI.getInstance().getLogger().warning("Warning: Invalid close sound for gui '" + name + "'.");
                    setSound = false;
                    reloadSuccessfully = false;
                }
                if (setSound) {
                    menu.setClose_sound(sound);
                    menu.setClose_volume(fc.getDouble("gui.close-event.sound.volume"));
                    menu.setClose_pitch(fc.getDouble("gui.close-event.sound.pitch"));
                }
            }
            menu.setClose_message(fc.getStringList("gui.close-event.message"));
            menu.setClose_commandPlayer(fc.getStringList("gui.close-event.command-player"));
            menu.setClose_commandConsole(fc.getStringList("gui.close-event.command-console"));
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
                        reloadSuccessfully = false;
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
                            reloadSuccessfully = false;
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
                            reloadSuccessfully = false;
                        }
                        if (effect != null) {
                            menuItem.setEffect(effect);
                            menuItem.setExtended(cs.getBoolean(key + ".extended"));
                            menuItem.setUpgraded(cs.getBoolean(key + ".upgraded"));
                        }
                    }
                    ConfigurationSection caCs = cs.getConfigurationSection(key + ".click-action");
                    if (caCs != null) {
                        for (String caKey : caCs.getKeys(false)) {
                            ClickAction clickAction = new ClickAction(caKey);
                            for (String clickTypeString : caCs.getStringList(caKey + ".click-type")) {
                                ClickType clickType;
                                try {
                                    clickType = ClickType.valueOf(clickTypeString.toUpperCase());
                                } catch (IllegalArgumentException exception) {
                                    ConfigUI.getInstance().getLogger().warning("Warning: Invalid click type for click action '" + caKey + "' for item '" + key + "'.");
                                    reloadSuccessfully = false;
                                    continue;
                                }
                                clickAction.addClickType(clickType);
                            }
                            clickAction.setAllowMoving(caCs.getBoolean(caKey + ".allow-moving"));
                            if (caCs.get(caKey + ".sound") != null) {
                                boolean setSound = true;
                                Sound sound = null;
                                try {
                                    sound = Sound.valueOf(caCs.getString(caKey + ".sound.sound").toUpperCase());
                                } catch (IllegalArgumentException e) {
                                    ConfigUI.getInstance().getLogger().warning("Warning: Invalid sound for click action '" + caKey + "' for item '" + key + "'.");
                                    setSound = false;
                                    reloadSuccessfully = false;
                                }
                                if (setSound) {
                                    clickAction.setSound(sound);
                                    clickAction.setVolume(caCs.getDouble(caKey + ".sound.volume"));
                                    clickAction.setPitch(caCs.getDouble(caKey + ".sound.pitch"));
                                }
                            }
                            clickAction.setMessage(caCs.getStringList(caKey + ".message"));
                            clickAction.setCommandPlayer(caCs.getStringList(caKey + ".command-player"));
                            clickAction.setCommandConsole(caCs.getStringList(caKey + ".command-console"));
                            menuItem.addClickAction(clickAction);
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
        return reloadSuccessfully;
    }

    public void openMenu(Player player,Menu menu) {
        if (menu.getPermissionNeeded() != null) {
            if (!player.hasPermission(menu.getPermissionNeeded())) {
                MessagesUtils.sendMessage(player,"dont-have-perms-inv","name",menu.getName());
                return;
            }
        }

        if (menu.getOpen_sound() != null)
            player.playSound(player.getLocation(),menu.getOpen_sound(), (float) menu.getOpen_volume(), (float) menu.getOpen_pitch());

        for (String message : menu.getOpen_message()) {
            if (message == null || message.equalsIgnoreCase("")) continue;
            player.sendMessage(ColorUtils.translate(message));
        }
        for (String command : menu.getOpen_commandPlayer()) {
            if (command == null || command.equalsIgnoreCase("")) continue;
            player.performCommand(command.replaceFirst("/",""));
        }
        for (String command : menu.getOpen_commandConsole()) {
            if (command == null || command.equalsIgnoreCase("")) continue;
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command.replace("{player}",player.getName()).replaceFirst("/",""));
        }

        player.openInventory(menu.createInventory(player));
    }
    public void closeMenu(Player player,Menu menu) {
        if (menu.getClose_sound() != null)
            player.playSound(player.getLocation(),menu.getClose_sound(), (float) menu.getClose_volume(), (float) menu.getClose_pitch());

        for (String message : menu.getClose_message()) {
            if (message == null || message.equalsIgnoreCase("")) continue;
            player.sendMessage(ColorUtils.translate(message));
        }
        for (String command : menu.getClose_commandPlayer()) {
            if (command == null || command.equalsIgnoreCase("")) continue;
            player.performCommand(command.replaceFirst("/",""));
        }
        for (String command : menu.getClose_commandConsole()) {
            if (command == null || command.equalsIgnoreCase("")) continue;
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command.replace("{player}",player.getName()).replaceFirst("/",""));
        }
    }

    public void clickAction(InventoryClickEvent event,Menu menu) {
        if (menu.getContents().size() == 0) return;
        Player player = (Player) event.getWhoClicked();
        for (MenuItem menuItem : menu.getContents()) {
            if (!menuItem.getSlots().contains(event.getSlot())) continue;
            if (!menuItem.getMaterial().equals(event.getCurrentItem().getType())) continue;
            for (ClickAction clickAction : menuItem.getClickActions()) {
                if (!clickAction.getClickType().isEmpty()) {
                    boolean sameClick = false;
                    for (ClickType clickType : clickAction.getClickType()) {
                        if (!event.getClick().equals(clickType)) continue;
                        sameClick = true;
                        break;
                    }
                    if (!sameClick) continue;
                }
                if (clickAction.isAllowMoving()) event.setCancelled(false);
                if (clickAction.getSound() != null) {
                    player.playSound(player.getLocation(),clickAction.getSound(), (float) clickAction.getVolume(), (float) clickAction.getPitch());
                }

                for (String message : clickAction.getMessage()) {
                    if (message == null || message.equalsIgnoreCase("")) continue;
                    player.sendMessage(ColorUtils.translate(message));
                }
                for (String command : clickAction.getCommandPlayer()) {
                    if (command == null || command.equalsIgnoreCase("")) continue;
                    player.performCommand(command.replaceFirst("/",""));
                }
                for (String command : clickAction.getCommandConsole()) {
                    if (command == null || command.equalsIgnoreCase("")) continue;
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command.replace("{player}",player.getName()).replaceFirst("/",""));
                }
            }
            return;
        }
    }

    public Menu getGui(String name) {
        return allGuis.get(name);
    }

    public HashMap<String, Menu> getAllGuis() {
        return allGuis;
    }
}
