package groupChat.command.subcommands;

import groupChat.object.Group;
import groupChat.GroupChat;
import groupChat.object.Invite;
import groupChat.command.GroupSubCommand;
import org.bukkit.entity.Player;

public class InviteDecline extends GroupSubCommand {
    public InviteDecline(GroupChat plugin) {
        super(plugin, "decline");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (plugin.inviteHandler().getInvite(player.getUniqueId()) == null) {
            utils.message(player, "You do not have a pending invite.");
            return;
        }

		Invite theInvite = plugin.inviteHandler().getInvite(player.getUniqueId());
		Group group = theInvite.getGroup();

        utils.message(player, "You declined the invite to join " + group.name() + ".");
        group.broadcast(player.getName() + " declined the invite to join " + group.name() + ".");

        plugin.inviteHandler().removeInviteFromMap(theInvite);
    }

    @Override
    public String description() {
        return "Decline an invite.";
    }

    @Override
    public String usage() {
        return "/group decline";
    }
}
