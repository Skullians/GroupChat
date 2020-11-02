package groupChat.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import groupChat.GroupChat;
import groupChat.Utils;

public class ConfirmCommand implements CommandExecutor {
	public static Map<UUID, String[]> groupArgs = new HashMap<UUID, String[]>();
	public static Map<UUID, String> confirmationMap = new HashMap<UUID, String>();
	private GroupChat group;

	public ConfirmCommand(GroupChat group) {
		this.group = group;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage("That is a player only command.");
			return true;
		}

		Player player = (Player) sender;
		UUID uuid = player.getUniqueId();

		if (confirmationMap.get(uuid) == null) {
			sender.sendMessage("You don't have anything to confirm.");
			return true;
		}

		String data = confirmationMap.get(uuid);

		if (data.equals("createGroup")) {
			String groupName = groupArgs.get(uuid)[1];
			YamlConfiguration yml = group.getYml();
			List<String> listOfMembers = new ArrayList<String>();
			listOfMembers.add(player.getUniqueId().toString());

			// Create group
			yml.createSection(groupName + ".created");
			yml.createSection(groupName + ".leader");
			yml.createSection(groupName + ".captains");
			yml.createSection(groupName + ".members");
			yml.set(groupName + ".created", Utils.getCurrentDate());
			yml.set(groupName + ".leader", player.getUniqueId().toString());
			yml.set(groupName + ".members", listOfMembers);

			// Tell people stuff
			String name = ChatColor.RED + groupName + ChatColor.RESET;
			for (Player p : Bukkit.getOnlinePlayers())
				Utils.delayMessage(p, group.groupColor + "[GroupChat] " + ChatColor.RESET + player.getName()
						+ " has created the group \"" + groupName + "\"!", 80);
			group.message(sender, "You have created the group " + name + ".");
			group.save();
			confirmationMap.put(uuid, null);
			return true;
		}

		if (data.equals("deleteGroup")) {
			String groupName = groupArgs.get(uuid)[1];
			group.tellAdmins(player, groupName, player.getName() + " disbanded the group " + groupName + ".");
			group.broadcast(groupName, "Your group has been disbanded.");
			group.getYml().set(groupName, null);
			group.save();
			confirmationMap.put(uuid, null);
			return true;
		}

		if (data.equals("leaveGroup")) {
			String groupName = group.getPlayerGroup(player.getUniqueId().toString());
			List<String> listOfMembers = group.getYml().getStringList(groupName + ".members");
			listOfMembers.remove(player.getUniqueId().toString());
			String memberPath = groupName + ".members";
			group.getYml().set(memberPath, listOfMembers);
			group.message(player, "You left " + groupName + ".");
			group.broadcast(groupName, player.getName() + " has left the group.");
			group.tellAdmins(player, groupName, player.getName() + " left " + groupName + ".");
			group.save();
			confirmationMap.put(uuid, null);
		}
		return false;

	}
}
