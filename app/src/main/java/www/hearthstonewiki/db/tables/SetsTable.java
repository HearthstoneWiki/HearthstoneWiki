package www.hearthstonewiki.db.tables;

import android.net.Uri;
import android.provider.BaseColumns;

import www.hearthstonewiki.db.DatabaseHelper;

/**
 * Created by uzzz on 17.01.15.
 */
public class SetsTable implements BaseColumns {

    private SetsTable() {}

    public static final String TABLE_NAME = "card_sets";

    public static final String COLUMN_SET_NAME = "set_name";

    public static final Uri ALL_SETS_URI
            = Uri.parse("content://" + DatabaseHelper.AUTHORITY + TABLE_NAME + "/all/");
    public static final String DEFAULT_SORT_ORDER = COLUMN_SET_NAME + " ASC";

}
