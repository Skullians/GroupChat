package groupChat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import groupChat.GroupChat;
import groupChat.SubCommand;

public class ToggleGroupChat extends SubCommand {
	public ToggleGroupChat(GroupChat group) {
		super(group, "togglechat");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;

		if (!group.playerInGroup(player)) {
			group.fail(sender, "You are not in a group.");
			return;
		}

		FileConfiguration config = group.getConfig();
		String path = "groupChat." + player.getUniqueId().toString();

		if (config.getBoolean(path)) {
			config.set(path, false);
			player.sendMessage(group.groupColor + "> " + ChatColor.RESET + "Default chat has been set to "
					+ ChatColor.GREEN + "Public Chat" + ChatColor.RESET + ".");
		} else {
			config.set(path, true);
			player.sendMessage(group.groupColor + "> " + ChatColor.RESET + "Default chat has been set to "
					+ group.groupColor + "Group Chat" + ChatColor.RESET + ".");

		}

		GroupChat.plugin.saveConfig();
		return;
	}

	@Override
	public String description() {
		return "Toggle default chat setting";
	}

	@Override
	public String usage() {
		return "/group togglechat";
	}
}
