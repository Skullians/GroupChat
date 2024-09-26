package groupChat.command.subcommands;

import groupChat.GroupChat;
import groupChat.command.GroupSubCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import groupChat.util.Chat;
import groupChat.util.Utils;

import java.util.ArrayList;
import java.util.Collections;

public class CreateGroup extends GroupSubCommand {

    public CreateGroup(GroupChat plugin) {
        super(plugin, "create");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (utils.getPlayerGroup(player.getUniqueId()) != null) {
            utils.fail(player, "You are already in a party.");
            return;
        }

        if (args.length == 1) {
            utils.message(player, "Usage: /party create <name>");
            return;
        }

        if (!utils.isValidName(args[1])) {
            utils.fail(player, "That's not a valid party name. (Minimum 4 characters, maximum 16)");
            return;
        }

        String name = args[1];
        YamlConfiguration yml = plugin.getYml();
        ConfigurationSection theGroup = yml.getConfigurationSection(name);

        if (theGroup != null) {
            utils.fail(player, "The party \"" + Chat.GRAY + name + Chat.RESET + "\" already exists.");
            return;
        }

        if (args.length < 3) {
            utils.message(player, "You are about to create the party \"" + Chat.RED + name + Chat.WHITE + "\"" + ".");
            utils.message(player, "Are you sure? Type " + Chat.RED + "/party create <name> confirm " + Chat.WHITE + "to confirm.");
        } else if (args[2].equalsIgnoreCase("confirm")) {
            theGroup = yml.createSection(name);

            theGroup.set(".created", Utils.getCurrentDateString());
            theGroup.set(".leader", player.getUniqueId().toString());
            theGroup.set(".members", new ArrayList<>(Collections.singleton(player.getUniqueId().toString())));
            theGroup.set(".isGuild", false);
            theGroup.set(".pointBalance", 0.0);

            utils.message(player, "You have created the party " + name + "! Type /party help for a list of commands.");
            plugin.save();
        }

    }

    @Override
    public String description() {
        return "Create a new party";
    }

    @Override
    public String usage() {
        return "/party create <name>";
    }
}
