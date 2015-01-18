package www.hearthstonewiki.db.tables;

import android.net.Uri;
import android.provider.BaseColumns;

import www.hearthstonewiki.db.DatabaseHelper;

/**
 * Created by uzzz on 10.11.14.
 */
public class CardDataTable implements BaseColumns{

    private CardDataTable() {}

    public static final String TABLE_NAME = "card_data";

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TEXT = "text_description";
    public static final String COLUMN_COST = "cost";
    public static final String COLUMN_CLASS = "class";

    public static final Uri CARD_URI
            = Uri.parse("content://" + DatabaseHelper.AUTHORITY + "/card_data/all/");
    public static final Uri CARD_URI_ID
            = Uri.parse("content://" + DatabaseHelper.AUTHORITY + "/card_data/item/");
    public static final Uri CARD_URI_CLASS
            = Uri.parse("content://" + DatabaseHelper.AUTHORITY + "/card_data/class/");
    public static final Uri CARD_URI_FILTER
            = Uri.parse("content://" + DatabaseHelper.AUTHORITY + "/card_data/filter/");
    public static final Uri CARD_URI_SEARCH
            = Uri.parse("content://" + DatabaseHelper.AUTHORITY + "/card_data/search/");

    public static final String CONTENT_TYPE = "www.hearthstonewiki/db.table.cards";
    public static final String CONTENT_ITEM_TYPE = "www.hearthstonewiki/db.table.card_item";

    public static final String DEFAULT_SORT_ORDER = COLUMN_NAME + " ASC";

}
