package www.hearthstonewiki.gui.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import www.hearthstonewiki.R;
import www.hearthstonewiki.gui.fragments.CardListFragment;
import www.hearthstonewiki.gui.fragments.FilterFragment;

public class CardListActivity extends Activity implements FilterFragment.OnFragmentInteractionListener, View.OnClickListener {

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

    private String mSearchingStr = null;
    private String mSelectedHero = null;

    private boolean mIsLandscape;
    private boolean mIsPortait;

    public static final String SEARCHING_STRING = "searching_string";
    public static final String SELECTED_HERO = "selected_hero";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);

        if (savedInstanceState != null) {
            mSearchingStr = savedInstanceState.getString(SEARCHING_STRING);
            mSelectedHero = savedInstanceState.getString(SELECTED_HERO);
        }

        mIsPortait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        mIsLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;


        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        CardListFragment cardListFragment = (CardListFragment) fm.findFragmentByTag(CardListFragment.CARD_LIST_FRAGMENT_TAG);
        if (cardListFragment != null) {
            if (mIsPortait) {
                FilterFragment filterFragment = (FilterFragment) fm.findFragmentById(R.id.filterFragment);
                ft.hide(filterFragment);
                ft.commit();
                ImageButton showFilterImgBtn = (ImageButton) findViewById(R.id.showFilterImageButton);
                showFilterImgBtn.setOnClickListener(this);
            }
            updateSelectedHeroView();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.card_list, menu);
        return true;
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


        if (mIsPortait) { // Если portrait
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
            cardListFragment.applyNewFilter(mSearchingStr, mSelectedHero);
        }

        ft.commit();


    }

    private void updateSelectedHeroView() {
        LinearLayout heroImgView = (LinearLayout) findViewById(R.id.selectedHeroView);
        TextView selectedHeroText = (TextView) findViewById(R.id.selectedHeroTextView);

        if (mSelectedHero == null) {
            selectedHeroText.setText("");
            heroImgView.setBackgroundResource(R.drawable.bg_all_heroes);
        } else if (mSelectedHero.equals(DRUID)) {
            selectedHeroText.setText(R.string.druid_hero_name);
            heroImgView.setBackgroundResource(R.drawable.bg_druid);
        } else if (mSelectedHero.equals(HUNTER)) {
            selectedHeroText.setText(R.string.hunter_hero_name);
            heroImgView.setBackgroundResource(R.drawable.bg_hunter);
        } else if (mSelectedHero.equals(WARlOCK)) {
            selectedHeroText.setText(R.string.warlock_hero_name);
            heroImgView.setBackgroundResource(R.drawable.bg_warlock);
        } else if (mSelectedHero.equals(WARRIOR)) {
            selectedHeroText.setText(R.string.warrior_hero_name);
            heroImgView.setBackgroundResource(R.drawable.bg_warrior);
        } else if (mSelectedHero.equals(PRIEST)) {
            selectedHeroText.setText(R.string.priest_hero_name);
            heroImgView.setBackgroundResource(R.drawable.bg_priest);
        } else if (mSelectedHero.equals(MAGE)) {
            selectedHeroText.setText(R.string.mage_hero_name);
            heroImgView.setBackgroundResource(R.drawable.bg_mage);
        } else if (mSelectedHero.equals(NEUTRAL)) {
            selectedHeroText.setText(R.string.neutral_hero_name);
            heroImgView.setBackgroundResource(R.drawable.bg_neutral);
        } else if (mSelectedHero.equals(PALADIN)) {
            selectedHeroText.setText(R.string.paladin_hero_name);
            heroImgView.setBackgroundResource(R.drawable.bg_paladin);
        } else if (mSelectedHero.equals(ROGUE)) {
            selectedHeroText.setText(R.string.rogue_hero_name);
            heroImgView.setBackgroundResource(R.drawable.bg_rogue);
        } else if (mSelectedHero.equals(SHAMAN)) {
            selectedHeroText.setText(R.string.shaman_hero_name);
            heroImgView.setBackgroundResource(R.drawable.bg_shaman);
        }

        heroImgView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.showFilterImageButton) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            FilterFragment filterFragment = (FilterFragment) fm.findFragmentById(R.id.filterFragment);
            if (filterFragment.isHidden()) {
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
}
