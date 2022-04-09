package groupChat.config;

import groupChat.util.Utils;

/**
 * todo: Set up this class and create proper config.yml
 */
public class ConfigHandler {
    public static int COST = 10000;
    public static String COST_STRING = Utils.formatNumber(COST);

    // todo: put this in config as list
    public final String[] disallowedInName
            = {".", ",", "'", "\"", "!", ")", ";", "(", "[", "]", "/", "?", "'", "}", "{", "^", "*"};
}
