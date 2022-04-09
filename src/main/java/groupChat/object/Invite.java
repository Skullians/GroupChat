package groupChat.object;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Represents a player inviting another player to their group.
 */
public record Invite(Group group, Player inviteSender, UUID invitedUUID) {

    public UUID inviteSenderID() {
        return inviteSender.getUniqueId();
    }

    public String invitedUsername() {
        return Bukkit.getOfflinePlayer(invitedUUID).getName();
    }

    public Group getGroup() {
        return group;
    }

}
