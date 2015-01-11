package www.hearthstonewiki.gui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import www.hearthstonewiki.R;
import www.hearthstonewiki.services.APIService;


public class MenuActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button getUpdateButton;
        Button lookCardsButton;

        setContentView(R.layout.activity_main);
//        getFragmentManager().beginTransaction()
//                .replace(android.R.id.content, new LoaderFragment())
//                .commit();
//        ContentValues v = new ContentValues();
        //v.put("_id", 3);
        //v.put("description", "LaaaaaL");
        //getContentResolver().delete(CardDataTable.CONTENT_URI, "3", ["id"]);



        getUpdateButton = (Button)findViewById(R.id.get_update_button);
        getUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag", "msg");
                APIService.startActionGetUpdate(MenuActivity.this);
            }
        });

        lookCardsButton = (Button)findViewById(R.id.look_cards_button);
        lookCardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag", "cards");
                Intent i = new Intent(MenuActivity.this, CardsActivity.class);
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
}
