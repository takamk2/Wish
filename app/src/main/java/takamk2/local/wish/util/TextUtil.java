package takamk2.local.wish.util;

import java.text.NumberFormat;

/**
 * Created by takamk2 on 16/12/14.
 * <p>
 * The Edit Fragment of Base Class.
 */

public class TextUtil {

    public static String convertCurrency(Long num) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        return numberFormat.format(num);
    }
}
