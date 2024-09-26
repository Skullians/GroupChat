package groupChat.command.subcommands;

import groupChat.GroupChat;
import groupChat.command.GroupSubCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import groupChat.util.Chat;

/*
This class is a bit gross - Nick
 */
public class ToggleGroupChat extends GroupSubCommand {
    public ToggleGroupChat(GroupChat plugin) {
        super(plugin, "togglechat");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (utils.getPlayerGroup(player.getUniqueId()) == null) {
            utils.fail(player, "You are not a member of any party.");
            return;
        }

        FileConfiguration config = plugin.getConfig();
        String path = "groupChat." + player.getUniqueId();

        if (config.getBoolean(path)) {
            config.set(path, false);
            utils.message(player, "Default chat has been set to "
                    + Chat.GREEN + "Public Chat" + Chat.RESET + ".");
        } else {
            config.set(path, true);

            utils.message(player, "Default chat has been set to " + Chat.AQUA + "Party Chat" + Chat.RESET + ".");
        }

        plugin.saveConfig();
    }

    @Override
    public String description() {
        return "Toggle default chat setting.";
    }

    @Override
    public String usage() {
        return "/party togglechat";
    }
}
