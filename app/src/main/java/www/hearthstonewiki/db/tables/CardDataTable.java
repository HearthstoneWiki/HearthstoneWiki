package www.hearthstonewiki.db.tables;

import android.net.Uri;
import android.provider.BaseColumns;

import www.hearthstonewiki.db.DatabaseHelper;

/**
 * Created by uzzz on 10.11.14.
 */
public class CardDataTable  implements BaseColumns{

    private CardDataTable() {}

    public static final String TABLE_NAME = "card_data";
    public static final Uri CONTENT_URI =  Uri.parse("content://" + DatabaseHelper.AUTHORITY + "/main");
    public static final Uri CONTENT_ID_URI_BASE
            = Uri.parse("content://" + DatabaseHelper.AUTHORITY + "/main/");

    public static final String CONTENT_TYPE = "www.hearthstonewiki/db.table";
    public static final String CONTENT_ITEM_TYPE = "www.hearthstonewiki/db.table.item";

    public static final String COLUMN_DESCRIPTION = "description";

    public static final String DEFAULT_SORT_ORDER = COLUMN_DESCRIPTION + "DESC";



}
