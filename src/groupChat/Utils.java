package groupChat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Utils {
	public static DecimalFormat numberStuff = new DecimalFormat("#,###");

	public static boolean isAdmin(Player player) {
		return player.isOp() || player.hasPermission("groupchat.admin");
	}

	public static String createBar(int size) {
		return ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", size);
	}

	public static String getCurrentDate() {
		SimpleDateFormat niceLookingDate = new SimpleDateFormat("MM/dd/yyyy");
		return niceLookingDate.format(new Date());
	}

	public static void delayMessage(Player player, String message, int time) {
		Bukkit.getScheduler().runTaskLater(GroupChat.plugin, new Runnable() {

			@Override
			public void run() {
				player.sendMessage(message);
			}

		}, time);
	}

}
