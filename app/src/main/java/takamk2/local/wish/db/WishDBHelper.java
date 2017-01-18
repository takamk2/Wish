package takamk2.local.wish.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by takamk2 on 16/12/10.
 * <p>
 * The Edit Fragment of Base Class.
 */

public class WishDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "wish.db";
    private static final int DB_VERSION = 9;

    public WishDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WishDBStore.Wishes.CREATE_TABLE);
        db.execSQL(WishDBStore.Tasks.CREATE_TABLE);
        db.execSQL(WishDBStore.Savings.CREATE_TABLE);
        db.execSQL(WishDBStore.Histories.CREATE_TABLE);
        db.execSQL(WishDBStore.Daily.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(WishDBStore.Wishes.DROP_TABLE);
        db.execSQL(WishDBStore.Tasks.DROP_TABLE);
        db.execSQL(WishDBStore.Savings.DROP_TABLE);
        db.execSQL(WishDBStore.Histories.DROP_TABLE);
        db.execSQL(WishDBStore.Daily.DROP_TABLE);
        onCreate(db);
    }
}
