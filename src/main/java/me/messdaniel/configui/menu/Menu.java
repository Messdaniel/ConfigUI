package me.messdaniel.configui.menu;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Menu {

    private String name;
    private int size = 54;
    private String title = "Dummy";
    private boolean disabledMovingItems = false;
    private String permissionNeeded = null;
    private ArrayList<MenuItem> contents = new ArrayList<>();
    private Sound sound;
    private double volume;
    private double pitch;
    private String message;
    private String commandPlayer;
    private String commandConsole;

    public Menu (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        if (size != 9 && size != 18 && size != 27 && size != 36 && size != 45 && size != 54) size = 54;
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getTitle() {
        if (title == null) title = "Dummy";
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDisabledMovingItems() {
        return disabledMovingItems;
    }

    public void setDisabledMovingItems(boolean disabledMovingItems) {
        this.disabledMovingItems = disabledMovingItems;
    }

    public String getPermissionNeeded() {
        return permissionNeeded;
    }

    public void setPermissionNeeded(String permissionNeeded) {
        this.permissionNeeded = permissionNeeded;
    }

    public ArrayList<MenuItem> getContents() {
        return contents;
    }

    public void setContents(ArrayList<MenuItem> contents) {
        this.contents = contents;
    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCommandPlayer() {
        return commandPlayer;
    }

    public void setCommandPlayer(String commandPlayer) {
        this.commandPlayer = commandPlayer;
    }

    public String getCommandConsole() {
        return commandConsole;
    }

    public void setCommandConsole(String commandConsole) {
        this.commandConsole = commandConsole;
    }

    public Inventory createInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null,getSize(),getTitle());

        for (MenuItem menuItem : getContents()) {
            ItemStack item = menuItem.createItem(player);

            for (Integer slot : menuItem.getSlots()) {
                inv.setItem(slot,item);
            }
        }

        return inv;
    }
}
