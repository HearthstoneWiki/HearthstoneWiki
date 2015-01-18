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
    private static final int DATABASE_VERSION = 17;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CardDataTable.TABLE_NAME + " ("
                + CardDataTable._ID + " TEXT PRIMARY KEY,"
                + CardDataTable.COLUMN_NAME + " TEXT,"
                + CardDataTable.COLUMN_TEXT + " TEXT,"
                + CardDataTable.COLUMN_CLASS + " TEXT,"
                + CardDataTable.COLUMN_COST + " INTEGER"
                + ");"
        );
        db.execSQL("CREATE TABLE " + SetsTable.TABLE_NAME + " ("
                + SetsTable._ID + " INTEGER PRIMARY KEY,"
                + SetsTable.COLUMN_SET_NAME + " TEXT"
                + ");"
        );
        db.execSQL("CREATE TABLE " + APIVersionTable.TABLE_NAME + " ("
                + APIVersionTable._ID + " INTEGER PRIMARY KEY,"
                + APIVersionTable.COLUMN_VERSION + " TEXT"
                + ");"
        );
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CardDataTable.TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + SetsTable.TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + APIVersionTable.TABLE_NAME + ";");
        onCreate(db);
    }
}
