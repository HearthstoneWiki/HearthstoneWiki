package www.hearthstonewiki.gui.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import www.hearthstonewiki.R;
import www.hearthstonewiki.gui.fragments.HeroInfoFragment;
import www.hearthstonewiki.gui.fragments.HeroListFragment;

public class HeroesActivity extends Activity implements
        HeroListFragment.OnFragmentInteractionListener {

    public boolean mIsPortrait;
    public boolean mIsLandscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heroes);

        mIsPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        mIsLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.heroes, menu);
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
    public void onChooseHeroInteraction(String heroName) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        //Toast.makeText(this, heroName, Toast.LENGTH_SHORT).show();
        HeroInfoFragment heroInfoFragment = (HeroInfoFragment) fm.findFragmentByTag(HeroInfoFragment.HERO_INFO_FRAGMENT_TAG);
        if (heroInfoFragment == null) { // Если фрагмент еще не был добавлен
            heroInfoFragment = HeroInfoFragment.newInstance(heroName);
            heroInfoFragment.setRetainInstance(true);
            ft.add(R.id.heroInfoView, heroInfoFragment, HeroInfoFragment.HERO_INFO_FRAGMENT_TAG);
            ft.addToBackStack(null);
        }
        else {
            heroInfoFragment.applyNewHero(heroName);
        }
        ft.commit();
    }


}
