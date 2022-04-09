package group;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public abstract class GroupSubCommand {
    protected GroupChat plugin;
    protected GroupChatUtils utils;
    private final String name;

    public GroupSubCommand(GroupChat groupChat, String name) {
        this.plugin = groupChat;
        this.name = name;
        this.utils = groupChat.getUtils();
    }

    public String getName() {
        return name;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }

    /*
    All group commands are player-only
     */
    public abstract void execute(Player player, String[] args);

    public abstract String description();

    public abstract String usage();
}
