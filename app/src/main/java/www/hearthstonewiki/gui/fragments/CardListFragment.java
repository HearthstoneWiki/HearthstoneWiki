package www.hearthstonewiki.gui.fragments;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import www.hearthstonewiki.R;

import www.hearthstonewiki.db.tables.CardDataTable;
import www.hearthstonewiki.gui.activities.ImageAdapter;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {@link //Callbacks}
 * interface.
 */
public class CardListFragment extends Fragment implements AbsListView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    static final String[] PROJECTION = new String[] {
            CardDataTable._ID,
    };


    public static final String CARD_LIST_FRAGMENT_TAG = "cardListFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SEARCHING_STRING = "searching_string";
    private static final String ARG_SELECTED_HERO = "selected_hero";

    // TODO: Rename and change types of parameters
    private String mSearchingStr;
    private String mSelectedHero;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private GridView mGridView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ImageAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static CardListFragment newInstance(String searchingString, String selectedHero) {
        CardListFragment fragment = new CardListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SEARCHING_STRING, searchingString);
        args.putString(ARG_SELECTED_HERO, selectedHero);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CardListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mSearchingStr = getArguments().getString(ARG_SEARCHING_STRING);
            mSelectedHero = getArguments().getString(ARG_SELECTED_HERO);
        }

        mAdapter = new ImageAdapter(getActivity(), null, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cardlist_grid, container, false);

        // Set the adapter
        mGridView = (GridView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mGridView).setAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);

        // Set OnItemClickListener so we can be notified on item clicks
        mGridView.setOnItemClickListener(this);

        //Toast.makeText(getActivity(), "In fragment " + mSearchingStr + " " + mSelectedHero, Toast.LENGTH_SHORT).show();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
             mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
             throw new ClassCastException(activity.toString()
                 + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            mListener.onItemClickInteraction((String)view.getTag());

        }
        //Toast.makeText(getActivity(), (String)view.getTag(), Toast.LENGTH_SHORT).show();
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mGridView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public void applyNewFilter(String searchingStr, String selectedHero) {
        mSearchingStr = searchingStr;
        mSelectedHero = selectedHero;

//        //mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, testList);
//        Cursor c = getActivity().getContentResolver().query(Uri.withAppendedPath(CardDataTable.CARD_URI_FILTER, "/" + mSelectedHero),
//                PROJECTION, null, null, null
//        );
//        mAdapter = new ImageAdapter(getActivity(), null, 0);
//        mAdapter.swapCursor(c);
//        mGridView = (GridView) getActivity().findViewById(android.R.id.list);
//        ((AdapterView<ListAdapter>) mGridView).setAdapter(mAdapter);
//
//        // Set OnItemClickListener so we can be notified on item clicks
//        mGridView.setOnItemClickListener(this);

        getLoaderManager().restartLoader(0, null, this);

    }

    public interface OnFragmentInteractionListener {
        public void onItemClickInteraction(String cardId);
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //CursorLoader cl = new CursorLoader(getActivity(), CardDataTable.CARD_URI,
        //        PROJECTION, null, null, null);

        android.net.Uri uri;

        if (mSelectedHero != null && mSearchingStr != null) {
            uri = Uri.withAppendedPath(CardDataTable.CARD_URI_FILTER, "/" + mSelectedHero + "/" + mSearchingStr);
        }
        else if (mSelectedHero != null) {
            uri = Uri.withAppendedPath(CardDataTable.CARD_URI_CLASS, "/" + mSelectedHero);
        }
        else if (mSearchingStr != null) {
            uri = Uri.withAppendedPath(CardDataTable.CARD_URI_SEARCH, "/" + mSearchingStr);
        }
        else {
            uri = CardDataTable.CARD_URI;
        }
        CursorLoader cl = new CursorLoader(getActivity(), uri, PROJECTION, null, null, null);
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
