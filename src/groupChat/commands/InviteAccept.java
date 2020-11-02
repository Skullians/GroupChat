package groupChat.commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import groupChat.GroupChat;
import groupChat.SubCommand;
import groupChat.Utils;

public class InviteAccept extends SubCommand {
	public InviteAccept(GroupChat group) {
		super(group, "accept");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		UUID uuid = player.getUniqueId();

		if (!group.inviteMap.containsKey(player.getUniqueId())) {
			group.fail(sender, "You do not have a pending invite.");
			return;
		}

		String leaderID = group.inviteMap.get(uuid).toString();
		String groupToJoin = group.getPlayerGroup(leaderID.toString());
		String memberPath = group.getPlayerGroup(leaderID) + ".members";

		List<String> listOfMembers = group.getYml().getStringList(groupToJoin + ".members");
		listOfMembers.add(player.getUniqueId().toString());
		group.getYml().set(memberPath, listOfMembers);

		group.inviteMap.remove(uuid);
		group.broadcast(groupToJoin, player.getName() + ChatColor.RESET + " has joined the group!");
		Utils.delayMessage(player, group.groupColor + "> " + ChatColor.RESET + "Hint: " + ChatColor.GRAY
				+ "Type /g <message> to talk within your group.", 80);
		group.tellAdmins(sender, groupToJoin, player.getName() + " joined " + groupToJoin + ".");
		group.save();
	}

	@Override
	public String description() {
		return "Accept an invite";
	}

	@Override
	public String usage() {
		return "/group accept";
	}
}
