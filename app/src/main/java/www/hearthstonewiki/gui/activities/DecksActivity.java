package www.hearthstonewiki.gui.activities;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import www.hearthstonewiki.R;
import www.hearthstonewiki.db.DatabaseHelper;
import www.hearthstonewiki.db.tables.HeroPowerTable;
import www.hearthstonewiki.gui.views.CardTransformation;

public class DecksActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.decks_layout);

        String heroClass = "Shaman";
        final String[] HERO_POWER_PROJECTION = new String[] {
                HeroPowerTable._ID,
        };
        DatabaseHelper mDbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(HeroPowerTable.TABLE_NAME,
                    HERO_POWER_PROJECTION,
                    HeroPowerTable.COLUMN_CLASS + "=" + "'Shaman'",
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
        ImageView imageView = (ImageView) findViewById(R.id.power);

        Picasso.with(this)
                .load(url)
                .resize(434, 585)
                .transform(tr)
                .into(imageView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.decks_acivity, menu);
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
}
