package util;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import java.text.SimpleDateFormat;
import java.util.Date;

public interface Chat {
    String GOLD = ChatColor.GOLD + "";
    String WHITE = ChatColor.WHITE + "";
    String YELLOW = ChatColor.YELLOW + "";
    String GRAY = ChatColor.GRAY + "";
    String RED = ChatColor.RED + "";
    String GREEN = ChatColor.GREEN + "";

    String DARK_GRAY = ChatColor.DARK_GRAY + "";
    String ITALIC_GRAY = ChatColor.GRAY + "" + ChatColor.ITALIC + "";

    String BOLD = ChatColor.BOLD + "";
    String ITALIC = ChatColor.ITALIC + "";
    String RESET = ChatColor.RESET + "";

    String BLUE = ChatColor.BLUE + "";

    String AQUA = ChatColor.DARK_AQUA + "";
    String PURPLE = ChatColor.LIGHT_PURPLE + "";
    String DARK_PURPLE = ChatColor.DARK_PURPLE + "";

    String HR = ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", 20);
    String HR2 = ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", 6);
    String menuPrefix = GRAY + " " + HR2 + RESET + GOLD;
    String menuSuffix = GRAY + HR2;
    String BAR = GRAY + Chat.HR + RESET + GOLD + BOLD;
    String ar = GRAY + "> " + RESET;
    String NOTICE = Chat.RED + "[Notice] " + Chat.RESET;

    SimpleDateFormat niceLookingDate = new SimpleDateFormat("yyyy-MM-dd");
    String format = niceLookingDate.format(new Date());

    static String bar(int size) {
        return ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", size);
    }

    static String line(int size) {
        return GRAY + ChatColor.STRIKETHROUGH + StringUtils.repeat(" ", size) + RESET;
    }
}
