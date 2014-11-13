package www.hearthstonewiki;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;

import www.hearthstonewiki.db.tables.CardDataTable;

/**
 * Created by uzzz on 13.11.14.
 */
public class Loader implements LoaderManager.LoaderCallbacks<Cursor>{


    static final String[] PROJECTION = new String[] {
            CardDataTable._ID,
            CardDataTable.COLUMN_DESCRIPTION,
    };

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        CursorLoader cl = new CursorLoader(getActivity(), CardDataTable.CONTENT_URI,
                PROJECTION, null, null, null);
        cl.setUpdateThrottle(2000);
        return cl;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> cursorLoader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> cursorLoader) {

    }
}
