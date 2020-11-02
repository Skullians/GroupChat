package groupChat.commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import groupChat.GroupChat;
import groupChat.SubCommand;

public class LeaveGroup extends SubCommand {
	public LeaveGroup(GroupChat group) {
		super(group, "leave");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		UUID uuid = player.getUniqueId();

		if (!group.playerInGroup(player)) {
			group.fail(sender, "You are not in a group.");
			return;
		}

		String groupName = group.getPlayerGroup(uuid.toString());

		if (group.getLeader(groupName) == player.getName()) {
			group.fail(sender, "You are the group leader. You cannot leave.");
			return;
		}

		ConfirmCommand.confirmationMap.put(player.getUniqueId(), "leaveGroup");
		ConfirmCommand.groupArgs.put(player.getUniqueId(), args);
		group.message(sender, "Are you sure you want to leave " + ChatColor.DARK_GRAY + groupName + ChatColor.RESET
				+ "? Type /confirm to continue.");
	}

	@Override
	public String description() {
		return "Leave a group";
	}

	@Override
	public String usage() {
		return "/group leave";
	}
}
