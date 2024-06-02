package me.messdaniel.configui.command;

import me.messdaniel.configui.ConfigUI;
import me.messdaniel.configui.utils.MessagesUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReloadCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission("configui.reload")) {
				MessagesUtils.sendMessage(player,"dont-have-perms");
				return true;
			}
			ConfigUI.getInstance().reloadConfig();
			MessagesUtils.load();
			boolean reload = ConfigUI.getInstance().getConfigUIManger().load();
			if (!reload) {
				MessagesUtils.sendMessage(player,"reload-not-successful");
			} else {
				MessagesUtils.sendMessage(player,"reload-successful");
				return true;
			}
			return true;
		}
		ConfigUI.getInstance().reloadConfig();
		MessagesUtils.load();
		boolean reload = ConfigUI.getInstance().getConfigUIManger().load();
		if (!reload) {
			ConfigUI.getInstance().getLogger().info(MessagesUtils.getMessage("reload-not-successful"));
		} else {
			ConfigUI.getInstance().getLogger().info(MessagesUtils.getMessage("reload-successful"));
			return true;
		}

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			completions.add("guis");
		}
		completions.removeIf(completion -> !completion.toLowerCase().startsWith(args[args.length - 1].toLowerCase()));
		Collections.sort(completions);
		return completions;
	}
}