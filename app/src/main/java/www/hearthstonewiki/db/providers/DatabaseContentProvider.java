package www.hearthstonewiki.db.providers;

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

import www.hearthstonewiki.db.DatabaseHelper;
import www.hearthstonewiki.db.tables.CardDataTable;

public class DatabaseContentProvider extends ContentProvider {

    private HashMap<String, String> mProjectionMap;
    private UriMatcher mUriMatcher;
    private DatabaseHelper mOpenHelper;

    private static final int MAIN = 1;
    private static final int MAIN_ID = 2;
    private static final int MAIN_FILTER = 3;
    private static final int MAIN_SEARCH = 4;

    public DatabaseContentProvider() {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(DatabaseHelper.AUTHORITY, CardDataTable.TABLE_NAME + "/all/", MAIN);
        mUriMatcher.addURI(DatabaseHelper.AUTHORITY, CardDataTable.TABLE_NAME + "/item/*", MAIN_ID);
        mUriMatcher.addURI(DatabaseHelper.AUTHORITY, CardDataTable.TABLE_NAME + "/filter/*", MAIN_FILTER);
        mUriMatcher.addURI(DatabaseHelper.AUTHORITY, CardDataTable.TABLE_NAME + "/filter/*/*", MAIN_SEARCH); // filter/class/name

        mProjectionMap = new HashMap<String, String>();
        mProjectionMap.put(CardDataTable._ID, CardDataTable._ID);
        mProjectionMap.put(CardDataTable.COLUMN_NAME, CardDataTable.COLUMN_NAME);
        mProjectionMap.put(CardDataTable.COLUMN_TEXT, CardDataTable.COLUMN_TEXT);
        mProjectionMap.put(CardDataTable.COLUMN_COST, CardDataTable.COLUMN_COST);
        mProjectionMap.put(CardDataTable.COLUMN_CLASS, CardDataTable.COLUMN_CLASS);
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
            Uri noteUri = ContentUris.withAppendedId(CardDataTable.CARD_URI_ID, rowId);
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
            case MAIN_FILTER:
                SQLiteDatabase db = mOpenHelper.getReadableDatabase();
                selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs,
                        new String []{uri.getLastPathSegment()});
                String proj = buildProjection(projection);
                String query = "SELECT " + proj + " FROM " + CardDataTable.TABLE_NAME +
                        " WHERE " + CardDataTable.COLUMN_CLASS +
                        "=? " + " ORDER BY " + CardDataTable.COLUMN_COST + " ASC";
                return db.rawQuery(query, selectionArgs);
            case MAIN_SEARCH:
                db = mOpenHelper.getReadableDatabase();
                selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs,
                        new String []{uri.getPathSegments().get(uri.getPathSegments().size() - 2)});
                proj = buildProjection(projection);
                String searchedName = "'%" + uri.getLastPathSegment() + "%'";
                query = "SELECT " + proj + " FROM " + CardDataTable.TABLE_NAME +
                        " WHERE " + CardDataTable.COLUMN_CLASS +
                        "=? " + " AND (" + CardDataTable.COLUMN_NAME + " LIKE " +
                        searchedName + " OR " + CardDataTable.COLUMN_TEXT + " LIKE " +
                        searchedName + ") ORDER BY " + CardDataTable.COLUMN_COST + " ASC";
                Log.e("tag", query);
                Log.e("tag", selectionArgs[0]);
                return db.rawQuery(query, selectionArgs);
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with " + mUriMatcher.match(uri));
        }

        if (TextUtils.isEmpty(sortOrder)) {
            sortOrder = CardDataTable.DEFAULT_SORT_ORDER;
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        Log.d("q", qb.buildQuery(projection, selection, null, null, sortOrder, null));

        Cursor c = qb.query(db, projection, selection, selectionArgs,
                null /* no group */, null /* no filter */, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    private String buildProjection(String[] projection) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < projection.length - 1; i++) {
            sb = sb.append(projection[i]);
            sb.append(", ");
        }
        if (projection.length > 1)
            sb.append(projection[projection.length - 1]);
        return sb.toString();
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
