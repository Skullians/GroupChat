package groupChat.command.subcommands;

import groupChat.GroupChat;
import groupChat.command.GroupSubCommand;
import org.bukkit.entity.Player;

public class InfoCommand extends GroupSubCommand {
    public InfoCommand(GroupChat group) {
        super(group, "info");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            utils.message(player, "Usage: /party info <party>");
            return;
        }

        if (utils.getGroupByName(args[1]) == null) {
            utils.message(player, "Couldn't find a party by that name.");
            return;
        }

        utils.getGroupByName(args[1]).displayGroupInfo(player);
    }

    @Override
    public String description() {
        return "View information about a specific party.";
    }

    @Override
    public String usage() {
        return "/party info <party>";
    }
}
