package groupChat.commands;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import groupChat.GroupChat;
import groupChat.SubCommand;

public class InviteDecline extends SubCommand {
	public InviteDecline(GroupChat group) {
		super(group, "decline");
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
		String groupName = group.getPlayerGroup(leaderID);
		
		group.message(sender, "You declined the invite to join " + groupName + ".");
		group.broadcast(groupName, sender.getName() + " declined the invite to join " + groupName + ".");
		group.inviteMap.remove(uuid);
	}

	@Override
	public String description() {
		return "Decline an invite";
	}

	@Override
	public String usage() {
		return "/group decline";
	}
}
