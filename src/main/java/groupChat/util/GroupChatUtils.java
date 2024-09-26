package groupChat.util;

import groupChat.object.Group;
import groupChat.GroupChat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import groupChat.config.ConfigHandler;
import groupChat.config.Lang;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GroupChatUtils {
    private final GroupChat plugin;

    public GroupChatUtils(GroupChat plugin) {
        this.plugin = plugin;
    }

    public void fail(Player player, String why) {
        player.sendMessage(Chat.RED + "Error: " + Chat.WHITE + why);
    }

    public void message(Player player, String msg) {
        player.sendMessage(Lang.GROUP_TAG + msg);
    }

    public boolean isValidName(String theName) {
        if (theName.length() < 4 || theName.length() > 16) return false;
        return !Utils.chatInArray(theName, plugin.config().disallowedInName);
    }

    public List<String> getGroupNames() {
        List<String> results = new ArrayList<>();
        plugin.getGroups().forEach(group -> results.add(group.name()));
        return results;
    }

    public Group getGroupByName(String name) {
        Group theGroup = null;
        for (Group group : plugin.getGroups()) {
            if (group.name().equalsIgnoreCase(name)) {
                theGroup = group;
            }
        }
        return theGroup;
    }

    public Group getPlayerGroup(UUID uuid) {
        Group theGroup = null;
        for (Group group : plugin.getGroups()) {
            if (group.getMembers().contains(uuid)) {
                theGroup = group;
            }
        }
        return theGroup;
    }

    public boolean playerInGroup(UUID uuid) {
        Group theGroup = null;
        for (Group group : plugin.getGroups()) {
            if (group.getMembers().contains(uuid)) {
                theGroup = group;
            }
        }
        return theGroup != null;
    }

    public boolean groupChatEnabled(Player p) {
        return plugin.getConfig().getBoolean("groupChat." + p.getUniqueId());
    }

    // todo: do this via OOP
    public void displayMenu(CommandSender p) {
        String bar = Chat.GRAY + Chat.bar(30) + Chat.RESET;
        String arrow = Chat.DARK_GRAY + "> " + Chat.AQUA;
        String desc = Chat.WHITE + "- ";
        String command = Chat.WHITE + "";
        String arg = Chat.GRAY + "";
        p.sendMessage(bar + Chat.AQUA + " [Party Help] " + bar);
        p.sendMessage(arrow + "/party " + command + "create" + arg + " <name> " + desc + "Create a party for your friends.");
        p.sendMessage(arrow + "/party " + command + "leave " + arg + "<player> " + desc + "Leave your current party");
        p.sendMessage(arrow + "/party " + command + "invite " + arg + "<player> " + desc
                + "Invite a player to join your party");
        p.sendMessage(
                arrow + "/party " + command + "kick " + arg + "<player> " + desc + "Remove a player from your party");
        p.sendMessage(arrow + "/group " + command + "disband " + arg + "<name> " + desc + "Disband your party");
        p.sendMessage(
                arrow + "/party " + command + "promote " + arg + "<player> " + desc + "Promote a member to captain");
        p.sendMessage(
                arrow + "/party " + command + "togglechat " + desc + "Toggle default chat between party and public");
        p.sendMessage(arrow + "/p " + command + arg + "<message> " + desc + "Send a message to your party");
    }

}
