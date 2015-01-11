package www.hearthstonewiki.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import www.hearthstonewiki.db.tables.*;

/**
 * Created by uzzz on 10.11.14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String AUTHORITY = "www.hearthstone-wiki";

    private static final String DATABASE_NAME = "hearthstone.db";
    private static final int DATABASE_VERSION = 11;

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CardDataTable.TABLE_NAME + " ("
                + CardDataTable._ID + " INTEGER PRIMARY KEY,"
                + CardDataTable.COLUMN_NAME + " TEXT,"
                + CardDataTable.COLUMN_TEXT + " TEXT,"
                + CardDataTable.COLUMN_ID + " TEXT"
                + ");");
        Log.d("db", "here");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CardDataTable.TABLE_NAME);
        onCreate(db);
    }
}
