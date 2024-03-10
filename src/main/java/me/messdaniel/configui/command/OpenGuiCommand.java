package me.messdaniel.configui.command;

import me.messdaniel.configui.ConfigUI;
import me.messdaniel.configui.manager.ConfigUIManger;
import me.messdaniel.configui.menu.Menu;
import me.messdaniel.configui.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class OpenGuiCommand implements CommandExecutor,TabCompleter {

    private ConfigUIManger configUIManger = ConfigUI.getInstance().getConfigUIManger();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessagesUtils.getMessage("only-player-command"));
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("configui.openmenu")) {
            MessagesUtils.sendMessage(player,"dont-have-perms");
            return true;
        }
        if (args.length == 1) {
            String menuName = args[0];
            Menu menu = configUIManger.getGui(menuName);
            if (menu == null) {
                MessagesUtils.sendMessage(player,"no-gui-found");
                return true;
            }
            configUIManger.openMenu(player,menu);
            MessagesUtils.sendMessage(player,"opened-gui","guiname",menuName);
        } else if (args.length == 2) {
            if (!player.hasPermission("configui.openmenu-other")) {
                MessagesUtils.sendMessage(player,"dont-have-perms");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                MessagesUtils.sendMessage(player,"no-player-online","name",args[1]);
                return true;
            }
            String menuName = args[0];
            Menu menu = configUIManger.getGui(menuName);
            if (menu == null) {
                MessagesUtils.sendMessage(player,"no-gui-found");
                return true;
            }
            configUIManger.openMenu(target,menu);
            MessagesUtils.sendMessage(player,"opened-gui-for-other","guiname",menuName,"player",target.getName());
        } else {
            MessagesUtils.sendMessage(player,"openmenu-usages");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.addAll(configUIManger.getAllGuis().keySet());
        } else if (args.length == 2 && sender.hasPermission("configui.openmenu-other")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        }
        completions.removeIf(completion -> !completion.toLowerCase().startsWith(args[args.length - 1].toLowerCase()));
        Collections.sort(completions);
        return completions;
    }
}
