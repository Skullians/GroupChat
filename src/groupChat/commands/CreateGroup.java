package groupChat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import groupChat.GroupChat;
import groupChat.SubCommand;


public class CreateGroup extends SubCommand {
	public CreateGroup(GroupChat group) {
		super(group, "create");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 1) {
			group.message(sender, "Usage: /group create <name>");
			return;
		}

		String groupName = args[1];
		Player creator = (Player) sender;

		if (group.playerInGroup(creator)) {
			group.fail(sender, "You are already in a group.");
			return;
		}
		if (args.length > 2) {
			group.fail(sender, "Group names cannot contain a space.");
			return;
		}
		if (groupName.length() < 4 || groupName.length() > 16) {
			group.fail(sender, "Group names must be between 4 and 16 characters.");
			return;
		}
		if (chatInArray(groupName, disallowedName)) {
			group.fail(sender, "Your group name contains disallowed characters.");
			return;
		}
		if (groupAlreadyExists(groupName)) {
			group.fail(sender, "The group \"" + ChatColor.GRAY + groupName + ChatColor.RESET + "\" already exists.");
			return;
		}

		// Confirm creation of group
		ConfirmCommand.confirmationMap.put(creator.getUniqueId(), "createGroup");
		ConfirmCommand.groupArgs.put(creator.getUniqueId(), args);
		group.message(sender, "You are about to create the group \"" + ChatColor.RED + groupName + ChatColor.WHITE + "\"" + ".");
		group.message(sender, "Are you sure? Type " + ChatColor.GRAY + "/confirm " + ChatColor.WHITE + "to continue.");
	}

	private boolean groupAlreadyExists(String input) {
		for (String groupName : group.getYml().getKeys(false)) {
			if (groupName.toLowerCase().contains(input.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	private String[] disallowedName = { "faggot", "nigger", "nigga", "natzi", "cunt", "fuck", "taliban", ".", "confirm",
			",", "'", "\"", "!", ")", ";", "(", "[", "]", "/", "?", "'", "}", "{", "^", "*" };

	private boolean chatInArray(String input, String[] array) {
		for (int i = 0; i < array.length; i++) {
			if (input.contains(array[i].replace("i", "1")) || input.equalsIgnoreCase(array[i])
					|| (input.contains(array[i]))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String description() {
		return "Create a new group";
	}

	@Override
	public String usage() {
		return "/group create <name>";
	}
}
