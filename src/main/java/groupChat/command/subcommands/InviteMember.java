package groupChat.command.subcommands;

import groupChat.object.Group;
import groupChat.GroupChat;
import groupChat.object.Invite;
import groupChat.command.GroupSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import groupChat.util.Chat;
import groupChat.config.Lang;

import java.util.Objects;

public class InviteMember extends GroupSubCommand {
	public InviteMember(GroupChat plugin) {
		super(plugin, "invite");
	}

	@Override
	public void execute(Player player, String[] args) {
		if (args.length == 1) {
			utils.message(player, "Usage: /party invite <player>");
			return;
		}

		Group group = utils.getPlayerGroup(player.getUniqueId());

		if (group == null) {
			utils.message(player, "You are not a member of any party.");
			return;
		}

		if (!group.isGroupStaff(player.getUniqueId())) {
			utils.fail(player, "Only a leader or party " + Lang.TRUSTED_RANK + " can invite players.");
			return;
		}

		if (Bukkit.getPlayer(args[1]) == null) {
			utils.fail(player, "Couldn't find that player. Are they online?");
			return;
		}

		Player invited = Bukkit.getPlayer(args[1]);

		if (invited == player) {
			utils.fail(player, "You're already a member of your party.");
			return;
		}

		if (utils.getPlayerGroup(Objects.requireNonNull(invited).getUniqueId()) != null) {
			utils.fail(player, invited.getName() + " is already a member of a party.");
			return;
		}

		if (plugin.inviteHandler().getInvite(invited.getUniqueId()) != null) {
			utils.message(player, "That player already has an existing invite.");
			return;
		}

		plugin.inviteHandler().addInviteToMap(new Invite(group, player, invited.getUniqueId()));

		utils.message(player, "You have sent " + invited.getName() + " an invite to join " + group.name() + ".");
		invited.sendMessage("");
		invited.sendMessage(Chat.DARK_GRAY + " >" + Chat.RESET + " You have been invited by " + player.getName()
				+ " to join " + group.name() + Chat.RESET + "!");
		invited.sendMessage(Chat.DARK_GRAY + " >" + Chat.RESET + " Respond with " + Chat.GREEN + "/party accept"
				+ Chat.RESET + " or " + Chat.RED + "/party decline" + Chat.RESET + ".");
		invited.sendMessage("");
	}

	@Override
	public String description() {
		return "Invite a player to your party";
	}

	@Override
	public String usage() {
		return "/party invite <player>";
	}
}
