package www.hearthstonewiki.gui.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import www.hearthstonewiki.R;
import www.hearthstonewiki.gui.fragments.CardDetailFragment;
import www.hearthstonewiki.gui.fragments.CardListFragment;
import www.hearthstonewiki.gui.fragments.FilterFragment;

public class CardListActivity extends Activity implements
        FilterFragment.OnFragmentInteractionListener,
        View.OnClickListener,
        CardListFragment.OnFragmentInteractionListener,
        CardDetailFragment.OnFragmentInteractionListener {

    public static final String NEUTRAL = "Neutral";
    public static final String WARRIOR = "Warrior";
    public static final String MAGE = "Mage";
    public static final String PALADIN = "Paladin";
    public static final String PRIEST = "Priest";
    public static final String ROGUE = "Rogue";
    public static final String SHAMAN = "Shaman";
    public static final String WARlOCK = "Warlock";
    public static final String HUNTER = "Hunter";
    public static final String DRUID = "Druid";
    public static final String ALL = "All";

    private Map<String, HeroData> mHeroesMap = new HashMap<String, HeroData>();

    private String mSearchingStr = null;
    private String mSelectedHero = null;

    private boolean mIsLandscape;
    private boolean mIsPortrait;

    public static final String SEARCHING_STRING = "searching_string";
    public static final String SELECTED_HERO = "selected_hero";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_card_list);

        if (savedInstanceState != null) {
            mSearchingStr = savedInstanceState.getString(SEARCHING_STRING);
            mSelectedHero = savedInstanceState.getString(SELECTED_HERO);
        }

        mIsPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        mIsLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        initHeroMap();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        CardListFragment cardListFragment = (CardListFragment) fm.findFragmentByTag(CardListFragment.CARD_LIST_FRAGMENT_TAG);
        if (cardListFragment != null) {
            if (mIsPortrait) {
                FilterFragment filterFragment = (FilterFragment) fm.findFragmentById(R.id.filterFragment);
                ft.hide(filterFragment);
                ft.commit();
            }
            updateSelectedHeroView();
        }
        if (mIsPortrait) {
            ImageButton showFilterImgBtn = (ImageButton) findViewById(R.id.showFilterImageButton);
            showFilterImgBtn.setOnClickListener(this);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.card_list, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SEARCHING_STRING, mSearchingStr);
        outState.putString(SELECTED_HERO, mSelectedHero);
    }

    @Override
    public void onClickSearchBtnInteraction(String searchingStr, String selectedHero) {
        mSearchingStr = searchingStr;
        mSelectedHero = selectedHero;

        updateSelectedHeroView();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();


        if (mIsPortrait) { // Если portrait
            FilterFragment filterFragment = (FilterFragment) fm.findFragmentById(R.id.filterFragment);
            if (!filterFragment.isHidden()) {
                ft.hide(filterFragment);
                ImageButton showFilterImgBtn = (ImageButton) findViewById(R.id.showFilterImageButton);
                showFilterImgBtn.setOnClickListener(this);
            }
        }

        CardListFragment cardListFragment = (CardListFragment) fm.findFragmentByTag(CardListFragment.CARD_LIST_FRAGMENT_TAG);
        if (cardListFragment == null) { // Если фрагмент со списком карт еще не был добавлен
            cardListFragment = CardListFragment.newInstance(mSearchingStr, mSelectedHero);
            cardListFragment.setRetainInstance(true);
            ft.add(R.id.cardListLinearLayout, cardListFragment, CardListFragment.CARD_LIST_FRAGMENT_TAG);
            //Toast.makeText(this, mSearchingStr + " " + mSelectedHero, Toast.LENGTH_SHORT).show();
        }
        else {
            if (!mIsLandscape) {
                ((ImageButton) findViewById(R.id.showFilterImageButton)).setImageResource(R.drawable.down_ico);
            }
            cardListFragment.applyNewFilter(mSearchingStr, mSelectedHero);
        }

        ft.commit();


    }

    @Override
    public void onSwitchHeroInteraction(String searchingStr, String selectedHero) {
        mSearchingStr = searchingStr;
        mSelectedHero = selectedHero;

        updateSelectedHeroView();

        FragmentManager fm = getFragmentManager();

        CardListFragment cardListFragment = (CardListFragment) fm.findFragmentByTag(CardListFragment.CARD_LIST_FRAGMENT_TAG);
        if (cardListFragment != null) {
            //Toast.makeText(this, "onSwitchHeroInteraction", Toast.LENGTH_SHORT).show();

            cardListFragment.applyNewFilter(mSearchingStr, mSelectedHero);
        }
        else {
            FragmentTransaction ft = fm.beginTransaction();
            cardListFragment = CardListFragment.newInstance(mSearchingStr, mSelectedHero);
            cardListFragment.setRetainInstance(true);
            ft.add(R.id.cardListLinearLayout, cardListFragment, CardListFragment.CARD_LIST_FRAGMENT_TAG);
            ft.commit();
        }

        CardDetailFragment cardDetailFragment = (CardDetailFragment) fm.findFragmentByTag(CardDetailFragment.CARD_DETAIL_FRAGMENT_TAG);
        if (cardDetailFragment != null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(cardDetailFragment);
            ft.commit();
        }
    }

    private void updateSelectedHeroView() {

        FrameLayout heroBgView = (FrameLayout) findViewById(R.id.cardInfoFrameLayout);
        TextView selectedHeroText = (TextView) findViewById(R.id.selectedHeroTextView);


        if (mSelectedHero == null) {
            if (selectedHeroText != null) {
                selectedHeroText.setText(mHeroesMap.get(ALL).hero_name_id);
            }
            heroBgView.setBackgroundResource(mHeroesMap.get(ALL).bg_img_id);
        }
        else {
            if (selectedHeroText != null) {
                //Toast.makeText(this, mSelectedHero, Toast.LENGTH_SHORT).show();
                selectedHeroText.setText(mHeroesMap.get(mSelectedHero).hero_name_id);
            }
            heroBgView.setBackgroundResource(mHeroesMap.get(mSelectedHero).bg_img_id);
        }


//        } else if (mSelectedHero.equals(DRUID)) {
//            if (selectedHeroText != null)
//            selectedHeroText.setText(R.string.druid_hero_name);
//            heroBgView.setBackgroundResource(R.drawable.bg_druid);
//        } else if (mSelectedHero.equals(HUNTER)) {
//            if (selectedHeroText != null)
//            selectedHeroText.setText(R.string.hunter_hero_name);
//            heroBgView.setBackgroundResource(R.drawable.bg_hunter);
//        } else if (mSelectedHero.equals(WARlOCK)) {
//            if (selectedHeroText != null)
//            selectedHeroText.setText(R.string.warlock_hero_name);
//            heroBgView.setBackgroundResource(R.drawable.bg_warlock);
//        } else if (mSelectedHero.equals(WARRIOR)) {
//            if (selectedHeroText != null)
//            selectedHeroText.setText(R.string.warrior_hero_name);
//            heroBgView.setBackgroundResource(R.drawable.bg_warrior);
//        } else if (mSelectedHero.equals(PRIEST)) {
//            if (selectedHeroText != null)
//            selectedHeroText.setText(R.string.priest_hero_name);
//            heroBgView.setBackgroundResource(R.drawable.bg_priest);
//        } else if (mSelectedHero.equals(MAGE)) {
//            if (selectedHeroText != null)
//            selectedHeroText.setText(R.string.mage_hero_name);
//            heroBgView.setBackgroundResource(R.drawable.bg_mage);
//        } else if (mSelectedHero.equals(NEUTRAL)) {
//            if (selectedHeroText != null)
//            selectedHeroText.setText(R.string.neutral_hero_name);
//            heroBgView.setBackgroundResource(R.drawable.bg_neutral);
//        } else if (mSelectedHero.equals(PALADIN)) {
//            if (selectedHeroText != null)
//            selectedHeroText.setText(R.string.paladin_hero_name);
//            heroBgView.setBackgroundResource(R.drawable.bg_paladin);
//        } else if (mSelectedHero.equals(ROGUE)) {
//            if (selectedHeroText != null)
//            selectedHeroText.setText(R.string.rogue_hero_name);
//            heroBgView.setBackgroundResource(R.drawable.bg_rogue);
//        } else if (mSelectedHero.equals(SHAMAN)) {
//            if (selectedHeroText != null)
//            selectedHeroText.setText(R.string.shaman_hero_name);
//            heroBgView.setBackgroundResource(R.drawable.bg_shaman);
//        }

        if (!mIsLandscape) {
            ((LinearLayout)findViewById(R.id.selectedHeroView)).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.showFilterImageButton) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            FilterFragment filterFragment = (FilterFragment) fm.findFragmentById(R.id.filterFragment);
            if (filterFragment.isHidden()) {
                CardDetailFragment cardDetailFragment = (CardDetailFragment) fm.findFragmentByTag(CardDetailFragment.CARD_DETAIL_FRAGMENT_TAG);
                if (cardDetailFragment != null) {
                    ft.remove(cardDetailFragment);
                }
                ft.show(filterFragment);
                ((ImageButton)view).setImageResource(R.drawable.up_ico);
            }
            else {
                ft.hide(filterFragment);
                ((ImageButton)view).setImageResource(R.drawable.down_ico);
            }
            ft.commit();

        }
    }

    @Override
    public void onItemClickInteraction(String cardId) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        CardDetailFragment cardDetailFragment = (CardDetailFragment) fm.findFragmentByTag(CardDetailFragment.CARD_DETAIL_FRAGMENT_TAG);

        if (cardDetailFragment == null) {
            cardDetailFragment = CardDetailFragment.newInstance(cardId);
            cardDetailFragment.setRetainInstance(true);
            ft.add(R.id.cardInfoFrameLayout, cardDetailFragment, CardDetailFragment.CARD_DETAIL_FRAGMENT_TAG);
            ft.commit();
        }
    }

    @Override
    public void onCloseFragmentInteraction() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        CardDetailFragment cardDetailFragment = (CardDetailFragment) fm.findFragmentByTag(CardDetailFragment.CARD_DETAIL_FRAGMENT_TAG);
        if (cardDetailFragment != null) {
            ft.remove(cardDetailFragment);
            ft.commit();
        }
    }

    private void initHeroMap() {
        mHeroesMap.put(WARRIOR, new HeroData(R.drawable.bg_warrior, R.string.warrior_hero_name));
        mHeroesMap.put(MAGE, new HeroData(R.drawable.bg_mage, R.string.mage_hero_name));
        mHeroesMap.put(PALADIN, new HeroData(R.drawable.bg_paladin, R.string.paladin_hero_name));
        mHeroesMap.put(PRIEST, new HeroData(R.drawable.bg_priest, R.string.priest_hero_name));
        mHeroesMap.put(ROGUE, new HeroData(R.drawable.bg_rogue, R.string.rogue_hero_name));
        mHeroesMap.put(SHAMAN, new HeroData(R.drawable.bg_shaman, R.string.shaman_hero_name));
        mHeroesMap.put(WARlOCK, new HeroData(R.drawable.bg_warlock, R.string.warlock_hero_name));
        mHeroesMap.put(HUNTER, new HeroData(R.drawable.bg_hunter, R.string.hunter_hero_name));
        mHeroesMap.put(DRUID, new HeroData(R.drawable.bg_druid, R.string.druid_hero_name));
        mHeroesMap.put(NEUTRAL, new HeroData(R.drawable.bg_neutral, R.string.neutral_hero_name));
        mHeroesMap.put(ALL, new HeroData(R.drawable.bg_all, R.string.all_hero));
    }

    public class HeroData {
        public int bg_img_id;
        public int hero_name_id;

        public HeroData(int bg_img_id, int hero_name_id) {
            this.bg_img_id = bg_img_id;
            this.hero_name_id = hero_name_id;
        }
    }
}
