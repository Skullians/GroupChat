package groupChat.command.subcommands;

import groupChat.object.Group;
import groupChat.GroupChat;
import groupChat.command.GroupSubCommand;
import org.bukkit.entity.Player;
import groupChat.util.Chat;

import java.util.List;

public class LeaveGroup extends GroupSubCommand {
	public LeaveGroup(GroupChat plugin) {
		super(plugin, "leave");
	}

	@Override
	public void execute(Player player, String[] args) {
		if (utils.getPlayerGroup(player.getUniqueId()) == null) {
			utils.fail(player, "You are not in a party.");
			return;
		}

		Group group = utils.getPlayerGroup(player.getUniqueId());

		if (group.getLeader().equals(player.getUniqueId())) {
			utils.fail(player, "You are the party leader. To disband your party, type /party disband.");
			return;
		}

		if (args.length < 2) {
			utils.message(player, "Are you sure you want to leave " + Chat.GRAY + group.name() + Chat.RESET
					+ "? Type " + Chat.RED + "/party leave confirm " + Chat.RESET + "to continue.");
		} else if (args[1].equalsIgnoreCase("confirm")) {
			List<String> listOfMembers = plugin.getYml().getStringList(group.name() + ".members");
			listOfMembers.remove(player.getUniqueId().toString());
			String memberPath = group.name() + ".members";
			plugin.getYml().set(memberPath, listOfMembers);
			group.broadcast(player.getName() + " has left the party.");
			plugin.save();
		}
	}

	@Override
	public String description() {
		return "Leave a party.";
	}

	@Override
	public String usage() {
		return "/party leave";
	}
}
