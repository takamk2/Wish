package takamk2.local.wish.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

import takamk2.local.wish.db.WishDBHelper;
import takamk2.local.wish.db.WishDBStore;

import static takamk2.local.wish.db.WishDBStore.BaseColumns2.AUTHORITY;

public class WishProvider extends ContentProvider {

    private static final int CODE_WISHES = 0;
    private static final int CODE_WISH_ITEM = 1;
    private static final int CODE_TASKS = 2;
    private static final int CODE_TASK_ITEM = 3;
    private static final int CODE_SAVINGS = 4;
    private static final int CODE_SAVINGS_ITEM = 5;
    private static final int CODE_HISTORIES = 6;
    private static final int CODE_HISTORY_ITEM = 7;

    private static final UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, WishDBStore.Wishes.TABLE_NAME, CODE_WISHES);
        sUriMatcher.addURI(AUTHORITY, WishDBStore.Wishes.TABLE_NAME + "/#", CODE_WISH_ITEM);
        sUriMatcher.addURI(AUTHORITY, WishDBStore.Tasks.TABLE_NAME, CODE_TASKS);
        sUriMatcher.addURI(AUTHORITY, WishDBStore.Tasks.TABLE_NAME + "/#", CODE_TASK_ITEM);
        sUriMatcher.addURI(AUTHORITY, WishDBStore.Savings.TABLE_NAME, CODE_SAVINGS);
        sUriMatcher.addURI(AUTHORITY, WishDBStore.Savings.TABLE_NAME + "/#", CODE_SAVINGS_ITEM);
        sUriMatcher.addURI(AUTHORITY, WishDBStore.Histories.TABLE_NAME, CODE_HISTORIES);
        sUriMatcher.addURI(AUTHORITY, WishDBStore.Histories.TABLE_NAME + "/#", CODE_HISTORY_ITEM);
    }

    private SQLiteOpenHelper mHelper;

    public WishProvider() {
    }

    @Override
    public boolean onCreate() {
        mHelper = new WishDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        String table;
        switch (sUriMatcher.match(uri)) {
            case CODE_WISHES:
                table = WishDBStore.Wishes.TABLE_NAME;
                break;
            case CODE_WISH_ITEM:
                table = WishDBStore.Wishes.TABLE_NAME;
                selection = BaseColumns._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            case CODE_TASKS:
                table = WishDBStore.Tasks.TABLE_NAME;
                break;
            case CODE_TASK_ITEM:
                table = WishDBStore.Tasks.TABLE_NAME;
                selection = BaseColumns._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            case CODE_SAVINGS:
                table = WishDBStore.Savings.TABLE_NAME;
                break;
            case CODE_SAVINGS_ITEM:
                table = WishDBStore.Savings.TABLE_NAME;
                selection = BaseColumns._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            case CODE_HISTORIES:
                table = WishDBStore.Histories.TABLE_NAME;
                break;
            case CODE_HISTORY_ITEM:
                table = WishDBStore.Histories.TABLE_NAME;
                selection = BaseColumns._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            default:
                throw new IllegalArgumentException("uri does not matches");
        }

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor =
                db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        String table;
        switch (sUriMatcher.match(uri)) {
            case CODE_WISHES:
                table = WishDBStore.Wishes.TABLE_NAME;
                break;
            case CODE_TASKS:
                table = WishDBStore.Tasks.TABLE_NAME;
                break;
            case CODE_SAVINGS:
                table = WishDBStore.Savings.TABLE_NAME;
                break;
            case CODE_HISTORIES:
                table = WishDBStore.Histories.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("uri does not matches");
        }

        SQLiteDatabase db = mHelper.getWritableDatabase();
        long newId = db.insert(table, null, values);
        Uri newUri = ContentUris.withAppendedId(uri, newId);
        getContext().getContentResolver().notifyChange(uri, null);

        updateHistories();

        return newUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        String table;
        switch (sUriMatcher.match(uri)) {
            case CODE_WISHES:
                table = WishDBStore.Wishes.TABLE_NAME;
                break;
            case CODE_WISH_ITEM:
                table = WishDBStore.Wishes.TABLE_NAME;
                selection = BaseColumns._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            case CODE_TASKS:
                table = WishDBStore.Tasks.TABLE_NAME;
                break;
            case CODE_TASK_ITEM:
                table = WishDBStore.Tasks.TABLE_NAME;
                selection = BaseColumns._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            case CODE_SAVINGS:
                table = WishDBStore.Savings.TABLE_NAME;
                break;
            case CODE_SAVINGS_ITEM:
                table = WishDBStore.Savings.TABLE_NAME;
                selection = BaseColumns._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            case CODE_HISTORIES:
                table = WishDBStore.Histories.TABLE_NAME;
                break;
            case CODE_HISTORY_ITEM:
                table = WishDBStore.Histories.TABLE_NAME;
                selection = BaseColumns._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            default:
                throw new IllegalArgumentException("uri does not matches");
        }

        SQLiteDatabase db = mHelper.getWritableDatabase();
        int count = db.update(table, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        updateHistories();

        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        String table;
        switch (sUriMatcher.match(uri)) {
            case CODE_WISHES:
                table = WishDBStore.Wishes.TABLE_NAME;
                break;
            case CODE_WISH_ITEM:
                table = WishDBStore.Wishes.TABLE_NAME;
                selection = BaseColumns._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            case CODE_TASKS:
                table = WishDBStore.Tasks.TABLE_NAME;
                break;
            case CODE_TASK_ITEM:
                table = WishDBStore.Tasks.TABLE_NAME;
                selection = BaseColumns._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            case CODE_SAVINGS:
                table = WishDBStore.Savings.TABLE_NAME;
                break;
            case CODE_SAVINGS_ITEM:
                table = WishDBStore.Savings.TABLE_NAME;
                selection = BaseColumns._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            case CODE_HISTORIES:
                table = WishDBStore.Histories.TABLE_NAME;
                break;
            case CODE_HISTORY_ITEM:
                table = WishDBStore.Histories.TABLE_NAME;
                selection = BaseColumns._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            default:
                throw new IllegalArgumentException("uri does not matches");
        }

        SQLiteDatabase db = mHelper.getWritableDatabase();
        int count = db.delete(table, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        updateHistories();

        return count;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void updateHistories() {

        SQLiteDatabase db = mHelper.getWritableDatabase();

        long total_savings = 0L;
        long total_wish_price = 0L;
        long total_task_price = 0L;

        String[] projection;
        Cursor cursor;

        projection = new String[]{"sum(" + WishDBStore.Savings.COLUMN_PRICE + ")"};
        cursor = null;
        try {
            cursor = getContext().getContentResolver().query(WishDBStore.Savings.CONTENT_URI,
                    projection, null, null, null);
            if (cursor.moveToFirst()) {
                total_savings = cursor.getLong(0);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        projection = new String[]{"sum(" + WishDBStore.Wishes.COLUMN_PRICE + ")"};
        cursor = null;
        try {
            cursor = getContext().getContentResolver().query(WishDBStore.Wishes.CONTENT_URI,
                    projection, null, null, null);
            if (cursor.moveToFirst()) {
                total_wish_price = cursor.getLong(0);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        // Todo: DEBUG
        total_task_price = 1600L;

        ContentValues values = new ContentValues();
        values.put(WishDBStore.Histories.COLUMN_TOTAL_SAVINGS, total_savings);
        values.put(WishDBStore.Histories.COLUMN_TOTAL_WISH_PRICE, total_wish_price);
        values.put(WishDBStore.Histories.COLUMN_TOTAL_TASK_PRICE, total_task_price); // Todo: TBD
        values.put(WishDBStore.Histories.COLUMN_CREATED, System.currentTimeMillis());
        db.insert(WishDBStore.Histories.TABLE_NAME, null, values);
        getContext().getContentResolver().notifyChange(WishDBStore.Histories.CONTENT_URI, null);
    }

}
