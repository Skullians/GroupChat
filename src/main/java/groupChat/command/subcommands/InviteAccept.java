package groupChat.command.subcommands;

import groupChat.object.Group;
import groupChat.GroupChat;
import groupChat.object.Invite;
import groupChat.command.GroupSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import groupChat.util.Chat;

import java.util.List;

public class InviteAccept extends GroupSubCommand {
    public InviteAccept(GroupChat plugin) {
        super(plugin, "accept");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (plugin.inviteHandler().getInvite(player.getUniqueId()) == null) {
            utils.message(player, "You do not have a pending invite.");
            return;
        }

        Invite theInvite = plugin.inviteHandler().getInvite(player.getUniqueId());

        YamlConfiguration yml = plugin.getYml();

        Group group = theInvite.getGroup();

        List<String> l = yml.getStringList(group.name() + ".members");
        l.add(player.getUniqueId().toString());
        yml.set(group.name() + ".members", l);
        plugin.save();

        plugin.inviteHandler().removeInviteFromMap(theInvite);

        utils.message(player, "Please wait...");

        Bukkit.getScheduler().runTaskLater(plugin, () ->
                utils.message(player, "You have joined the party" + group.name() + "!"), 80);

        Bukkit.getScheduler().runTaskLater(plugin, () ->
                utils.message(player, "Hint: " + Chat.GRAY
                        + "Type /party <message> to talk within your party."), 240);

        group.broadcast(player.getName() + " has joined the party!");
    }

    @Override
    public String description() {
        return "Accept an invite.";
    }

    @Override
    public String usage() {
        return "/party accept";
    }
}
