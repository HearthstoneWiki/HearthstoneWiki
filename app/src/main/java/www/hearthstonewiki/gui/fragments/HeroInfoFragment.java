package www.hearthstonewiki.gui.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.HashMap;
import java.util.Map;

import www.hearthstonewiki.R;
import www.hearthstonewiki.db.DatabaseHelper;
import www.hearthstonewiki.db.tables.HeroPowerTable;
import www.hearthstonewiki.gui.activities.CardListActivity;
import www.hearthstonewiki.gui.views.CardTransformation;

public class HeroInfoFragment extends Fragment {

    public static final String HERO_INFO_FRAGMENT_TAG = "hero_info_fragment_tag";

    private Map<String, HeroData> mHeroesMap = new HashMap<String, HeroData>();

    private static final String ARG_HERO_NAME = "hero_name";

    private String mHeroName;

    public static HeroInfoFragment newInstance(String heroName) {
        HeroInfoFragment fragment = new HeroInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_HERO_NAME, heroName);
        fragment.setArguments(args);
        return fragment;
    }

    public HeroInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mHeroName = getArguments().getString(ARG_HERO_NAME);
        }
        initHeroMap();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hero_info, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        TextView textView = (TextView) view.findViewById(R.id.heroStoryTextView);
//        textView.setText(mHeroesMap.get(mHeroName).story_id);
//        view.setBackgroundResource(mHeroesMap.get(mHeroName).bg_img_id);
//        ImageView imageView = (ImageView) view.findViewById(R.id.heroImageView);
//        imageView.setImageResource(mHeroesMap.get(mHeroName).img_id);

        setHeroInfo();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_HERO_NAME, mHeroName);
    }

    private void initHeroMap() {

        mHeroesMap.put(CardListActivity.WARRIOR, new HeroData(R.string.warrior_description, R.drawable.bg_warrior, R.drawable.hero_warrior));
        mHeroesMap.put(CardListActivity.MAGE, new HeroData(R.string.mage_description, R.drawable.bg_mage, R.drawable.hero_mage));
        mHeroesMap.put(CardListActivity.PALADIN, new HeroData(R.string.paladin_description, R.drawable.bg_paladin, R.drawable.hero_paladin));
        mHeroesMap.put(CardListActivity.PRIEST, new HeroData(R.string.priest_description, R.drawable.bg_priest, R.drawable.hero_priest));
        mHeroesMap.put(CardListActivity.ROGUE, new HeroData(R.string.rogue_description, R.drawable.bg_rogue, R.drawable.hero_rogue));
        mHeroesMap.put(CardListActivity.SHAMAN, new HeroData(R.string.shaman_description, R.drawable.bg_shaman, R.drawable.hero_shaman));
        mHeroesMap.put(CardListActivity.WARlOCK, new HeroData(R.string.warlock_description, R.drawable.bg_warlock, R.drawable.hero_warlock));
        mHeroesMap.put(CardListActivity.HUNTER, new HeroData(R.string.hunter_description, R.drawable.bg_hunter, R.drawable.hero_hunter));
        mHeroesMap.put(CardListActivity.DRUID, new HeroData(R.string.druid_description, R.drawable.bg_druid, R.drawable.hero_druid));

    }


    public void applyNewHero(String heroName) {
        mHeroName = heroName;
        setHeroInfo();
    }

    private void setHeroInfo() {
        TextView textView = (TextView) getActivity().findViewById(R.id.heroStoryTextView);
        textView.setText(mHeroesMap.get(mHeroName).story_id);
        TextView name = (TextView) getActivity().findViewById(R.id.heroName);
        name.setText(mHeroName);
        getActivity().findViewById(R.id.heroInfoScrollView).setBackgroundResource(mHeroesMap.get(mHeroName).bg_img_id);
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.heroImageView);
        imageView.setImageResource(mHeroesMap.get(mHeroName).img_id);

        final String[] HERO_POWER_PROJECTION = new String[] {
                HeroPowerTable._ID,
        };
        DatabaseHelper mDbHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(HeroPowerTable.TABLE_NAME,
                HERO_POWER_PROJECTION,
                HeroPowerTable.COLUMN_CLASS + "=" + "'" + mHeroName + "'",
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        String cardID = cursor.getString(0);
        cursor.close();
        String PIC_SERVER_URL = "http://wow.zamimg.com/images/hearthstone/cards/enus/original/";
        String PIC_EXTENSION = ".png";
        String url = PIC_SERVER_URL + cardID + PIC_EXTENSION;

        Transformation tr = new CardTransformation();
        ImageView abilityImageView = (ImageView) getActivity().findViewById(R.id.abilityImageView);

        Picasso.with(getActivity())
                .load(url)
                .resize(434, 585)
                .transform(tr)
                .into(abilityImageView);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class HeroData {
        public int story_id;
        public int bg_img_id;
        public int img_id;

        public HeroData(int story_id, int bg_img_id, int img_id) {
            this.story_id = story_id;
            this.bg_img_id = bg_img_id;
            this.img_id = img_id;
        }
    }

}
