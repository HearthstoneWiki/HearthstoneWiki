package www.hearthstonewiki.gui.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import www.hearthstonewiki.R;
import www.hearthstonewiki.db.tables.CardDataTable;
import www.hearthstonewiki.gui.msg.APIStatusMsg;
import www.hearthstonewiki.services.APIService;
import www.hearthstonewiki.services.APIStatus;


public class MenuActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button getUpdateButton;
        Button lookCardsButton;

        setContentView(R.layout.activity_main);

        ImageView iv = (ImageView)findViewById(R.id.imageView);
        Picasso.with(this).load("http://wow.zamimg.com/images/hearthstone/cards/enus/original/EX1_165.png")
                .resize(434, 658)
                .into(iv);


        IntentFilter updateStatusFilter = new IntentFilter( APIService.API_STATUS_INTENT);
        UpdateStatusReceiver updateReceiver = new UpdateStatusReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(updateReceiver, updateStatusFilter);


        getUpdateButton = (Button)findViewById(R.id.get_update_button);
        getUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIService.startActionGetUpdate(MenuActivity.this);
            }
        });

        lookCardsButton = (Button)findViewById(R.id.look_cards_button);
        lookCardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("tag", "cards");
                Intent i = new Intent(MenuActivity.this, CardListActivity.class);
                //Intent i = new Intent(MenuActivity.this, CardsActivity.class);
                startActivity(i);
            }
        });

        Button checkConnectionButton = (Button)findViewById(R.id.check_connection_button);
        checkConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIService.startActionCheckConnection(MenuActivity.this);
            }
        });

        APIService.startActionCheckConnection(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
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

    private class UpdateStatusReceiver extends BroadcastReceiver
    {

        boolean mCheckUpdate = true;
        private UpdateStatusReceiver() {

        }
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(APIService.API_STATUS, -1);

            boolean showToast = false;
            String message = "";

            switch(status) {
                case APIStatus.CONNECTED:
                    if(mCheckUpdate)
                        APIService.startActionCheckUpdate(context);
                    mCheckUpdate = false;
                    break;
                case APIStatus.NOT_CONNECTED:
                    message = APIStatusMsg.NO_CONNECTION;
                    showToast = true;
                    break;
                case APIStatus.ACTUAL:
                    message = APIStatusMsg.ACTUAL;
                    showToast = true;
                    break;
                case APIStatus.NEED_TO_UPDATE:
                    APIService.startActionGetUpdate(context);
                    break;
                case APIStatus.NEED_TO_INITIALIZE:
                    APIService.startActionInitData(context);
                    break;
                case APIStatus.UPDATED:
                    message = APIStatusMsg.UPDATED;
                    showToast = true;
                    break;
                case APIStatus.ERROR:
                    message = APIStatusMsg.ERROR;
                    showToast = true;
                    break;
                default:
                    break;
            }

            if(showToast) {

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast_layout,
                        (ViewGroup) findViewById(R.id.toast_layout_root));

                TextView text_place = (TextView) layout.findViewById(R.id.text);

                text_place.setText(message);

                Toast toast = new Toast(context);
                toast.setGravity(Gravity.BOTTOM, 0, 50);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            }

        }
    }
}
