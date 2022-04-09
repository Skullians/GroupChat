package groupChat.command;

import groupChat.object.Group;
import groupChat.GroupChat;
import groupChat.util.GroupChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import groupChat.util.Chat;

import java.util.ArrayList;
import java.util.List;

/*
Handles the base /group command
 */
public class CommandBase implements CommandExecutor, TabCompleter {
    private final GroupChat plugin;
    private final GroupChatUtils utils;

    private final List<String> subCmdNames = new ArrayList<>();

    public CommandBase(GroupChat plugin) {
        this.plugin = plugin;
        this.utils = plugin.getUtils();

        plugin.getCommand("group").setExecutor(this); // Register the command with Bukkit

        // Load command names for auto completion
        plugin.getCommands().forEach(cmd -> subCmdNames.add(cmd.getName()));
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Chat.RED + "All group commands are player only. Sorry!");
            return true;
        }

        if (args.length == 0) {
            Group group = utils.getPlayerGroup(player.getUniqueId());

            if (group == null) {
                utils.displayMenu(player);
            } else {
                group.displayGroupInfo(player);
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
            utils.displayMenu(player);
        }

        for (GroupSubCommand subCommand : plugin.getCommands()) {
            if (subCommand.getName().equals(args[0])) {
                subCommand.execute(player, args);
                break;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, String[] args) {
        List<String> result = new ArrayList<>();
        Player player = (Player) commandSender;

        if (args.length == 1) {
            for (String a : subCmdNames) {
                if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
                    result.add(a);
                }
            }
            return result;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("info"))
                return getResults(args, utils.getGroupNames());

            if (utils.getPlayerGroup(player.getUniqueId()) == null) { // player ain't in a group
                return List.of();
            }

            Group group = utils.getPlayerGroup(player.getUniqueId());

            // return online players
            if (args[0].equalsIgnoreCase("invite")) {
                List<String> names = new ArrayList<>();
                Bukkit.getOnlinePlayers().forEach(p -> {
                    if (p != player) names.add(p.getName());
                });
                return names;
            }

            // return all players in group, minus the leader
            if (args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("promote")) {
                List<String> membersMinusLeader = group.getMemberUsernames();
                group.getMemberUsernames().remove(Bukkit.getOfflinePlayer(group.getLeader()).getName());

                return getResults(args, membersMinusLeader);
            }

            if (args[0].equalsIgnoreCase("demote")) {
                return getResults(args, group.getCaptainUsernames());
            }

            return result;
        }

        return List.of(); // Return an empty array so it stops tab completing
    }

    private List<String> getResults(String[] args, List<String> toSearch) {
        List<String> results = new ArrayList<>();
        for (String s : toSearch) {
            if (s.toLowerCase().startsWith(args[1].toLowerCase()))
                results.add(s);
        }
        return results;
    }

}
