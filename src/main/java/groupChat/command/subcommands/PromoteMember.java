package groupChat.command.subcommands;

import groupChat.object.Group;
import groupChat.GroupChat;
import groupChat.command.GroupSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import groupChat.config.Lang;

import java.util.List;

public class PromoteMember extends GroupSubCommand {
	public PromoteMember(GroupChat plugin) {
		super(plugin, "promote");
	}

	@Override
	public void execute(Player player, String[] args) {
		if (utils.getPlayerGroup(player.getUniqueId()) == null) {
			utils.message(player, "You are not in a party.");
			return;
		}

		Group group = utils.getPlayerGroup(player.getUniqueId());

		if (!group.isLeader(player.getUniqueId())) {
			utils.fail(player, "Only the party leader can do that.");
			return;
		}

		if (args.length == 1) {
			utils.message(player, "Usage: /party promote <player>");
			return;
		}

		@SuppressWarnings("deprecation")
		OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

		if (!target.hasPlayedBefore() && !target.isOnline()) {
			utils.fail(player, "That player has never joined the server before.");
			return;
		}

		if (!group.getMembers().contains(target.getUniqueId())) {
			utils.message(player, target.getName() + " is not a member of your party.");
			return;
		}

		if (group.isGroupStaff(target.getUniqueId())) {
			utils.message(player, "You cannot promote that party member any further.");
			return;
		}

		List<String> list = plugin.getYml().getStringList(group.name() + ".captains");
		list.add(target.getUniqueId().toString());
		group.broadcast(target.getName() + " has been promoted to party " + Lang.TRUSTED_RANK + " by "
				+ player.getName() + ".");
		plugin.getYml().set(group.name() + ".captains", list);
		plugin.save();
	}

	@Override
	public String description() {
		return "Give a member invite and kick permissions.";
	}

	@Override
	public String usage() {
		return "/party promote <player>";
	}
}
