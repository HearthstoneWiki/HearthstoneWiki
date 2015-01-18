package www.hearthstonewiki.db.tables;

import android.net.Uri;
import android.provider.BaseColumns;

import www.hearthstonewiki.db.DatabaseHelper;

/**
 * Created by uzzz on 17.01.15.
 */
public class APIVersionTable implements BaseColumns {

    private APIVersionTable() {}

    public static final String TABLE_NAME = "api_version";

    public static final String COLUMN_VERSION = "version";

    public static final Uri VERSION_URI
            = Uri.parse("content://" + DatabaseHelper.AUTHORITY + TABLE_NAME + "/version/");

    public static final String DEFAULT_SORT_ORDER = COLUMN_VERSION + " ASC";

}
