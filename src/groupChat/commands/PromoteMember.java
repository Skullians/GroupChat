package groupChat.commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import groupChat.GroupChat;
import groupChat.SubCommand;

public class PromoteMember extends SubCommand {
	public PromoteMember(GroupChat group) {
		super(group, "promote");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		UUID uuid = player.getUniqueId();
		String groupName = group.getPlayerGroup(uuid.toString());
		String trustedPath = groupName + ".captains";

		if (args.length == 1) {
			group.message(sender, "Usage: /group promote <player>");
			return;
		}

		if (!group.playerInGroup(player)) {
			group.message(sender, "You are not in a group.");
			return;
		}

		if (!group.playerIsGroupStaff(player, groupName)) {
			group.fail(sender, "You do not have permission to do that.");
			return;
		}

		if (Bukkit.getPlayer(args[1]) == null) {
			group.fail(sender, "That player isn't online.");
			return;
		}

		Player captain = Bukkit.getPlayer(args[1]);

		if (!group.getPlayerGroup(captain.getUniqueId().toString()).equalsIgnoreCase(groupName)
				|| !captain.hasPlayedBefore()) {
			group.message(sender, captain.getName() + " is not a member of your group.");
			return;
		}

		if (group.playerIsCaptain(captain.getUniqueId().toString(), groupName)
				|| group.playerIsLeader(captain.getPlayer(), groupName)) {
			group.fail(sender, "That player is already a group " + group.trustedRank + ".");
			return;
		}

		List<String> list = group.getYml().getStringList(groupName + ".captains");
		list.add(captain.getUniqueId().toString());
		group.broadcast(groupName, captain.getName() + " has been promoted to group " + group.trustedRank + " by "
				+ sender.getName() + ".");
		group.getYml().set(trustedPath, list);
		group.save();
	}

	@Override
	public String description() {
		return "Give a member invite and kick permissions";
	}

	@Override
	public String usage() {
		return "/group promote <player>";
	}
}
