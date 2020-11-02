package groupChat.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import groupChat.GroupChat;

public class TalkInGroup implements CommandExecutor {
	private GroupChat group;

	public TalkInGroup(GroupChat group) {
		this.group = group;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage("Player only command.");
			return true;
		}

		Player player = (Player) sender;
		if (!group.playerInGroup(player)) {
			group.commands.get(0).execute(sender, args); // Execute help command
			return true;
		}

		if (args.length == 0) {
			group.groupMenu(player);
			return true;
		}

		String message = "";
		for (int i = 0; i < args.length; i++)
			message = message + args[i] + " ";

		String name = player.getDisplayName();
		String uuid = player.getUniqueId().toString();
		String groupName = group.getPlayerGroup(uuid);

		List<Player> onlineMembers = group.getOnlineMembers(groupName);
		for (Player p : onlineMembers)
			p.sendMessage(group.groupColor + "<Group> " + ChatColor.RESET + name + ": " + message);
		return false;
	}
}
