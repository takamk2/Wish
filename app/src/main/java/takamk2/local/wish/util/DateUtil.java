package takamk2.local.wish.util;

import android.text.format.DateUtils;

import java.util.Calendar;

import timber.log.Timber;

/**
 * Created by takamk2 on 17/01/01.
 * <p>
 * The Edit Fragment of Base Class.
 */

public class DateUtil {

    public static Calendar getNowDate() {
        Calendar now = getNowDateTime();
        Calendar cal = (Calendar) now.clone();
        cal.clear();
        cal.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE), 0, 0, 0);
        return cal;
    }

    public static Calendar getNowDateTime() {
        return Calendar.getInstance();
    }
}
