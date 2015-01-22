package www.hearthstonewiki.gui.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import www.hearthstonewiki.R;
import www.hearthstonewiki.app.HearthstoneWikiApp;
import www.hearthstonewiki.gui.msg.APIStatusMsg;
import www.hearthstonewiki.services.APIService;
import www.hearthstonewiki.services.APIStatus;


public class MenuActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button lookDecsButton;
        Button lookCardsButton;

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        IntentFilter updateStatusFilter = new IntentFilter( APIService.API_STATUS_INTENT);
        UpdateStatusReceiver updateReceiver = new UpdateStatusReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(updateReceiver, updateStatusFilter);


        lookCardsButton = (Button)findViewById(R.id.look_cards_button);
        lookCardsButton.setBackgroundResource(R.drawable.button_style);
        lookCardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MenuActivity.this, CardListActivity.class);
                startActivity(i);
            }
        });

        lookDecsButton = (Button)findViewById(R.id.look_decks_button);
        lookDecsButton.setBackgroundResource(R.drawable.button_style);
        lookDecsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MenuActivity.this, HeroesActivity.class);
                startActivity(i);
            }
        });

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

        private UpdateStatusReceiver() {

        }
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(APIService.API_STATUS, -1);

            boolean showToast = false;
            String message = "";

            switch(status) {
                case APIStatus.CONNECTED:
                    APIService.startActionCheckUpdate(context);
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
