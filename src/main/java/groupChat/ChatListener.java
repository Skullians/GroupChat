package groupChat;

import groupChat.object.Group;
import groupChat.util.GroupChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    private final GroupChatUtils utils;

    public ChatListener(GroupChat plugin) {
        utils = plugin.getUtils();

        // Register this event with Bukkit
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /*
    This handles the case where a player has toggled default chat to group chat
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        Group group = utils.getPlayerGroup(p.getUniqueId());

        if (utils.playerInGroup(p.getUniqueId()) && utils.groupChatEnabled(p)) {
            e.setCancelled(true); // Cancel the public chat message

            group.broadcast(p.getName() + ": " + e.getMessage());
        }
    }

}
