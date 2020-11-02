package groupChat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import groupChat.GroupChat;
import groupChat.SubCommand;

public class InviteMember extends SubCommand {
	public InviteMember(GroupChat group) {
		super(group, "invite");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 1) {
			group.message(sender, "Usage: /group invite <player>");
			return;
		}

		if (Bukkit.getPlayer(args[1]) == null) {
			group.fail(sender, "That player is not online.");
			return;
		}

		Player invited = Bukkit.getPlayer(args[1]);
		Player inviteSender = (Player) sender;
		String groupName = group.getPlayerGroup(inviteSender.getUniqueId().toString());

		if (!group.playerInGroup(inviteSender)) {
			group.fail(sender, "You must be in a group to invite players.");
			return;
		}
		if (!group.playerIsGroupStaff(inviteSender, groupName)) {
			group.fail(sender, "You do not have permission to invite players.");
			return;
		}
		if (invited == inviteSender) {
			group.fail(sender, "You cannot invite yourself.");
			return;
		}
		if (group.playerInGroup(invited)) {
			group.fail(sender, invited.getName() + " is already in a group.");
			return;
		}
		if (group.inviteMap.containsValue(inviteSender.getUniqueId())) {
			group.fail(sender, invited.getName() + " you already have a pending invite.");
			return;
		}

		group.message(inviteSender, "You sent " + invited.getName() + " an invite to join " + groupName + ".");
		invited.sendMessage("");
		invited.sendMessage(ChatColor.DARK_GRAY + " >" + ChatColor.WHITE + " You have been invited by "
				+ inviteSender.getName() + " to join " + groupName + ChatColor.WHITE + ".");
		invited.sendMessage(
				ChatColor.DARK_GRAY + " >" + ChatColor.WHITE + " Respond with " + ChatColor.GREEN + "/group accept"
						+ ChatColor.WHITE + " or " + ChatColor.RED + "/group decline" + ChatColor.WHITE + ".");
		invited.sendMessage("");
		group.inviteMap.put(invited.getUniqueId(), inviteSender.getUniqueId());
	}

	@Override
	public String description() {
		return "Invite a player to your group";
	}

	@Override
	public String usage() {
		return "/group invite <player>";
	}
}
