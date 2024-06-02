package me.messdaniel.configui.menu;

import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public class ClickEvent {

    private String name;
    private List<ClickType> clickType = new ArrayList<>();
    private Sound sound;
    private double volume;
    private double pitch;
    private List<String> message = new ArrayList<>();
    private List<String> commandPlayer = new ArrayList<>();
    private List<String> commandConsole = new ArrayList<>();
    private boolean allowMoving = false;

    public ClickEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ClickType> getClickType() {
        return clickType;
    }

    public void setClickType(List<ClickType> clickType) {
        this.clickType = clickType;
    }
    public void addClickType(ClickType clickType) {
        this.clickType.add(clickType);
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

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public List<String> getCommandPlayer() {
        return commandPlayer;
    }

    public void setCommandPlayer(List<String> commandPlayer) {
        this.commandPlayer = commandPlayer;
    }

    public List<String> getCommandConsole() {
        return commandConsole;
    }

    public void setCommandConsole(List<String> commandConsole) {
        this.commandConsole = commandConsole;
    }

    public boolean isAllowMoving() {
        return allowMoving;
    }

    public void setAllowMoving(boolean allowMoving) {
        this.allowMoving = allowMoving;
    }
}
