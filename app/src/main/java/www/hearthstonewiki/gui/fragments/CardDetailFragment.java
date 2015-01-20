package www.hearthstonewiki.gui.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import www.hearthstonewiki.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CardDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CardDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class CardDetailFragment extends Fragment implements View.OnClickListener {

    public static final String CARD_DETAIL_FRAGMENT_TAG = "cardDetailFragment";

    private static final String ARG_CARD_ID = "cardId";

    private String mCardId;

    private OnFragmentInteractionListener mListener;

    public static CardDetailFragment newInstance(String cardId) {
        CardDetailFragment fragment = new CardDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CARD_ID, cardId);
        fragment.setArguments(args);
        return fragment;
    }
    public CardDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCardId = getArguments().getString(ARG_CARD_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_detale, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView cardImageView = (ImageView) view.findViewById(R.id.cardDetailImageView);
        String url = "http://wow.zamimg.com/images/hearthstone/cards/enus/original/" + mCardId + ".png";
        Picasso.with(getActivity())
                .load(url)
                .resize(800, 1200)
                .into(cardImageView);
        ImageButton closeButton = (ImageButton) view.findViewById(R.id.closeDetailImageButton);
        closeButton.setOnClickListener(this);
        view.setOnClickListener(this);
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
    public void onClick(View view) {
        if (view.getId() == R.id.closeDetailImageButton) {
            if (mListener != null) {
                mListener.onCloseFragmentInteraction();
            }
        }
    }

    public interface OnFragmentInteractionListener {
        public void onCloseFragmentInteraction();
    }

}
