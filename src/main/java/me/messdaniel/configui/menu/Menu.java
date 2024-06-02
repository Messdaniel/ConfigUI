package me.messdaniel.configui.menu;

import me.messdaniel.configui.ConfigUI;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Menu implements Listener {

    private String name;
    private int size = 54;
    private String title = "Dummy";
    private boolean disabledMovingItems = false;
    private String permissionNeeded = null;
    private ArrayList<MenuItem> contents = new ArrayList<>();
    private Sound open_sound;
    private double open_volume;
    private double open_pitch;
    private List<String> open_message;
    private List<String> open_commandPlayer;
    private List<String> open_commandConsole;
    private Sound close_sound;
    private double close_volume;
    private double close_pitch;
    private List<String> close_message;
    private List<String> close_commandPlayer;
    private List<String> close_commandConsole;

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

    public Sound getOpen_sound() {
        return open_sound;
    }

    public void setOpen_sound(Sound open_sound) {
        this.open_sound = open_sound;
    }

    public double getOpen_volume() {
        return open_volume;
    }

    public void setOpen_volume(double open_volume) {
        this.open_volume = open_volume;
    }

    public double getOpen_pitch() {
        return open_pitch;
    }

    public void setOpen_pitch(double open_pitch) {
        this.open_pitch = open_pitch;
    }

    public List<String> getOpen_message() {
        return open_message;
    }

    public void setOpen_message(List<String> open_message) {
        this.open_message = open_message;
    }

    public List<String> getOpen_commandPlayer() {
        return open_commandPlayer;
    }

    public void setOpen_commandPlayer(List<String> open_commandPlayer) {
        this.open_commandPlayer = open_commandPlayer;
    }

    public List<String> getOpen_commandConsole() {
        return open_commandConsole;
    }

    public void setOpen_commandConsole(List<String> open_commandConsole) {
        this.open_commandConsole = open_commandConsole;
    }

    public Sound getClose_sound() {
        return close_sound;
    }

    public void setClose_sound(Sound close_sound) {
        this.close_sound = close_sound;
    }

    public double getClose_volume() {
        return close_volume;
    }

    public void setClose_volume(double close_volume) {
        this.close_volume = close_volume;
    }

    public double getClose_pitch() {
        return close_pitch;
    }

    public void setClose_pitch(double close_pitch) {
        this.close_pitch = close_pitch;
    }

    public List<String> getClose_message() {
        return close_message;
    }

    public void setClose_message(List<String> close_message) {
        this.close_message = close_message;
    }

    public List<String> getClose_commandPlayer() {
        return close_commandPlayer;
    }

    public void setClose_commandPlayer(List<String> close_commandPlayer) {
        this.close_commandPlayer = close_commandPlayer;
    }

    public List<String> getClose_commandConsole() {
        return close_commandConsole;
    }

    public void setClose_commandConsole(List<String> close_commandConsole) {
        this.close_commandConsole = close_commandConsole;
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
