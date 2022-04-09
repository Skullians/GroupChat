package groupChat.command.subcommands;

import groupChat.object.Group;
import groupChat.GroupChat;
import groupChat.command.GroupSubCommand;
import org.bukkit.entity.Player;
import groupChat.util.Chat;

public class DisbandGroup extends GroupSubCommand {
    public DisbandGroup(GroupChat plugin) {
        super(plugin, "disband");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (utils.getPlayerGroup(player.getUniqueId()) == null) {
            utils.fail(player, "You are not a member of any group.");
            return;
        }

        Group group = utils.getPlayerGroup(player.getUniqueId());

        if (!group.getLeader().equals(player.getUniqueId())) {
            utils.fail(player, "Only the leader can disband the group.");
            return;
        }

        if (args.length < 3) {
            var WARN = Chat.RED + "[!] " + Chat.RESET;
            player.sendMessage(WARN + "You are about to disband " + Chat.GRAY + group.name() + Chat.WHITE + " forever.");
            player.sendMessage(WARN + "Are you sure? Type " + Chat.RED + "/group disband <groupName> confirm" + Chat.RESET + " to continue.");
        } else if (args[1].equals(group.name()) && args[2].equals("confirm")) {
            group.broadcast("Your group has been disbanded.");
            plugin.getYml().set(group.name(), null);
            plugin.save();
        }
    }


    @Override
    public String description() {
        return "Disband a group";
    }

    @Override
    public String usage() {
        return "/group disband";
    }
}
