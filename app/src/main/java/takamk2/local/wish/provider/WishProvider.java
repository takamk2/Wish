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

    private static final UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, WishDBStore.Wishes.TABLE_NAME, CODE_WISHES);
        sUriMatcher.addURI(AUTHORITY, WishDBStore.Wishes.TABLE_NAME + "/#", CODE_WISH_ITEM);
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
            default:
                throw new IllegalArgumentException("uri does not matches");
        }

        SQLiteDatabase db = mHelper.getWritableDatabase();
        long newId = db.insert(table, null, values);
        Uri newUri = ContentUris.withAppendedId(uri, newId);
        getContext().getContentResolver().notifyChange(uri, null);

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
            default:
                throw new IllegalArgumentException("uri does not matches");
        }

        SQLiteDatabase db = mHelper.getWritableDatabase();
        int count = db.update(table, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

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
            default:
                throw new IllegalArgumentException("uri does not matches");
        }

        SQLiteDatabase db = mHelper.getWritableDatabase();
        int count = db.delete(table, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
