package www.hearthstonewiki.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

import www.hearthstonewiki.db.tables.CardDataTable;

public class DatabaseContentProvider extends ContentProvider {

    private SQLiteDatabase database;
    private HashMap<String, String> mProjectionMap;
    private UriMatcher mUriMatcher;
    private DatabaseHelper mOpenHelper;

    private static final int MAIN = 1;
    private static final int MAIN_ID = 2;

    public DatabaseContentProvider() {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(DatabaseHelper.AUTHORITY, CardDataTable.TABLE_NAME + "/all", MAIN);
        mUriMatcher.addURI(DatabaseHelper.AUTHORITY, CardDataTable.TABLE_NAME + "/item", MAIN_ID);


        mProjectionMap = new HashMap<String, String>();
        mProjectionMap.put(CardDataTable._ID, CardDataTable._ID);
        mProjectionMap.put(CardDataTable.COLUMN_NAME, CardDataTable.COLUMN_NAME);
        mProjectionMap.put(CardDataTable.COLUMN_TEXT, CardDataTable.COLUMN_TEXT);
        mProjectionMap.put(CardDataTable.COLUMN_ID, CardDataTable.COLUMN_ID);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String finalWhere;

        int count;

        switch (mUriMatcher.match(uri)) {
            case MAIN:
                count = db.delete(CardDataTable.TABLE_NAME, selection, selectionArgs);
                break;

            case MAIN_ID:

                finalWhere = DatabaseUtils.concatenateWhere(
                        CardDataTable._ID + " = " + ContentUris.parseId(uri), selection);
                count = db.delete(CardDataTable.TABLE_NAME, finalWhere, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri  + " with " + mUriMatcher.match(uri));
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case MAIN:
                return CardDataTable.CONTENT_TYPE;
            case MAIN_ID:
                return CardDataTable.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri  + " with " + mUriMatcher.match(uri));
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if (mUriMatcher.match(uri) != MAIN) {
            throw new IllegalArgumentException("Unknown URI " + uri  + " with " + mUriMatcher.match(uri));
        }

        ContentValues values;

        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        if (!values.containsKey(CardDataTable.COLUMN_NAME)) {
            values.put(CardDataTable.COLUMN_NAME, "");
        }
        if (!values.containsKey(CardDataTable.COLUMN_TEXT)) {
            values.put(CardDataTable.COLUMN_TEXT, "");
        }

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        long rowId = db.insert(CardDataTable.TABLE_NAME, null, values);

        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(CardDataTable.CONTENT_ID_URI_BASE, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        return null;
    }

    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(CardDataTable.TABLE_NAME);

        System.out.print(mUriMatcher.match(uri));
        switch (mUriMatcher.match(uri)) {
            case MAIN:
                qb.setProjectionMap(mProjectionMap);
                break;
            case MAIN_ID:
                qb.setProjectionMap(mProjectionMap);
                qb.appendWhere(CardDataTable._ID + "=?");
                selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs,
                        new String[]{uri.getLastPathSegment()});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with " + mUriMatcher.match(uri));
        }

        if (TextUtils.isEmpty(sortOrder)) {
            sortOrder = CardDataTable.DEFAULT_SORT_ORDER;
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        Log.d("q", qb.buildQuery(projection, selection, null, null, sortOrder, "1"));

        Cursor c = qb.query(db, projection, selection, selectionArgs,
                null /* no group */, null /* no filter */, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        String finalWhere;

        switch (mUriMatcher.match(uri)) {
            case MAIN:
                count = db.update(CardDataTable.TABLE_NAME, values, selection, selectionArgs);
                break;

            case MAIN_ID:
                finalWhere = DatabaseUtils.concatenateWhere(
                        CardDataTable._ID + " = " + ContentUris.parseId(uri), selection);
                count = db.update(CardDataTable.TABLE_NAME, values, finalWhere, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with " + mUriMatcher.match(uri));
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}
