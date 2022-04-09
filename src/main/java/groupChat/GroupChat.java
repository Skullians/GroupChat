package groupChat;

import groupChat.command.*;
import groupChat.command.subcommands.*;
import groupChat.config.ConfigHandler;
import groupChat.object.Group;
import groupChat.object.InviteHandler;
import groupChat.util.GroupChatUtils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupChat extends JavaPlugin implements CommandExecutor, TabCompleter {
    private static final List<Group> groups = new ArrayList<>(); // Stores current list of groups, from YML
    private final List<GroupSubCommand> subCommands = new ArrayList<>(); // List of each group subcommand

    private ConfigHandler config;
    private InviteHandler inviteHandler;
    private GroupChatUtils groupChatUtils;
    private YamlConfiguration groupYml;

    private File groupFile;
    public static final String VERSION = "2.0.0";

    public void onEnable() {
        groupFile = new File(getDataFolder(), "groups.yml");
        groupYml = YamlConfiguration.loadConfiguration(groupFile);

        rebuildGroupList(); // On startup, load group data from groups.yml into memory

        config = new ConfigHandler();
        inviteHandler = new InviteHandler(this);
        groupChatUtils = new GroupChatUtils(this);

        subCommands.add(new CreateGroup(this));
        subCommands.add(new DisbandGroup(this));
        subCommands.add(new InviteMember(this));
        subCommands.add(new InviteAccept(this));
        subCommands.add(new InviteDecline(this));
        subCommands.add(new PromoteMember(this));
        subCommands.add(new DemoteMember(this));
        subCommands.add(new LeaveGroup(this));
        subCommands.add(new KickMember(this));
        subCommands.add(new ToggleGroupChat(this));
        subCommands.add(new InfoCommand(this));

        new CommandBase(this); // The base /group command
        new GroupChatCommand(this); // The /g command

        new ChatListener(this);

        getLogger().info("GroupChat v" + VERSION + " has loaded.");
    }

    public void onDisable() {
        getLogger().info("GroupChat v" + VERSION + " has unloaded.");
    }

    public GroupChatUtils getUtils() {
        return groupChatUtils;
    }

    public InviteHandler inviteHandler() {
        return inviteHandler;
    }

    public YamlConfiguration getYml() {
        return groupYml;
    }

    public ConfigHandler config() { return config; }

    public List<Group> getGroups() {
        return groups;
    }

    public List<GroupSubCommand> getCommands() {
        return subCommands;
    }

    /**
     * Save the YML and re-create group list
     */
    public void save() {
        try {
            groupYml.save(groupFile);
            groupYml = YamlConfiguration.loadConfiguration(groupFile);
            rebuildGroupList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Re-create group data from yml
     * This is called when saving (aka upon any group data change)
     */
    private void rebuildGroupList() {
        groups.clear();

        for (String key : groupYml.getKeys(false)) {
            ConfigurationSection theYml = groupYml.getConfigurationSection(key);

            Group aGroup = new Group(Objects.requireNonNull(theYml));
            groups.add(aGroup);
        }
    }
}
