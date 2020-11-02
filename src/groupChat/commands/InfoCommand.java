package groupChat.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import groupChat.GroupChat;
import groupChat.SubCommand;
import groupChat.Utils;

public class InfoCommand extends SubCommand {
	public InfoCommand(GroupChat group) {
		super(group, "info");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length < 2) {
			group.message(sender, "Usage: /group info <group>");
			return;
		}
		
		String groupName = args[1];
		
		for (String string : group.getYml().getKeys(false)) {
			if (string.toLowerCase().contains(groupName.toLowerCase())) {
				groupName = string;
			} 
		}
		
		if (group.getCreationDate(groupName) == null) {
			group.message(sender, "There isn't a group by that name.");
			return;
		}
		
		String arrow = ChatColor.DARK_GRAY + "> " + group.groupColor;
		String bar = ChatColor.GRAY + Utils.createBar(15);
		sender.sendMessage(bar + ChatColor.RESET + " " + groupName + " " + bar);
		sender.sendMessage(arrow + "Leader: " + ChatColor.GRAY + group.getLeader(groupName));
		if (!group.getCaptains(groupName).isEmpty()) {
			sender.sendMessage(arrow + StringUtils.capitalize(group.trustedRank) + "s: " + ChatColor.GRAY
					+ group.getCaptains(groupName));
		}
		sender.sendMessage(arrow + "Members: " + ChatColor.GRAY + group.size(groupName));
		sender.sendMessage(arrow + "Created on: " + ChatColor.GRAY + group.getCreationDate(groupName));
	}

	@Override
	public String description() {
		return "View information about a specific group";
	}

	@Override
	public String usage() {
		return "/group leave";
	}
}
