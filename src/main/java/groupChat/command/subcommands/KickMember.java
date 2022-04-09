package groupChat.command.subcommands;

import groupChat.object.Group;
import groupChat.GroupChat;
import groupChat.command.GroupSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class KickMember extends GroupSubCommand {
    public KickMember(GroupChat plugin) {
        super(plugin, "kick");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (utils.getPlayerGroup(player.getUniqueId()) == null) {
            utils.fail(player, "You are not in any group.");
            return;
        }

        Group group = utils.getPlayerGroup(player.getUniqueId());

        if (!group.isLeader(player.getUniqueId())) {
            utils.fail(player, "Only the group leader can do that.");
            return;
        }

        if (args.length == 1) {
            utils.message(player, "Usage: /group kick <player>");
            return;
        }

        @SuppressWarnings("deprecation")
        OfflinePlayer toKick = Bukkit.getOfflinePlayer(args[1]);

        if (toKick.hasPlayedBefore() && !toKick.isOnline()) {
            utils.fail(player, "That's not a valid group member.");
            return;
        }

        if (!group.getMembers().contains(toKick.getUniqueId())) {
            utils.fail(player, "That player isn't a member of your group.");
            return;
        }

        if (toKick == player) {
            utils.message(player, "You cannot kick yourself!");
            return;
        }

        List<String> list = plugin.getYml().getStringList(group.name() + ".members");
        list.remove(toKick.getUniqueId().toString());
        plugin.getYml().set(group.name() + ".members", list);

        // we need to remove player from captain list, if they're a captain
        if (group.isGroupStaff(toKick.getUniqueId())) {
            List<String> captains = plugin.getYml().getStringList(group.name() + ".captains");
            captains.remove(toKick.getUniqueId().toString());
            plugin.getYml().set(group.name() + ".captains", captains);
        }

        if (toKick.isOnline()) {
            utils.message(((Player) toKick), "You have been kicked from the group.");
        }

        plugin.getYml().set(group.name() + ".members", list);
        plugin.save();
        group.broadcast(toKick.getName() + " has been kicked from the group.");
    }

    @Override
    public String description() {
        return "Kick a player from the group.";
    }

    @Override
    public String usage() {
        return "/group kick <player>";
    }
}
