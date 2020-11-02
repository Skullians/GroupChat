package groupChat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import groupChat.GroupChat;
import groupChat.SubCommand;
import groupChat.Utils;

public class HelpCommand extends SubCommand {

	public HelpCommand(GroupChat group) {
		super(group, "help");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		String bar = ChatColor.GRAY + Utils.createBar(30) + ChatColor.RESET;
		sender.sendMessage(bar + group.groupColor + " [GroupChat Help] " + bar);
		for (SubCommand cmd : group.commands) {
			sender.sendMessage(ChatColor.DARK_GRAY + "> " + ChatColor.GRAY + "/group " + group.groupColor + cmd.getName()
					+ ChatColor.RESET + " - " + cmd.description());
		}
	}

	@Override
	public String description() {
		return "View this menu";
	}

	@Override
	public String usage() {
		return "";
	}

}
