package groupChat.commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import groupChat.GroupChat;
import groupChat.SubCommand;

public class DemoteMember extends SubCommand {
	
	public DemoteMember(GroupChat group) {
		super(group, "demote");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		UUID uuid = player.getUniqueId();
		String groupName = group.getPlayerGroup(uuid.toString());
		String trustedPath = groupName + ".captains";
		
		if (!group.playerIsGroupStaff(player, groupName)) {
			group.fail(sender, "You do not have permission to do that.");
			return;
		}

		if (args.length == 1) {
			group.message(sender, "Usage: /group demote <player>");
			return;
		}
		
		OfflinePlayer captain = Bukkit.getOfflinePlayer(args[1]);
		
		if (!group.playerIsCaptain(captain.getUniqueId().toString(), groupName)) {
			group.fail(sender, "That player is not a group " + group.trustedRank + ".");
			return;
		}

		List<String> list = group.getYml().getStringList(groupName + ".captains");
		list.remove(captain.getUniqueId().toString());
		group.message(sender, "Successfully demoted " + captain.getName() + ".");
		group.getYml().set(trustedPath, list);
		group.save();
	}

	@Override
	public String description() {
		return "Revoke invite and kick permissions";
	}

	@Override
	public String usage() {
		return "/group demote <player>";
	}
}
