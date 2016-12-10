package takamk2.local.wish.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by takamk2 on 16/12/10.
 * <p>
 * The Edit Fragment of Base Class.
 */

public class WishDBStore {


    private WishDBStore() {
    }

    public abstract static class BaseColumns2 implements BaseColumns {

        public static final String AUTHORITY = "takamk2.local.wish.wishprovider";
    }

    public abstract static class Wishes extends BaseColumns2 {

        public static final String TABLE_NAME = "wishes";

        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PRICE = "price";

        public static final String CREATE_TABLE =
                "create table " + TABLE_NAME + " ( " +
                COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_NAME + " text not null, " +
                COLUMN_PRICE + " integer not null " +
                ");";
        public static final String DROP_TABLE =
                "drop table if exists " + TABLE_NAME;
    }
}
