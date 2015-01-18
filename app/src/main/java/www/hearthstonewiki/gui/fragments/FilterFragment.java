package www.hearthstonewiki.gui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import java.util.HashMap;
import java.util.Map;

import www.hearthstonewiki.R;
import www.hearthstonewiki.gui.activities.CardListActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FilterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class FilterFragment extends Fragment implements ToggleButton.OnCheckedChangeListener, View.OnClickListener {

    private Integer mSelectedHeroBtn;
    private Map<Integer, String> mHeroesBtnMap = new HashMap<Integer, String>();
    private String mSearchingString;

    private OnFragmentInteractionListener mListener;

    public static FilterFragment newInstance() {
        FilterFragment fragment = new FilterFragment();
        return fragment;
    }

    public FilterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_filter, container, false);

        mHeroesBtnMap.put(R.id.neutralToggleButton, CardListActivity.NEUTRAL);
        mHeroesBtnMap.put(R.id.warriorToggleButton, CardListActivity.WARRIOR);
        mHeroesBtnMap.put(R.id.mageToggleButton, CardListActivity.MAGE);
        mHeroesBtnMap.put(R.id.paladinToggleButton, CardListActivity.PALADIN);
        mHeroesBtnMap.put(R.id.priestToggleButton, CardListActivity.PRIEST);
        mHeroesBtnMap.put(R.id.rogueToggleButton, CardListActivity.ROGUE);
        mHeroesBtnMap.put(R.id.shamanToggleButton, CardListActivity.SHAMAN);
        mHeroesBtnMap.put(R.id.warlockToggleButton, CardListActivity.WARlOCK);
        mHeroesBtnMap.put(R.id.hunterToggleButton, CardListActivity.HUNTER);
        mHeroesBtnMap.put(R.id.druidToggleButton, CardListActivity.DRUID);

        for (Integer key: mHeroesBtnMap.keySet()) {
            ToggleButton toggleButton;
            toggleButton = (ToggleButton) v.findViewById(key);
            toggleButton.setOnCheckedChangeListener(this);
        }

        ImageButton searchButton = (ImageButton) v.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (mSelectedHeroBtn != null) {
                ToggleButton btn;
                btn = (ToggleButton) getActivity().findViewById(mSelectedHeroBtn);
                btn.setChecked(false);
            }
            mSelectedHeroBtn = buttonView.getId();
        }
        else {
            if (mSelectedHeroBtn == buttonView.getId())
                mSelectedHeroBtn = null;
        }

        if (mListener != null && mSelectedHeroBtn != null) {
            mListener.onSwitchHeroInteraction(mHeroesBtnMap.get(mSelectedHeroBtn));
        }
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            EditText editText = (EditText) getActivity().findViewById(R.id.searchEditText);
            mSearchingString = editText.getText().toString();

            mListener.onClickSearchBtnInteraction(mSearchingString, mHeroesBtnMap.get(mSelectedHeroBtn));
        }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onClickSearchBtnInteraction(String searchingStr, String selectedHero);
        public void onSwitchHeroInteraction(String selectedHero);
    }
}
