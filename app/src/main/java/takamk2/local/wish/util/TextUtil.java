package takamk2.local.wish.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by takamk2 on 16/12/14.
 * <p>
 * The Edit Fragment of Base Class.
 */

public class TextUtil {

    public static String convertNumToCurrency(Long num) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        return numberFormat.format(num);
    }

    public static String convertNumToCammaSeparated(Long num) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(num);
    }

    public static Long extractNumber(String str) {
        String s = str.replaceAll("[^-^0-9]", "");
        return (s != null) ? Long.parseLong(s) : 0L;
    }
}
