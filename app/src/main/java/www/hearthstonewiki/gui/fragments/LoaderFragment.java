package www.hearthstonewiki.gui.fragments;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

import www.hearthstonewiki.R;
import www.hearthstonewiki.db.tables.CardDataTable;

/**
 * Created by uzzz on 13.11.14.
 */
public class LoaderFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    SimpleCursorAdapter mAdapter;


    static final String[] PROJECTION = new String[] {
            CardDataTable.COLUMN_NAME,
            CardDataTable.COLUMN_TEXT,
            CardDataTable._ID,
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Html.from TODO
        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.card_item, null,
                PROJECTION,
                new int[] { R.id.name, R.id.description }, 0);
        setListAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //CursorLoader cl = new CursorLoader(getActivity(), CardDataTable.CARD_URI,
        //        PROJECTION, null, null, null);
        CursorLoader cl = new CursorLoader(getActivity(),
                Uri.withAppendedPath(CardDataTable.CARD_URI_FILTER, "/Warlock"),
                PROJECTION, null, null, null);
        return cl;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }
}
