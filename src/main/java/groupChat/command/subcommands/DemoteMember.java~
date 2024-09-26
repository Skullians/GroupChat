package groupChat.command.subcommands;

import groupChat.object.Group;
import groupChat.GroupChat;
import groupChat.command.GroupSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import groupChat.config.Lang;

public class DemoteMember extends GroupSubCommand {

    public DemoteMember(GroupChat plugin) {
        super(plugin, "demote");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (utils.getPlayerGroup(player.getUniqueId()) == null) {
            utils.fail(player, "You are not a member of any group.");
            return;
        }

        Group group = utils.getPlayerGroup(player.getUniqueId());

        if (!group.getLeader().equals(player.getUniqueId())) {
            utils.fail(player, "Only the group leader can do that.");
            return;
        }

        if (args.length == 1) {
            utils.message(player, "Usage: /group demote <" + Lang.TRUSTED_RANK + ">");
            return;
        }

        @SuppressWarnings("deprecation")
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]); // the captain to be kicked

        if (!target.hasPlayedBefore() && !target.isOnline()) {
            utils.fail(player, "Couldn't find that player.");
            return;
        }

        if (!group.getCaptains().contains(target.getUniqueId())) {
            utils.fail(player, "That player is not a " + Lang.TRUSTED_RANK + ".");
            return;
        }

        String path = group.name() + ".captains";
        utils.message(player, "You have demoted " + target.getName() + ".");
        plugin.getYml().set(path, plugin.getYml().getStringList(path).remove(target.getUniqueId().toString()));
        plugin.save();
    }

    @Override
    public String description() {
        return "Revoke invite and kick permissions.";
    }

    @Override
    public String usage() {
        return "/group demote <player>";
    }
}
