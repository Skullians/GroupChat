package groupChat.command.subcommands;

import groupChat.object.Group;
import groupChat.GroupChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import groupChat.util.Chat;

/**
 * This class does not inherit GroupSubCommand because this is just the /g command
 */
public class GroupChatCommand implements CommandExecutor {
    private final GroupChat plugin;

    public GroupChatCommand(GroupChat plugin) {
        this.plugin = plugin;

        plugin.getCommand("p").setExecutor(this);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(Chat.RED + "Sorry, that's a player only command.");
            return true;
        }

        Player player = (Player) sender;
        Group group = plugin.getUtils().getPlayerGroup(player.getUniqueId());

        if (group == null || args.length == 0) {
            plugin.getUtils().displayMenu(player);
            return true;
        }

        StringBuilder message = new StringBuilder();

        for (String arg : args) {
            message.append(arg).append(" ");
        }

        group.broadcast(player.getName() + ": " + message);
        return false;
    }

}
