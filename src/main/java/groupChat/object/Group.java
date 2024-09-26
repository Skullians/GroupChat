package groupChat.object;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import groupChat.util.Chat;
import groupChat.config.Lang;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Represents a group. Groups are saved to the groups.yml; Any change to
 * a group in this class will result in a save of the yml
 */
public class Group {
    private final List<UUID> captainUUIDS = new ArrayList<>();
    private final List<UUID> memberUUIDS = new ArrayList<>();

    private final String createdOn;
    private String name;
    private UUID leaderUUID;

    public Group(ConfigurationSection yml) {
        name = yml.getName();
        createdOn = yml.getString(".created");
        leaderUUID = UUID.fromString(yml.getString(".leader"));

        yml.getStringList(".members").forEach(string -> memberUUIDS.add(UUID.fromString(string)));
        yml.getStringList(".captains").forEach(string -> captainUUIDS.add(UUID.fromString(string)));
    }

    /**
     * Get the name of the group
     *
     * @return name of the group
     */
    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * get total amount of members, including leader and captains
     *
     * @return size of the group
     */
    public int size() {
        return memberUUIDS.size();
    }

    /**
     * Get the date the group was created on
     *
     * @return mm/dd/yyyy string
     */
    public String getCreatedOn() {
        return createdOn;
    }

    /**
     * Gets all captain UUIDs, not including the leader
     *
     * @return List of UUIDs
     */
    public List<UUID> getCaptains() {
        return captainUUIDS;
    }

    /**
     * Gets all member UUIDs, including the leader and captains
     *
     * @return List of UUIDs
     */
    public List<UUID> getMembers() {
        return memberUUIDS;
    }

    /**
     * Broadcast a message to all online group members
     *
     * @param message: The message to broadcast to all online group members
     */
    public void broadcast(String message) {
        getOnlineMembers().forEach(m -> m.sendMessage(Lang.GROUP_TAG + message));
    }

    /**
     * Obtain a list of the group member's usernames; This INCLUDES captains and the leader
     *
     * @return A String list of usernames
     */
    public List<String> getMemberUsernames() {
        var usernames = new ArrayList<String>();

        memberUUIDS.forEach(uuid -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            usernames.add(player.getName());
        });

        return usernames;
    }

    /**
     * Obtain a list of the group member's usernames; This does NOT include captains or the leader
     *
     * @return usernames of non-captain, leader members
     */
    public List<String> getLowerClassMembersUsernames() {
        var usernames = new ArrayList<String>();

        memberUUIDS.stream().filter(m -> !isGroupStaff(m)).forEach(uuid -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            usernames.add(player.getName());
        });

        return usernames;
    }

    public List<String> getCaptainUsernames() {
        var usernames = new ArrayList<String>();

        captainUUIDS.forEach(uuid -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            usernames.add(player.getName());
        });

        return usernames;
    }

    public String captainsAsString() {
        return getUsernames(captainUUIDS);
    }

    public String getMembersAsString() {
        return getUsernames(memberUUIDS);
    }

    /*
    Internal method for the above two methods
     */
    private String getUsernames(List<UUID> memberUUIDS) {
        StringBuilder list = new StringBuilder();

        for (UUID id : memberUUIDS) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(id);
            if (list.length() > 0) {
                list.append(", ");
            }
            list.append(p.getName());
        }
        return list.toString();
    }

    public List<Player> getOnlineMembers() {
        return Bukkit.getOnlinePlayers().stream()
                .filter(p -> memberUUIDS.contains(p.getUniqueId())).collect(Collectors.toList());
    }

    /**
     * Is this player a group captain or group leader?
     *
     * @param uuid: The UUID of the player to check
     * @return boolean value
     */
    public boolean isGroupStaff(UUID uuid) {
        return leaderUUID.equals(uuid) || captainUUIDS.contains(uuid);
    }

    public boolean isLeader(UUID uuid) {
        return leaderUUID.equals(uuid);
    }

    public UUID getLeader() {
        return leaderUUID;
    }

    public void setLeaderUUID(UUID uuid) {
        leaderUUID = uuid;
    }

    public void displayGroupInfo(CommandSender s) {
        String bar = Chat.GRAY + Chat.bar(17);

        String a;
        a = Chat.DARK_GRAY + "> " + Chat.AQUA;

        s.sendMessage(bar + Chat.WHITE + " " + name + " " + bar);
        s.sendMessage(a + "Leader: " + Chat.GRAY + Bukkit.getOfflinePlayer(leaderUUID).getName());

        if (!captainUUIDS.isEmpty()) {
            s.sendMessage(a + StringUtils.capitalize(Lang.TRUSTED_RANK) + "s: " + Chat.GRAY + captainsAsString());
        }

        s.sendMessage(a + "Created on: " + Chat.GRAY + createdOn);
        s.sendMessage(a + "Members: (" + memberUUIDS.size() + ") " + Chat.WHITE + getMembersAsString());
    }

}
