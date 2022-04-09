package groupChat.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static DecimalFormat numberFormat = new DecimalFormat("#,###");

    public static String formatNumber(double num) {
        return numberFormat.format(num);
    }

    public static String formatNumber(int num) {
        return numberFormat.format(num);
    }

    public static boolean chatInArray(String input, String[] array) {
        for (String s : array) {
            if (input.contains(s.replace("i", "1")) || input.equalsIgnoreCase(s)
                    || (input.contains(s))) {
                return true;
            }
        }
        return false;
    }

    public static String getCurrentDateString() {
        SimpleDateFormat niceLookingDate = new SimpleDateFormat("MM/dd/yyyy");
        return niceLookingDate.format(new Date());
    }

}
