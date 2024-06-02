package me.messdaniel.configui.listener;

import me.messdaniel.configui.ConfigUI;
import me.messdaniel.configui.manager.ConfigUIManger;
import me.messdaniel.configui.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryEvent implements Listener {

    private ConfigUIManger configUIManger = ConfigUI.getInstance().getConfigUIManger();

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        for (Menu menu : configUIManger.getAllGuis().values()) {
            if (!event.getView().getTitle().equalsIgnoreCase(menu.getTitle())) return;
            ConfigUI.getInstance().getConfigUIManger().closeMenu((Player) event.getPlayer(),menu);
            return;
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)  {
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null || event.getCurrentItem().getType().isAir()) return;
        for (Menu menu : configUIManger.getAllGuis().values()) {
            if (!event.getView().getTitle().equalsIgnoreCase(menu.getTitle())) return;
            if (menu.isDisabledMovingItems() && !player.getItemOnCursor().getType().isAir()) event.setCancelled(true);
            ConfigUI.getInstance().getConfigUIManger().clickAction(event,menu);
            return;
        }
    }
}
