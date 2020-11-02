package groupChat.commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import groupChat.GroupChat;
import groupChat.SubCommand;
import groupChat.Utils;

public class DeleteGroup extends SubCommand {
	public DeleteGroup(GroupChat group) {
		super(group, "disband");
	}

	private String warn = ChatColor.RED + "[Warning] " + ChatColor.RESET;

	@Override
	public void execute(CommandSender sender, String[] args) {
		YamlConfiguration yml = group.getYml();
		Player player = (Player) sender;
		UUID senderUUID = player.getUniqueId();
		boolean canDisbandAny = Utils.isAdmin(player);

		if (args.length < 2) {
			if (canDisbandAny) {
				group.message(sender, "Usage: /group disband <groupName>");
				return;
			} else {
				sender.sendMessage(warn + "Disbanding a group is a permanent action! It cannot be undone.");
				sender.sendMessage(warn + "To disband your group forever, type \"" + ChatColor.GRAY + "/group disband "
						+ group.getPlayerGroup(senderUUID.toString()) + ChatColor.RESET + "\".");
				return;
			}
		}

		if (group.getYml().getConfigurationSection(args[1]) == null) {
			group.fail(sender, "That isn't a valid group.");
			return;
		}

		String groupName = args[1];
		UUID groupLeaderUUID = UUID.fromString(yml.getString(groupName + ".leader"));
		if (canDisbandAny) {
			if (groupLeaderUUID != senderUUID) {
				areYouSureStaff(player, groupName, args);
				return;
			}
		}

		if (!group.playerIsLeader(player, groupName)) {
			group.fail(player, "You do not have permission to do that.");
			return;
		} else {
			areYouSure(player, groupName, args);
		}
	}

	private void areYouSure(Player player, String toDelete, String[] args) {
		ConfirmCommand.confirmationMap.put(player.getUniqueId(), "deleteGroup");
		ConfirmCommand.groupArgs.put(player.getUniqueId(), args);
		player.sendMessage(warn + "You are about to delete " + ChatColor.GRAY + toDelete + ChatColor.RESET + " forever.");
		player.sendMessage(warn + "Are you sure? Type " + ChatColor.GRAY + "/confirm " + ChatColor.RESET + "to continue.");
	}

	private void areYouSureStaff(Player player, String toDelete, String[] args) {
		ConfirmCommand.confirmationMap.put(player.getUniqueId(), "deleteGroup");
		ConfirmCommand.groupArgs.put(player.getUniqueId(), args);
		player.sendMessage(warn + "You are about to delete " + ChatColor.RED + toDelete + ChatColor.RESET + " forever.");
		player.sendMessage(warn + "This group has " + ChatColor.RED + group.size(toDelete) + ChatColor.RESET + " members.");
		player.sendMessage(warn + "Are you sure? Type " + ChatColor.GRAY + "/confirm " + ChatColor.RESET + "to continue.");
	}

	@Override
	public String description() {
		return "Delete a group";
	}

	@Override
	public String usage() {
		return "/group disband";
	}
}
