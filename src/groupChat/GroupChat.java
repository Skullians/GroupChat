package groupChat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import groupChat.commands.ConfirmCommand;
import groupChat.commands.CreateGroup;
import groupChat.commands.DeleteGroup;
import groupChat.commands.DemoteMember;
import groupChat.commands.HelpCommand;
import groupChat.commands.InfoCommand;
import groupChat.commands.InviteAccept;
import groupChat.commands.InviteDecline;
import groupChat.commands.InviteMember;
import groupChat.commands.KickMember;
import groupChat.commands.LeaveGroup;
import groupChat.commands.PromoteMember;
import groupChat.commands.TalkInGroup;
import groupChat.commands.ToggleGroupChat;

public class GroupChat extends JavaPlugin implements Listener {
	private Logger logger = Bukkit.getLogger();
	private File dataFolder;
	public static GroupChat plugin;
	private static File groupFile;
	private static YamlConfiguration groupYml;
	public HashMap<String, Double> pointsMap = new HashMap<String, Double>();
	public String groupColor = ChatColor.DARK_AQUA + "";
	public String groupTag = groupColor + "<Group>";
	public String trustedRank = "captain";
	public String version = "1.0.0";
	public Map<UUID, UUID> inviteMap = new HashMap<UUID, UUID>();
	public List<SubCommand> commands = new ArrayList<>();

	public void onEnable() {
		logger.info("Enabling GroupChat version " + version + "...");
		dataFolder = getDataFolder();
		groupFile = new File(dataFolder, "groups.yml");
		groupYml = YamlConfiguration.loadConfiguration(groupFile);
		plugin = this;

		getCommand("g").setExecutor(new TalkInGroup(this));
		getCommand("confirm").setExecutor(new ConfirmCommand(this));
		getServer().getPluginManager().registerEvents(this, this);

		commands.add(new HelpCommand(this));
		commands.add(new CreateGroup(this));
		commands.add(new DeleteGroup(this));
		commands.add(new InfoCommand(this));
		commands.add(new InviteMember(this));
		commands.add(new InviteAccept(this));
		commands.add(new InviteDecline(this));
		commands.add(new PromoteMember(this));
		commands.add(new DemoteMember(this));
		commands.add(new LeaveGroup(this));
		commands.add(new KickMember(this));
		commands.add(new ToggleGroupChat(this));
	}

	public void onDisable() {
		logger.info("Disabling GroupChat version " + version + "...");
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		if (playerInGroup(player) && talkingInGroup(player)) {
			e.setCancelled(true);
			UUID playerUUID = player.getUniqueId();
			broadcast(getPlayerGroup(playerUUID.toString()),
					ChatColor.RESET + player.getName() + ": " + e.getMessage());
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		String version = "1.0.0";
		Player p = (Player) sender;

		if (args.length == 0) {
			if (playerInGroup(p)) {
				groupMenu(p);
			} else {
				commands.get(0).execute(sender, args); // Execute help command
			}
			return true;
		}

		if (sender instanceof ConsoleCommandSender) {
			fail(sender, "That command only works as a player.");
			return true;
		}

		String[] arguments = Arrays.copyOfRange(args, 1, args.length);

		for (SubCommand subCommand : commands) {
			if (subCommand.getName().equals(args[0])) {
				subCommand.execute(sender, args);
				break;
			}
		}
		return true;
	}

	public String getPlayerGroup(String uuid) {
		for (String i : groupYml.getKeys(false)) {
			ConfigurationSection section = groupYml.getConfigurationSection(i);
			if (section.getStringList(".members").contains(uuid))
				return i;
		}
		return "none";
	}

	public boolean playerIsGroupStaff(Player p, String groupName) {
		return playerIsLeader(p, groupName) || playerIsCaptain(p.getUniqueId().toString(), groupName);
	}

	public boolean playerIsCaptain(String uuid, String groupName) {
		return groupYml.getStringList(groupName + ".captains").contains(uuid);
	}

	public boolean playerIsGroupLeader(Player p) {
		if (playerInGroup(p)) {
			String groupName = getPlayerGroup(p.getUniqueId().toString());
			if (playerIsLeader(p, groupName)) {
				return true;
			}
		}
		return false;
	}

	public boolean playerIsLeader(Player p, String groupName) {
		if (playerInGroup(p)) {
			groupName = getPlayerGroup(p.getUniqueId().toString());
			if (groupYml.getString(groupName + ".leader").equalsIgnoreCase(p.getUniqueId().toString())) {
				return true;
			}
		}
		return false;
	}

	public boolean playerInGroup(Player p) {
		String uuid = p.getUniqueId().toString();
		for (String key : groupYml.getKeys(false)) {
			ConfigurationSection g = groupYml.getConfigurationSection(key);
			ConfigurationSection mem = g.getConfigurationSection("members");
			if (g.getStringList(".members").contains(uuid)) {
				return true;
			}
		}
		return false;
	}

	public List<Player> getOnlineMembers(String groupName) {
		List<String> idList = groupYml.getStringList(groupName + ".members");
		List<Player> members = new ArrayList<Player>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (idList.contains(p.getUniqueId().toString())) {
				members.add(p);
			}
		}
		return members;
	}

	public String getMembers(String groupName) {
		StringBuilder list = new StringBuilder();
		for (String id : groupYml.getStringList(groupName + ".members")) {
			OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(id));
			if (list.length() > 0) {
				list.append(", ");
			}
			list.append(p.getName());
		}
		return list.toString();
	}

	public String getCaptains(String groupName) {
		StringBuilder list = new StringBuilder();
		for (String id : groupYml.getStringList(groupName + ".captains")) {
			OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(id));
			if (list.length() > 0) {
				list.append(", ");
			}
			list.append(p.getName());
		}
		return list.toString();
	}

	public Integer size(String groupName) {
		return groupYml.getStringList(groupName + ".members").size();
	}

	public String getLeader(String groupName) {
		return Bukkit.getOfflinePlayer(UUID.fromString(groupYml.getString(groupName + ".leader"))).getName();
	}

	public String getCreationDate(String groupName) {
		return groupYml.getString(groupName + ".created");
	}

	public boolean talkingInGroup(Player p) {
		String path = "groupChat." + p.getUniqueId().toString();
		return getConfig().getBoolean(path);
	}

	public void groupMenu(Player p) {
		String uuid = p.getUniqueId().toString();
		String groupName = getPlayerGroup(uuid);
		String arrow = ChatColor.DARK_GRAY + "> " + groupColor;
		String bar = ChatColor.GRAY + Utils.createBar(15);
		p.sendMessage(bar + ChatColor.WHITE + " " + groupName + " " + bar);
		p.sendMessage(arrow + "Leader: " + ChatColor.GRAY + getLeader(groupName));
		if (!getCaptains(groupName).isEmpty())
			p.sendMessage(
					arrow + StringUtils.capitalize(trustedRank) + "s: " + ChatColor.GRAY + getCaptains(groupName));
		p.sendMessage(arrow + "Created on: " + ChatColor.GRAY + getCreationDate(groupName));
		p.sendMessage(arrow + "Members: (" + size(groupName) + ") " + ChatColor.WHITE + getMembers(groupName));
		if (playerInGroup(p))
			p.sendMessage(arrow + ChatColor.RESET + "For a list of commands, type /group help.");
	}

	List<String> myList = Arrays.asList("create", "leave", "invite", "kick", "promote", "togglechat", "info", "help",
			"demote");
	List<String> arguments = new ArrayList<>(myList);

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
		List<String> result = new ArrayList<>();

		switch (args.length) {
		case 1:
			for (String a : arguments) {
				if (a.toLowerCase().startsWith(args[0].toLowerCase()))
					result.add(a);
			}
			return result;
		case 2:
			if (args[0].equalsIgnoreCase("invite"))
				return null;
			if (args[0].equalsIgnoreCase("kick"))
				return null;
			if (args[0].equalsIgnoreCase("promote"))
				return null;
			return result; // Return the list
		}
		return Arrays.asList(); // Return an empty array so it stops tab completing
	}

	public YamlConfiguration getYml() {
		return groupYml;
	}

	public void broadcast(String groupName, String msg) {
		List<Player> onlineMembers = getOnlineMembers(groupName);
		for (Player p : onlineMembers)
			p.sendMessage(groupTag + " " + msg);
	}

	public void tellAdmins(CommandSender sender, String groupName, String msg) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (Utils.isAdmin(p) && (sender != p)) {
				if (!getPlayerGroup(p.getUniqueId().toString()).equals(groupName))
					p.sendMessage(ChatColor.RED + "[!]" + " " + ChatColor.RESET + msg);
			}
		}
	}

	public void fail(CommandSender sender, String why) {
		sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + why);
	}

	public void message(CommandSender sender, String msg) {
		sender.sendMessage(groupColor + "> " + ChatColor.WHITE + msg);
	}

	public void save() {
		try {
			groupYml.save(groupFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
