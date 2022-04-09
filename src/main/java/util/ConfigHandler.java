package util;

public class ConfigHandler {
    public static int COST = 10000;
    public static String COST_STRING = Utils.formatNumber(COST);

    // todo: put this in config as list
    private final String[] disallowedInName
            = {".", ",", "'", "\"", "!", ")", ";", "(", "[", "]", "/", "?", "'", "}", "{", "^", "*"};
}
