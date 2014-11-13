package www.hearthstonewiki.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import www.hearthstonewiki.db.tables.*;

/**
 * Created by uzzz on 10.11.14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String AUTHORITY = "www.hearthstone-wiki";

    private static final String DATABASE_NAME = "hearthstone.db";
    private static final int DATABASE_VERSION = 1;

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CardDataTable.TABLE_NAME + " ("
                + CardDataTable._ID + " INTEGER PRIMARY KEY,"
                + CardDataTable.COLUMN_DESCRIPTION + " TEXT"
                + ");");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + CardDataTable.TABLE_NAME +" IF EXISTS;");
        onCreate(db);
    }
}
