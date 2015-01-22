package www.hearthstonewiki.db.tables;

import android.net.Uri;
import android.provider.BaseColumns;

import www.hearthstonewiki.db.DatabaseHelper;

/**
 * Created by uzzz on 21.01.15.
 */
public class HeroPowerTable implements BaseColumns {

    private HeroPowerTable() {}

    public static final String TABLE_NAME = "hero_power";

    public static final String COLUMN_NAME = "power_name";
    public static final String COLUMN_CLASS = "hero_class";

    public static final Uri POWER_URI
            = Uri.parse("content://" + DatabaseHelper.AUTHORITY + TABLE_NAME + "/class/");
    public static final String DEFAULT_SORT_ORDER = COLUMN_NAME + " ASC";
}
