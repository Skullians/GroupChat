package groupChat.commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import groupChat.GroupChat;
import groupChat.SubCommand;

public class KickMember extends SubCommand {
	public KickMember(GroupChat group) {
		super(group, "kick");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		UUID uuid = player.getUniqueId();
		String groupName = group.getPlayerGroup(uuid.toString());

		if (args.length == 1) {
			group.message(sender, "Usage: /group kick <player>");
			return;
		}

		if (!group.playerIsGroupStaff(player, groupName)) {
			group.fail(sender, "You do not have permission to do that.");
			return;
		}

		if (Bukkit.getPlayer(args[1]) == null) {
			group.fail(sender, "Player not found.");
			return;
		}

		Player toKick = Bukkit.getPlayer(args[1]);

		if (!group.getPlayerGroup(toKick.getUniqueId().toString()).equalsIgnoreCase(groupName)) {
			group.fail(sender, "That player is not a member of " + groupName + ".");
			return;
		}

		if (toKick == player) {
			group.fail(sender, "You cannot kick yourself.");
			return;
		}

		String leaderID = Bukkit.getOfflinePlayer(group.getLeader(groupName)).getUniqueId().toString();
		
		if (toKick.getUniqueId().toString().equals(leaderID)) {
			group.fail(sender, "You cannot kick the group leader.");
			return;
		}

		List<String> listOfMembers = group.getYml().getStringList(groupName + ".members");
		listOfMembers.remove(toKick.getUniqueId().toString());
		group.getYml().set(groupName + ".members", listOfMembers);

		// Remove player from captain list, if applicable
		if (group.playerIsCaptain(toKick.getUniqueId().toString(), groupName)) {
			List<String> listOfCaptains = group.getYml().getStringList(groupName + ".captains");
			listOfCaptains.remove(toKick.getUniqueId().toString());
			group.getYml().set(groupName + ".captains", listOfCaptains);
		}

		group.getYml().set(groupName + ".members", listOfMembers);
		group.broadcast(groupName, toKick.getName() + " has left the group.");
		group.tellAdmins(sender, groupName,
				player.getName() + " kicked " + toKick.getName() + " from " + groupName + ".");
		group.save();
	}

	@Override
	public String description() {
		return "Kick a player from the group";
	}

	@Override
	public String usage() {
		return "/group kick <player>";
	}
}
