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

    public abstract static class Savings extends BaseColumns2 {

        public static final String TABLE_NAME = "savings";

        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_CREATED = "created";

        public static final String CREATE_TABLE =
                "create table " + TABLE_NAME + " ( " +
                COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_PRICE + " integer not null, " +
                COLUMN_CREATED + " integer not null " +
                ");";
        public static final String DROP_TABLE =
                "drop table if exists " + TABLE_NAME;
    }

    public abstract static class Tasks extends BaseColumns2 {

        public static final String TABLE_NAME = "tasks";

        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_CREATED = "created";

        public static final String CREATE_TABLE =
                "create table " + TABLE_NAME + " ( " +
                        COLUMN_ID + " integer primary key autoincrement, " +
                        COLUMN_TITLE + " text not null, " +
                        COLUMN_PRICE + " integer not null, " +
                        COLUMN_CREATED + " integer not null " +
                        ");";
        public static final String DROP_TABLE =
                "drop table if exists " + TABLE_NAME;
    }

    public abstract static class Histories extends BaseColumns2 {

        public static final String TABLE_NAME = "histories";

        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_TOTAL_SAVINGS = "total_savings";
        public static final String COLUMN_TOTAL_WISH_PRICE = "total_wish_price";
        public static final String COLUMN_TOTAL_TASK_PRICE = "total_task_price";
        public static final String COLUMN_CREATED = "created";

        public static final String CREATE_TABLE =
                "create table " + TABLE_NAME + " ( " +
                        COLUMN_ID + " integer primary key autoincrement, " +
                        COLUMN_TOTAL_SAVINGS + " integer not null, " +
                        COLUMN_TOTAL_WISH_PRICE + " integer not null, " +
                        COLUMN_TOTAL_TASK_PRICE + " integer not null, " +
                        COLUMN_CREATED + " integer not null " +
                        ");";
        public static final String DROP_TABLE =
                "drop table if exists " + TABLE_NAME;
    }

    public abstract static class Daily extends BaseColumns2 {

        public static final String TABLE_NAME = "daily";

        public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_TASK_ID = "task_id";
        public static final String COLUMN_COMPLETED = "completed";
        public static final String COLUMN_CREATED = "created";

        public static final String CREATE_TABLE =
                "create table " + TABLE_NAME + " ( " +
                        COLUMN_ID + " integer primary key autoincrement, " +
                        COLUMN_TASK_ID + " integer not null, " +
                        COLUMN_COMPLETED + " integer not null, " +
                        COLUMN_CREATED + " integer not null " +
                        ");";
        public static final String DROP_TABLE =
                "drop table if exists " + TABLE_NAME;
    }
}
