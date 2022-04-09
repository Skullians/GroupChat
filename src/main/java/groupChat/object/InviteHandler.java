package groupChat.object;

import groupChat.GroupChat;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Handles invites
 * <p>
 * Invites expire after ten minute by default; todo: Add this to configuration
 */
public class InviteHandler {
    private final Map<UUID, Set<Invite>> map = new HashMap<>();
    private final GroupChat plugin;

    public InviteHandler(GroupChat plugin) {
        this.plugin = plugin;
    }

    /**
     * Add an invite to the invite pool; Default expiration is 10 minutes
     * @param invite: The invite
     */
    public void addInviteToMap(Invite invite) {
        map.putIfAbsent(invite.inviteSenderID(), new HashSet<>());

        Set<Invite> inviteList = map.get(invite.inviteSenderID());

        inviteList.add(invite);
        map.put(invite.inviteSenderID(), inviteList);

        final int ONE_MINUTE_TICKS = 60 * 20;

        // clear out after 10 minutes
        Bukkit.getScheduler().runTaskLater(plugin, () ->
                inviteList.remove(invite), ONE_MINUTE_TICKS * 10L);
    }

    public void removeInviteFromMap(Invite invite) {
        map.forEach((k, set) -> set.removeIf(invite::equals));
    }

    /**
     * Obtain an invite given the invited player UUID
     *
     * @param invitedUUID: UUID of the player who was (or wasn't) invited
     * @return either the Invite object, or null
     */
    @Nullable
    public Invite getInvite(UUID invitedUUID) {
        for (Map.Entry<UUID, Set<Invite>> entry : map.entrySet()) {
            for (Invite inv : entry.getValue()) {
                if (inv.invitedUUID().equals(invitedUUID)) {
                    return inv;
                }
            }
        }
        return null;
    }

}
